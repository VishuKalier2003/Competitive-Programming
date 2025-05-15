from flask import Flask, request, jsonify
from binance.client import Client
import threading
import time
import uuid
import requests
from decimal import Decimal, getcontext

# Increase Decimal precision
getcontext().prec = 12

app = Flask(__name__)

# ── Binance Testnet Client ────────────────────────────────────────────────────
API_KEY    = "fB6C0LVJR5ywSToRP9LVXNYrPj7SclL8KL832cqhSkSLbDKU5Mzsj1AH41uaGi3R"
API_SECRET = "ewAqF4IlIO36ObUwEIvfQl6vu2ohzbp7X4qffTo1m5fhnF011xF0kMCW8MSUynJr"
client = Client(API_KEY, API_SECRET, testnet=True)

# ── In-Memory Account Manager ─────────────────────────────────────────────────
class AccountManager:
    def __init__(self, client, asset="USDT"):
        acct = client.get_account()
        self.asset = asset
        self.balance = Decimal(
            next(a for a in acct['balances'] if a['asset']==asset)['free']
        )

    def apply_pnl(self, pnl_amount: Decimal) -> Decimal:
        self.balance += pnl_amount
        return self.balance

# ── Global Task Store ────────────────────────────────────────────────────────
task_results = {}

# ── Core Trade Logic with Callback ────────────────────────────────────────────
def auto_trade(symbol: str,
               qty: Decimal,
               profit_pct: Decimal,
               loss_pct: Decimal,
               client: Client,
               acct_mgr: AccountManager,
               task_id: str,
               callback_url: str = None,
               max_wait_steps: int = 3600):
    entry_price = Decimal(client.get_symbol_ticker(symbol=symbol)['price'])
    client.order_market_buy(symbol=symbol, quantity=float(qty))
    print(f"[BUY]  {symbol} @ {entry_price:.8f}")

    step = 0
    while True:
        price = Decimal(client.get_symbol_ticker(symbol=symbol)['price'])
        pnl_pct = (price - entry_price) / entry_price * Decimal("100")
        print(f"[MON]  step={step} | price={price:.8f} | P/L%={pnl_pct:.8f}")

        exit_reason = None
        if pnl_pct >= profit_pct:
            exit_reason = "COMPLETED"
        elif pnl_pct <= -loss_pct:
            exit_reason = "STOPPED_LOSS"
        elif step >= max_wait_steps:
            exit_reason = "FORCED_EXIT"

        if exit_reason:
            client.order_market_sell(symbol=symbol, quantity=float(qty))
            print(f"[EXIT:{exit_reason}] {symbol} @ {price:.8f}, P/L%={pnl_pct:.8f}")
            pnl_amount = (price - entry_price) * qty
            new_bal = acct_mgr.apply_pnl(pnl_amount)

            result = {
                "status":         exit_reason,
                "symbol":         symbol,
                "entry_price":    str(entry_price),
                "exit_price":     str(price),
                "pnl_amount":     str(pnl_amount),
                "pnl_percent":    str(pnl_pct),
                "ledger_balance": str(new_bal)
            }
            task_results[task_id] = result

            # send webhook callback if provided
            if callback_url:
                try:
                    requests.post(callback_url, json={"task_id": task_id, **result}, timeout=5)
                except Exception as e:
                    print(f"[CALLBACK ERROR] {e}")
            break

        step += 1
        time.sleep(1)

# ── Flask Endpoints ──────────────────────────────────────────────────────────
@app.route('/trade', methods=['POST'])
def start_trade():
    data = request.get_json(force=True)
    for field in ("symbol", "quantity", "profit_percent", "loss_percent"):
        if field not in data:
            return jsonify({"error": f"Missing field: {field}"}), 400

    symbol       = data["symbol"]
    qty          = Decimal(str(data["quantity"]))
    profit_pct   = Decimal(str(data["profit_percent"]))
    loss_pct     = Decimal(str(data["loss_percent"]))
    callback_url = data.get("callback_url")

    task_id = str(uuid.uuid4())
    task_results[task_id] = {"status": "RUNNING"}

    acct_mgr = AccountManager(client)
    thread = threading.Thread(
        target=auto_trade,
        args=(symbol, qty, profit_pct, loss_pct, client, acct_mgr, task_id, callback_url),
        daemon=True
    )
    thread.start()

    return jsonify({"task_id": task_id, "status": "STARTED"}), 202

@app.route('/status/<task_id>', methods=['GET'])
def get_status(task_id):
    if task_id not in task_results:
        return jsonify({"error": "Task not found"}), 404
    return jsonify(task_results[task_id])

if __name__ == '__main__':
    app.run(debug=True, port=5000)
