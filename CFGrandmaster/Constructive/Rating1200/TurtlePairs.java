// https://codeforces.com/problemset/problem/2003/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class TurtlePairs {
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

    public static void main(String[] args) throws IOException {
        Thread cons1200 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Turtle-and-good-pairs", 1 << 26);
        cons1200.start();
        try {
            cons1200.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0)
            output.append(solve(fr.nextInt(), fr.next())).append("\n");
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static String solve(final int n, final String s) {
        // 1. Count frequencies
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        // 2. If only one distinct character, return original
        int distinct = 0;
        for (int f : freq)
            if (f > 0)
                distinct++;
        if (distinct == 1) {
            return s;
        }
        // 3. Identify most and second-most frequent characters
        // We’ll keep a list of (char, remainingCount) and sort it dynamically.
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> b[1] - a[1] // descending by count
        );
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                pq.offer(new int[] { i, freq[i] });
            }
        }
        int[] top = pq.poll();
        int mostChar = top[0], mostCnt = top[1];
        int[] second = pq.poll();
        int secondChar = second[0], secondCnt = second[1];

        // 4. Start by placing (mostCnt - secondCnt) of the most frequent
        StringBuilder sb = new StringBuilder(n);
        int headCount = mostCnt - secondCnt;
        for (int i = 0; i < headCount; i++) {
            sb.append((char) ('a' + mostChar));
        }
        // reduce the most frequent down to equal the second
        mostCnt = secondCnt;

        // 5. Push back the reduced counts into the PQ
        if (mostCnt > 0)
            pq.offer(new int[] { mostChar, mostCnt });
        if (secondCnt > 0)
            pq.offer(new int[] { secondChar, secondCnt });

        // 6. For the rest, always pull the two highest counts and alternate
        while (pq.size() > 1) {
            int[] first = pq.poll();
            int[] secondItem = pq.poll();
            // append one of each
            sb.append((char) ('a' + first[0]));
            sb.append((char) ('a' + secondItem[0]));
            // decrement and re-offer if still positive
            if (--first[1] > 0)
                pq.offer(first);
            if (--secondItem[1] > 0)
                pq.offer(secondItem);
        }
        // 7. If one character remains, append it
        if (!pq.isEmpty()) {
            int[] last = pq.poll();
            sb.append((char) ('a' + last[0]));
            // if there were more than one, they’d all go here; but since
            // headCount took care of the “bulk” up front, there’s at most one.
        }

        return sb.toString();
    }

}