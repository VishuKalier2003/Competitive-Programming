import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StaticXor {
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
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fast.nextInt();

        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});

        solve(n, nums, queries);
    }

    public static void solve(final int n, final int nums[], final List<int[]> queries) {
        int[] prefixXor = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixXor[i + 1] = prefixXor[i] ^ nums[i];
        }

        StringBuilder ans = new StringBuilder();
        for (int[] query : queries) {
            int l = query[0] - 1;
            int r = query[1];  // prefix[r] is exclusive
            ans.append(prefixXor[r] ^ prefixXor[l]).append("\n");
        }

        System.out.print(ans);
    }
}
