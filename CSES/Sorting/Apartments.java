import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class Apartments {
    static final int BUFFER_SIZE = 1 << 16;
    static DataInputStream din = new DataInputStream(System.in);
    static byte[] buffer = new byte[BUFFER_SIZE];
    static int bufferPointer = 0, bytesRead = 0;

    // FastReader replacement methods
    static String nextToken() throws IOException {
        byte c;
        // skip whitespace
        do {
            if (bufferPointer == bytesRead) fillBuffer();
            c = buffer[bufferPointer++];
        } while (c <= ' ');
        StringBuilder sb = new StringBuilder();
        // read token
        while (c > ' ') {
            sb.append((char)c);
            if (bufferPointer == bytesRead) fillBuffer();
            c = buffer[bufferPointer++];
        }
        return sb.toString();
    }

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
        int n = nextInt();
        int m = nextInt();
        int k = nextInt();
        int[] people = new int[n];
        int[] apartments = new int[m];
        for (int i = 0; i < n; i++) people[i] = nextInt();
        for (int j = 0; j < m; j++) apartments[j] = nextInt();

        System.out.print(solve(n, m, k, people, apartments));
    }

    public static int solve(final int n, final int m, final int k, int[] people, int[] apartments) {
        Arrays.sort(people);
        Arrays.sort(apartments);
        int i = 0, j = 0, count = 0;
        while (i < n && j < m) {
            if (apartments[j] < people[i] - k) {
                j++;
            } else if (apartments[j] > people[i] + k) {
                i++;
            } else {
                i++;
                j++;
                count++;
            }
        }
        return count;
    }
}
