// Ques - https://codeforces.com/problemset/problem/1367/C

// Note- Filling values in an array from multiple positions simultaneously (marking simultaneous overlapping ranges) in O(n)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Problem1367C {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            final String s = fast.next();
            output.append(solve(n, k, s)).append("\n");
        }
        System.out.print(output);
    }

    public static class Node {      // Node class for Queue defined
        int index, range;

        public Node(int index, int range) {
            this.index = index; this.range = range;
        }
    }

    public static int solve(final int n, final int k, final String s) {
        return occupySegments(markingRanges(n, k, s), k+1);
    }

    public static boolean[] markingRanges(final int n, final int k, final String s) {
        boolean full[] = new boolean[n];
        Queue<Node> queue = new LinkedList<>();
        for(int i = 0; i < n; i++)
            if(s.charAt(i) == '1') {        // Start nodes as all 1s
                queue.add(new Node(i, k));
                full[i] = true;
            }
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                Node node = queue.poll();
                // Expand to right
                if(node.index < n-1 && node.range > 0 && !full[node.index+1]) {
                    full[node.index+1] = true;
                    queue.add(new Node(node.index+1, node.range-1));
                }   // Expand to left
                if(node.index > 0 && node.range > 0 && !full[node.index-1]) {
                    full[node.index-1] = true;
                    queue.add(new Node(node.index-1, node.range-1));
                }
            }
        }
        return full;
    }

    public static int occupySegments(final boolean full[], final int k) {
        int count = 0;
        for(int i = 0; i < full.length; i++) {
            if(!full[i]) {
                count++;    // Fill the empty seat and jump k steps ahead to maintain distance
                i += k-1;
            }
        }
        return count;       // return the count
    }
}
