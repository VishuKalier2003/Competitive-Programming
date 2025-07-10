import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Photographer {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
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

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new OutputStreamWriter(System.out));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static void main(String[] args) {
        Thread greedy1400 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Photographer", 1 << 26);
        greedy1400.start();
        try {
            greedy1400.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static class Node {
        long sum;
        int index;

        public Node(long s, int i) {
            this.sum = s; this.index = i;
        }

        public long getSum() {return this.sum;}
        public int getIndex() {return this.index;}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), k = fr.nextInt();
        long a = fr.nextInt(), b = fr.nextInt();
        Node nums[] = new Node[n];
        for(int i = 0; i < n; i++)
            nums[i] = new Node((a * fr.nextInt()) + (b * fr.nextInt()), i+1);
        fw.add(solve(n, k, nums));
        fw.flushMemory();
    }

    public static String solve(final int n, int k, final Node nums[]) {
        Arrays.sort(nums, (a, b) -> Long.compare(a.getSum(), b.getSum()));
        long time = 0;
        List<Integer> indices = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            time += nums[i].getSum();
            if(time <= k)
                indices.add(nums[i].getIndex());
            else
                break;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(indices.size()).append("\n");
        for(int idx : indices)
            sb.append(idx).append(" ");
        return sb.toString();
    }
}