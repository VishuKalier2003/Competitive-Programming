import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Anton {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single
        // System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long readLong() throws IOException {
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread math1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "anton-and-the-currency-we-all-know", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(solve(fr.readString().toCharArray())).append("\n");
        wr.write(output.toString()); // printwriter to print output
        wr.flush();
    }

    public static String solve(char[] cArray) {
        int n = cArray.length;
        int lastDigit = cArray[n - 1] - '0';
        int swapIndex = -1;

        for (int i = 0; i < n - 1; i++) {
            int digit = cArray[i] - '0';

            // Must be even
            if (digit % 2 == 0) {
                // Greedy: try to find a smaller even digit than the last digit to increase
                // number
                if (digit < lastDigit) {
                    // Optimal: swap immediately
                    swap(cArray, i, n - 1);
                    return new String(cArray);
                }
                // Fallback candidate
                swapIndex = i;
            }
        }

        // If found any even digit, swap the last one to make it even (but not optimal)
        if (swapIndex != -1) {
            swap(cArray, swapIndex, n - 1);
            return new String(cArray);
        }

        // No even digit found to make last digit even
        return "-1";
    }

    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
