import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClosingFarm {
public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {this.buffer = new BufferedReader(new FileReader("closing.in"));}

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() throws IOException {
            this.pw = new PrintWriter(new FileWriter("closing.out"));
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
        }, "Closing-The-Farm",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        g = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        DSU dsu = new DSU(n);
        for(int i = 0; i < m; i++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            g.get(v).add(u);
        }
        int closing[] = new int[n];
        for(int i = 0; i < n; i++)
            closing[i] = fr.nextInt();
        fw.attachOutput(solve(n, dsu, closing));
        fw.printOutput();
    }

    private static List<List<Integer>> g;

    public static StringBuilder solve(final int n, DSU dsu, final int closing[]) {
        final StringBuilder output = new StringBuilder();
        String ans[] = new String[n+1];
        // We assume the problem in reverse such that opening the barn is easier than closing it
        for(int i = n-1; i >= 0; i--) {
            final int close = closing[i];
            dsu.openBarn(close);        // Open the barn
            for(int neighbor : g.get(close)) {
                if(dsu.isOpened(close) && dsu.isOpened(neighbor))
                    dsu.union(close, neighbor);
            } 
            if(dsu.components == 1) // If at the end the components count remain 1, then the graph was given connected
                ans[i] = "YES\n";
            else
                ans[i] = "NO\n";
        }
        output.append((dsu.components == 1) ? "YES\n" : "NO\n");
        for(int i = 1; i < n; i++)
            output.append(ans[i]);
        return output;
    }

    public static class DSU {
        private final int size;
        private int components;
        private final int parent[], rank[];
        private final boolean opened[];

        public DSU(int n) {
            this.size = n + 1;
            this.components = 0;        // Initialise the components as 0
            this.parent = new int[this.size];
            this.rank = new int[this.size];
            this.opened = new boolean[this.size];
            for (int i = 1; i < this.size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public void openBarn(int node) {
            this.opened[node] = true;
            // When a barn is opened, we assume it to be disconnected, hence increase its component size by +1
            components++;   
        }

        public boolean isOpened(int node) {
            return this.opened[node];       // checks the state of the given barn
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                } else {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];
                }
                components--;       // If two disconnected components are merged, decrease the component count by -1
            }
        }
    }
}
