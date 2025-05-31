import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class ColorTree {
    public static class FastReader { // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        } // Input reader

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
        } // reading int

        public long nextLong() {
            return Long.parseLong(next());
        } // reading long
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        final int t = fast.nextInt();
        // IMP- For a tree that can have n children, use Map with Set to identify and
        // extract the data real quick
        Map<Integer, Set<Integer>> tree = new HashMap<>();
        for (int i = 1; i <= t; i++)
            tree.put(i, new HashSet<>()); // Attach a set with every tree node
        for (int i = 2; i <= t; i++)
            tree.get(fast.nextInt()).add(i);
        int colors[] = new int[t + 1];
        for (int i = 0; i < t; i++)
            colors[i + 1] = fast.nextInt(); // Fill the colors
        System.out.print(coloringTime(t, tree, colors) + 1);
    }

    public static int coloringTime(final int n, Map<Integer, Set<Integer>> tree, int color[]) {
        int colorCount = 0;
        for (int i = 1; i <= n; i++)
            // IMP For every node starting from 1, count the adjacent nodes colored
            // differently, that means we need to color them again, and they do not follow
            // the parent color
            colorCount += childrenColor(i, color[i], tree, color);
        return colorCount;
    }

    public static int childrenColor(final int source, final int sourceColor, Map<Integer, Set<Integer>> tree,
            int color[]) {
        int count = 0;
        for (int child : tree.get(source))
            if (color[child] != sourceColor)
                count++; // Count adjacent nodes of different color
        return count;
    }
}
