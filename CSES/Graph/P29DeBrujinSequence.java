// de-Brujin Graph (Eulerian Walk)

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P29DeBrujinSequence {
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
        }, "De-Brujin-Sequence",
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
        g = new ArrayList<>();
        // Info: We construct nodes of (n-1) mers, hence of size k which is 2^(n-1)
        final int n = fr.nextInt(), k = 1 << (n - 1);
        inDegree = new int[k];
        outDegree = new int[k];
        for (int i = 0; i < k; i++)
            g.add(new ArrayList<>());
        fw.attachOutput(solve(n, k));
        fw.printOutput();
    }

    private static List<List<Node>> g;
    private static int inDegree[], outDegree[];

    private static class Node {     // Node class defined to store the data of edge (from, to, cost)
        final int start, end, weight;

        public Node(int s, int e, int w) {
            this.start = s;
            this.end = e;
            this.weight = w;
        }

        public int start() {
            return start;
        }

        public int end() {
            return end;
        }

        public int weight() {
            return weight;
        }
    }

    // Note: 0 odd degree graph has eulerian cycle, and 2 odd degree graph has eulerian walk
    public static StringBuilder solve(final int n, final int k) {
        startNode = 0;
        // Info: creating de-Brujin's graph
        for (int i = 0; i < (1 << n); i++) {
            int n1 = i >> 1; // n-1 LSB bits
            int n2 = i & ((1 << (n - 1)) - 1); // n-1 MSB bits
            int edge = i & 1; // LSB bit
            g.get(n1).add(new Node(n1, n2, edge)); // creating the edge
            outDegree[n1]++;
            inDegree[n2]++;
        }
        // Note: de-Brujin Graphs are balanced and always have eulerian walk so we need
        // not check for presence of eulerian walk
        for (int i = 0; i < k; i++) {
            if (outDegree[i] - inDegree[i] == 1)
                startNode = i;
        }
        return new StringBuilder().append(hierholzer(n, k));
    }

    private static int startNode;

    public static StringBuilder hierholzer(final int n, final int k) {
        // array to store the edge that needs to be traversed next
        final int edgePtr[] = new int[k];
        ArrayDeque<Integer> vertexStack = new ArrayDeque<>();
        ArrayDeque<Integer> edgeStack = new ArrayDeque<>();
        List<Integer> seqBits = new ArrayList<>();
        vertexStack.push(startNode);        // starting with the chosen node
        edgeStack.push(-1);     // Adding a dummy edge, so that the stack sizes are maintained
        while (!vertexStack.isEmpty()) {
            // Info: We will only peek not remove until all the outgoing edges of the node are traversed
            int node = vertexStack.peek();      // Peeking the top vertex
            if (edgePtr[node] < g.get(node).size()) {
                // Micro-optimisation: Since the edges are 0 or 1, just incrementing the value does the trick
                Node e = g.get(node).get(edgePtr[node]++);
                // Pushing the next node and the edge weight (0 or 1)
                vertexStack.push(e.end);
                edgeStack.push(e.weight);
            } else {        // When all outgoing edges traversed, pop the node and weight
                vertexStack.pop();
                int weight = edgeStack.pop();
                // Info: this skips the dummy edge of -1
                if (!vertexStack.isEmpty()) {
                    seqBits.add(weight);
                }
            }
        }
        Collections.reverse(seqBits);       // Reverse the sequence to get the eulerian walk
        final StringBuilder output = new StringBuilder();
        if (startNode == 0)     // We started from node 0, so we take the first n-1 characters
            output.append("0".repeat(n - 1));
        else {      // otherwise take the n-1 start characters of the start node (just for safety doesn't happen in this problem)
            String s = Integer.toBinaryString(startNode);
            output.append(s.substring(0, s.length()-1));
        }
        for (int bit : seqBits)     // Info: Append the sequence bits to complete the superstring
            output.append(bit);
        return new StringBuilder().append(output);
    }
}
