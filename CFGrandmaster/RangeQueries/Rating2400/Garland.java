import java.io.*;
import java.util.*;

public class Garland {
    static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do x = x * 10 + (c - '0'); while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ') if (c < 0) return null;
            StringBuilder sb = new StringBuilder();
            do sb.append((char) c); while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    static class GarlandInfo {
        int len;
        long[] sums; // prefix sums
        int status; // 1 = on, 0 = off
        int startX, startY, endX, endY;
        int topX, bottomX, leftY, rightY;
        List<Integer> curr; // temporary list of indices for border hits

        public GarlandInfo(int len) {
            this.len = len;
            this.sums = new long[len + 1];
            this.status = 1;
            this.curr = new ArrayList<>();
        }
    }

    public static void main(String[] args) throws IOException {
        Thread t = new Thread(null, Garland::solve, "Garland", 1 << 26);
        t.start();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    static void solve() {
        try {
            FastReader fr = new FastReader();
            PrintWriter out = new PrintWriter(System.out);

            int n = fr.nextInt(), m = fr.nextInt(), k = fr.nextInt();

            // Mapping from cell to garlandId and index in garland
            int[][][] arr = new int[n + 2][m + 2][2];

            GarlandInfo[] garlands = new GarlandInfo[k + 1];

            for (int g = 1; g <= k; g++) {
                int len = fr.nextInt();
                GarlandInfo gi = new GarlandInfo(len);
                garlands[g] = gi;

                for (int i = 1; i <= len; i++) {
                    int x = fr.nextInt(), y = fr.nextInt(), w = fr.nextInt();
                    arr[x][y][0] = g;
                    arr[x][y][1] = i;
                    gi.sums[i] = gi.sums[i - 1] + w;

                    if (i == 1) {
                        gi.startX = gi.topX = gi.bottomX = x;
                        gi.startY = gi.leftY = gi.rightY = y;
                    }
                    if (i == len) {
                        gi.endX = x;
                        gi.endY = y;
                    }
                    gi.topX = Math.min(gi.topX, x);
                    gi.bottomX = Math.max(gi.bottomX, x);
                    gi.leftY = Math.min(gi.leftY, y);
                    gi.rightY = Math.max(gi.rightY, y);
                }
            }

            int q = fr.nextInt();
            while (q-- > 0) {
                String cmd = fr.next();
                if (cmd.equals("SWITCH")) {
                    int id = fr.nextInt();
                    garlands[id].status ^= 1; // toggle
                } else { // ASK query
                    int x1 = fr.nextInt(), y1 = fr.nextInt(), x2 = fr.nextInt(), y2 = fr.nextInt();
                    long cnt = 0;

                    // Scan border cells and collect indices of garlands ON
                    for (int g = 1; g <= k; g++) {
                        GarlandInfo gi = garlands[g];
                        if (gi.status == 0) continue;

                        // Fully inside query rectangle
                        if (gi.topX >= x1 && gi.bottomX <= x2 && gi.leftY >= y1 && gi.rightY <= y2) {
                            cnt += gi.sums[gi.len];
                            continue;
                        }

                        // Partial overlap → collect border hits
                        gi.curr.clear();

                        // Top and bottom rows
                        if (x1 > 1)
                            for (int y = y1; y <= y2; y++)
                                if (arr[x1][y][0] == arr[x1 - 1][y][0] && arr[x1][y][0] == g &&
                                        Math.abs(arr[x1][y][1] - arr[x1 - 1][y][1]) == 1)
                                    gi.curr.add(arr[x1][y][1]);

                        if (x2 < n)
                            for (int y = y1; y <= y2; y++)
                                if (arr[x2][y][0] == arr[x2 + 1][y][0] && arr[x2][y][0] == g &&
                                        Math.abs(arr[x2][y][1] - arr[x2 + 1][y][1]) == 1)
                                    gi.curr.add(arr[x2][y][1]);

                        // Left and right columns
                        if (y1 > 1)
                            for (int x = x1; x <= x2; x++)
                                if (arr[x][y1][0] == arr[x][y1 - 1][0] && arr[x][y1][0] == g &&
                                        Math.abs(arr[x][y1][1] - arr[x][y1 - 1][1]) == 1)
                                    gi.curr.add(arr[x][y1][1]);

                        if (y2 < m)
                            for (int x = x1; x <= x2; x++)
                                if (arr[x][y2][0] == arr[x][y2 + 1][0] && arr[x][y2][0] == g &&
                                        Math.abs(arr[x][y2][1] - arr[x][y2 + 1][1]) == 1)
                                    gi.curr.add(arr[x][y2][1]);

                        Collections.sort(gi.curr);

                        // Walk through collected indices and toggle segments
                        boolean inside = gi.startX >= x1 && gi.startX <= x2 && gi.startY >= y1 && gi.startY <= y2;
                        int prev = 1;
                        for (int c : gi.curr) {
                            if (inside)
                                cnt += gi.sums[c] - gi.sums[prev - 1];
                            prev = c;
                            inside ^= true;
                        }

                        if (!gi.curr.isEmpty()) {
                            // check last segment from last border to end
                            if ((gi.endX >= x1 && gi.endX <= x2 && gi.endY >= y1 && gi.endY <= y2) && inside)
                                cnt += gi.sums[gi.len] - gi.sums[prev - 1];
                        }
                    }

                    out.println(cnt);
                }
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
