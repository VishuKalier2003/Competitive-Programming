import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PrefixSumQueries {
    public static class FastReader {        // FastReader
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

    public static class FastWriter {        // Fast Writer class
        BufferedWriter writer;

        public FastWriter() {this.writer = new BufferedWriter(new OutputStreamWriter(System.out));}

        public void write(String s) throws IOException {
            this.writer.write(s);
        }

        public void close() throws IOException {this.writer.close();}
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        List<int[]> queries = new ArrayList<>();        // 1 based indexing
        for(int i = 0; i < k; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.nextInt()});
        solve(n, k, nums, queries);
    }

    public static void solve(final int n, final int q, final long nums[], final List<int[]> queries) throws IOException {
        FastWriter writer = new FastWriter();
        SegmentTree segmentTree = new SegmentTree(nums, n);
        for(int query[] : queries) {
            if(query[0] == 1)
                segmentTree.update(query[1], query[2]);
            else
                writer.write(segmentTree.query(query[1], query[2])+"\n");
        }
        writer.close();
    }

    public static class SegmentTree {
        public long maxPrefix[], sum[];
        public int N;

        public SegmentTree(long nums[], int n) {
            N = 1;
            while(N < n)
                N <<= 1;
            this.maxPrefix = new long[2*N];
            this.sum = new long[2*N];
            for(int i = 0; i < n; i++) {
                this.maxPrefix[N+i] = Math.max(0, nums[i]);
                this.sum[N+i] = nums[i];
            }
            for(int j = N-1; j > 0; j--)
                merge(j);
        }

        public void update(int query, int delta) {
            int index = N+query-1;
            this.sum[index] = delta;
            this.maxPrefix[index] = Math.max(0, delta);
            index >>= 1;
            while(index > 0) {
                merge(index);
                index >>= 1;
            }
            return;
        }

        public long query(int left, int right) {
            long leftSum = 0, leftPrefix  = 0;
            long rightSum = 0, rightPrefix = 0;
            int L = N + left  - 1;
            int R = N + right - 1;
            while (L <= R) {
                if ((L & 1) == 1) {
                    long s = sum[L], p = maxPrefix[L];
                    leftPrefix = Math.max(leftPrefix, leftSum + p);
                    leftSum = leftSum + s;
                    L++;
                }
                if ((R & 1) == 0) {
                    long s = sum[R], p = maxPrefix[R];
                    // ← CORRECT merge for right side
                    rightPrefix = Math.max(p, s + rightPrefix);
                    rightSum = s + rightSum;
                    R--;
                }
                L >>= 1;
                R >>= 1;
            }
            return Math.max(leftPrefix, leftSum + rightPrefix);
        }

        public void merge(int index) {
            long leftSum = this.sum[2*index], rightSum = this.sum[2*index+1];
            long rightPrefix = this.maxPrefix[2*index+1], leftPrefix = this.maxPrefix[2*index];
            this.sum[index] = leftSum + rightSum;
            this.maxPrefix[index] = Math.max(leftPrefix, leftSum + rightPrefix);
            return;
        }
    }
}
