// https://codeforces.com/problemset/problem/2111/D?csrf_token=877f5882d8467fb0705a55fa36748aaa

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Scedhule {
    public static class FastReader {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xFF;
        }

        public int readInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') {
                if (c < 0) return -1;
            }
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while ((c = read()) <= ' ') {
                if (c < 0) return null;
            }
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    public static class FastWriter {
        private final PrintWriter pw;
        private final StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void append(CharSequence cs) {
            sb.append(cs);
        }

        public void println() {
            sb.append('\n');
        }

        public void flush() {
            pw.write(sb.toString());
            pw.flush();
            sb.setLength(0);
        }
    }

    private static class Pair {
        int floor;
        String id;
        Pair(int f, String i) { floor = f; id = i; }
    }

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        int t = fr.readInt();
        while (t-- > 0) {
            int n = fr.readInt();
            int m = fr.readInt();

            String[] rooms = new String[m];
            for (int i = 0; i < m; i++) {
                rooms[i] = fr.readString();
            }

            fw.append(solve(n, m, rooms));
            // No extra blank line between test cases
        }

        fw.flush();
    }

    public static String solve(int n, int m, String[] classrooms) {
        // Build (floor, id) pairs and sort by floor ascending
        Pair[] arr = new Pair[m];
        for (int i = 0; i < m; i++) {
            String id = classrooms[i];
            int floor = Integer.parseInt(id.substring(0, id.length() - 2));
            arr[i] = new Pair(floor, id);
        }
        Arrays.sort(arr, Comparator.comparingInt(p -> p.floor));

        int half = n / 2;
        // Extract the lowest-half and highest-half room IDs
        String[] low  = new String[half];
        String[] high = new String[half];
        for (int i = 0; i < half; i++) {
            low[i]  = arr[i].id;
            high[i] = arr[m - 1 - i].id;
        }

        // If n is odd, reserve the next-lowest and next-highest for the "extra" group
        String extraLow = null, extraHigh = null;
        if ((n & 1) == 1) {
            extraLow  = arr[half].id;
            extraHigh = arr[m - 1 - half].id;
        }

        StringBuilder out = new StringBuilder(6 * n * 12);
        for (int i = 0; i < n; i++) {
            if ((n & 1) == 1 && i == half) {
                // The extra middle group for odd n
                for (int j = 0; j < 6; j++) {
                    out.append((j % 2 == 0) ? extraLow : extraHigh);
                    if (j < 5) out.append(' ');
                }
                out.append('\n');
            }
            else if (i < half) {
                // First half: start low → high
                String a = low[i], b = high[i];
                for (int j = 0; j < 6; j++) {
                    out.append((j % 2 == 0) ? a : b);
                    if (j < 5) out.append(' ');
                }
                out.append('\n');
            }
            else {
                // Second half: start high → low
                int idx = i - half - (n & 1);
                String a = high[idx], b = low[idx];
                for (int j = 0; j < 6; j++) {
                    out.append((j % 2 == 0) ? a : b);
                    if (j < 5) out.append(' ');
                }
                out.append('\n');
            }
        }

        return out.toString();
    }
}
