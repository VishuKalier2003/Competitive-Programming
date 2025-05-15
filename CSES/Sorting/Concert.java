import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

public class Concert {
    // Fast input
    static final int BUFFER_SIZE = 1 << 16;
    static DataInputStream din = new DataInputStream(System.in);
    static byte[] buffer = new byte[BUFFER_SIZE];
    static int bufferPointer = 0, bytesRead = 0;

    static int nextInt() throws IOException {
        int result = 0;
        byte c;
        do {
            if (bufferPointer == bytesRead) fillBuffer();
            c = buffer[bufferPointer++];
        } while (c <= ' ');
        boolean neg = (c == '-');
        if (neg) {
            if (bufferPointer == bytesRead) fillBuffer();
            c = buffer[bufferPointer++];
        }
        while (c >= '0' && c <= '9') {
            result = result * 10 + (c - '0');
            if (bufferPointer == bytesRead) fillBuffer();
            c = buffer[bufferPointer++];
        }
        return neg ? -result : result;
    }

    static void fillBuffer() throws IOException {
        bytesRead = din.read(buffer, 0, BUFFER_SIZE);
        if (bytesRead == -1) buffer[0] = -1;
        bufferPointer = 0;
    }

    public static void main(String[] args) throws IOException {
        int n = nextInt(), m = nextInt();
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            int h = nextInt();
            map.put(h, map.getOrDefault(h, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int t = nextInt();
            Map.Entry<Integer, Integer> e = map.floorEntry(t);
            if (e == null) {
                sb.append("-1\n");
            } else {
                int price = e.getKey();
                sb.append(price).append('\n');
                int cnt = e.getValue() - 1;
                if (cnt == 0) map.remove(price);
                else map.put(price, cnt);
            }
        }

        // Fast output
        OutputStream out = System.out;
        out.write(sb.toString().getBytes());
        out.flush();
    }
}
