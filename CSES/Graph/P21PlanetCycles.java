import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P21PlanetCycles {
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

    // Micro-optimisation: creating new thread, not hitting MLE (Memory Limit Exceeded)
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Planet-Cycles",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing call time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt();
        g = new ArrayList<>();
        cycles = new int[n + 1];
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for (int i = 1; i <= n; i++)
            g.get(i).add(fr.nextInt());
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    private static List<List<Integer>> g;
    private static int cycles[];
    private static Status status[];

    // Micro-optimisation: creating three status states
    private enum Status {UNVISITED, STACKED, DONE}

    public static StringBuilder solve(final int n) {
        status = new Status[n+1];
        boolean vis[] = new boolean[n+1];
        Arrays.fill(status, Status.UNVISITED);      // Initially unvisited
        for(int i = 1; i <= n; i++)
            if(status[i] == Status.UNVISITED)       // Incase the node is unvisited yet (disconnected graph)
                recurse(i, vis);
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= n; i++)
            output.append(cycles[i]).append(" ");
        return new StringBuilder().append(output);
    }

    public static void recurse(int start, boolean vis[]) {
        List<Integer> path = new ArrayList<>();
        // Info: The stack or list storing the entire path may not be a cycle (cycle can start from between)
        int node = start;       // start node saved
        while(status[node] == Status.UNVISITED) {       // Traversal of cycle
            status[node] = Status.STACKED;
            path.add(node);
            node = g.get(node).get(0);
        }
        if(status[node] == Status.STACKED) {        // If node is in stack (currently being processed)
            int index = path.indexOf(node);         // Accessing the start index, to find the cycle size
            int cycleSize = path.size() - index;
            for(int j = index; j < path.size(); j++) {
                int v = path.get(j);
                cycles[v] = cycleSize;
                // Info: DONE is used when the node is never to be processed again
                status[v] = Status.DONE;        // marking the state as done
            }
            path = path.subList(0, index);
        }
        // Info: Processing the path that is not cycle
        for(int k = path.size()-1; k >= 0; k--) {
            int v = path.get(k);
            int next = g.get(v).get(0);
            cycles[v] = cycles[next] + 1;
            status[v] = Status.DONE;
        }
        status[start] = Status.DONE;    // Marking status of the start node as well
    }
}
