// https://codeforces.com/problemset/problem/981/C

// Imp- When there are two leaf nodes there is exactly one path as a straight line
import java.io.*;
import java.util.*;

public class Useful {
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
                    e.printStackTrace();
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

    public static List<List<Integer>> tree;
    public static int root;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int[] degree = new int[n + 1];
        tree = new ArrayList<>();
        root = -1;
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 1; i <= n - 1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
            degree[n1]++;
            degree[n2]++;
        }
        solve(n, degree);
    }

    public static void solve(final int n, final int[] degree) throws IOException {
        StringBuilder output = new StringBuilder();
        List<Integer> leaves = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (degree[i] == 1) {
                leaves.add(i);
            }
        }

        // Special case: linear tree
        if (leaves.size() == 2) {
            output.append("Yes\n1\n").append(leaves.get(0)).append(" ").append(leaves.get(1)).append("\n");
        } else {
            for (int i = 1; i <= n; i++) {
                if (degree[i] >= leaves.size()) {
                    root = i;
                    break;
                }
            }

            if (root == -1) {
                output.append("No");
            } else {
                output.append("Yes\n").append(leaves.size()).append("\n");
                for (int leaf : leaves) {
                    output.append(root).append(" ").append(leaf).append("\n");
                }
            }
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
        writer.close(); // Ensure writer is closed properly
    }
}
