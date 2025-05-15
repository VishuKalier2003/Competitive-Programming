import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Cowntagion {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
        final int n = fast.nextInt();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
        System.out.print(solve());
    }

    public static int solve() {
        return dfs(1, -1);
    }

    public static int dfs(int root, int parent) {
        int ans = 0;        // ans for each node to be returned
        int cows = tree.get(root).size();
        if(parent == -1)
            cows++;
        int infectedCows = 1, days = 0;
        while(infectedCows < cows) {
            days++;
            infectedCows *= 2;      // First we will infect the farm to the point that one cow can be send to all paths
        }
        ans += days;        // add the number of double operations
        for(int farm : tree.get(root))
            if(farm != parent)
                ans += dfs(farm, root) + 1;     // add 1 for each path, since only one cow will travel at one instance
        return ans;
    }
}
