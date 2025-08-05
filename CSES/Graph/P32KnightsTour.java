import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P32KnightsTour {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Knights-Tour",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = 8;
        board = new Node[n + 1][n + 1];
        g = new HashMap<>();
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++) {
                // Info: Need to use the same instances in both matrix and map
                board[i][j] = new Node(i, j);
                g.put(board[i][j], new ArrayList<>());
            }
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                connect(i, j, n);
        final int x = fr.nextInt(), y = fr.nextInt();
        fw.attachOutput(solve(n, x, y));
        fw.printOutput();
    }

    private static Map<Node, List<Node>> g;
    private static Node board[][];

    private static class Node {
        final int x, y;
        int moveNumber, active;
        boolean visited;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.moveNumber = 0;
            this.active = 64;
            this.visited = false;
        }

        public void setMove(int move) {
            this.moveNumber = move;
        }

        public void setVisited(boolean flag) {
            this.visited = flag;
        }

        public void setActive(int num) {
            this.active = num;
        }

        public int X() {
            return x;
        }

        public int Y() {
            return y;
        }

        public int getMove() {
            return moveNumber;
        }

        public boolean getVisited() {
            return visited;
        }

        public int getActive() {
            return active;
        }
    }

    public static void connect(int x, int y, int n) {
        if (x - 2 >= 1 && y - 1 >= 1)
            g.get(board[x][y]).add(board[x - 2][y - 1]);
        if (x - 2 >= 1 && y + 1 <= n)
            g.get(board[x][y]).add(board[x - 2][y + 1]);
        if (x + 2 <= n && y - 1 >= 1)
            g.get(board[x][y]).add(board[x + 2][y - 1]);
        if (x + 2 <= n && y + 1 <= n)
            g.get(board[x][y]).add(board[x + 2][y + 1]);
        if (x - 1 >= 1 && y - 2 >= 1)
            g.get(board[x][y]).add(board[x - 1][y - 2]);
        if (x - 1 >= 1 && y + 2 <= n)
            g.get(board[x][y]).add(board[x - 1][y + 2]);
        if (x + 1 <= n && y - 2 >= 1)
            g.get(board[x][y]).add(board[x + 1][y - 2]);
        if (x + 1 <= n && y + 2 <= n)
            g.get(board[x][y]).add(board[x + 1][y + 2]);
    }

    public static StringBuilder solve(final int n, final int x, final int y) {
        board[x][y].visited = true;
        board[x][y].setMove(1);
        backtrack(x, y, 1);
        final StringBuilder output = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                output.append(board[i][j].getMove()).append(" ");
            output.append("\n");
        }
        return output;
    }

    public static boolean backtrack(int x, int y, int move) {
        if (move == 64) {
            return true;
        }
        List<Node> candidates = new ArrayList<>();
        for (Node neighbor : g.get(board[x][y])) {
            if (!neighbor.getVisited()) {
                int onward = 0;
                for (Node nn : g.get(neighbor)) {
                    if (!nn.getVisited())
                        onward++;
                }
                neighbor.setActive(onward);
                candidates.add(neighbor);
            }
        }
        candidates.sort((a, b) -> Integer.compare(a.getActive(), b.getActive()));
        for (Node neighbor : candidates) {      // Try possible candidates only, not all
            neighbor.setVisited(true);
            neighbor.setMove(move + 1);
            if (backtrack(neighbor.x, neighbor.y, move + 1))
                return true;
            neighbor.setVisited(false);
            neighbor.setMove(0);
        }
        return false;
    }
}
