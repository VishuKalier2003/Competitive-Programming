import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class EscapeStones {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Escape-Stones", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(solve(fr.next())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static class Node {
        protected Node next, prev;
        protected final int value;

        public Node(int n) {
            this.value = n;
            this.next = this.prev = null;
        }

        public static void insertBetween(Node n1, Node n2, Node x) {
            n1.next = x;
            n2.prev = x;
            x.next = n2;
            x.prev = n1;
        }
    }

    public static StringBuilder solve(final String s) {
        Node head = new Node(-1), tail = new Node(-1), temp = head;
        head.next = tail; tail.prev = head;
        int n = s.length();
        Node l = head, r = tail;
        for(int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            Node x = new Node(i+1);
            Node.insertBetween(l, r, x);
            if(ch == 'l')
                r = x;
            else
                l = x;
        }
        final StringBuilder out = new StringBuilder();
        while(temp.next != null) {
            if(temp.value != -1)
                out.append(temp.value).append("\n");
            temp = temp.next;
        }
        return out;
    }
}
