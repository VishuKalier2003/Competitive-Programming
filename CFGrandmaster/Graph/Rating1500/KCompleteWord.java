// Union Find

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class KCompleteWord {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
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

        public int nextInt() throws IOException { // reading int
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

        public long nexLong() throws IOException { // reading long
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

        public String next() throws IOException { // reading string (whitespace exclusive)
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

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
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

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "K-Complete-Word(1332C)",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt(), k = fr.nextInt();
            final String word = fr.next();
            fw.attachOutput(solve(n, k, word));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int k, final String word) {
        int blockCount = n / k;
        long replacements = 0;
        int[] freq = new int[26];
        for (int i = 0; i < k / 2; i++) {
            Arrays.fill(freq, 0);
            int totalChars = 0;
            for (int block = 0; block < blockCount; block++) {
                // Characters at symmetric positions in the block
                char leftChar = word.charAt(block * k + i);
                char rightChar = word.charAt(block * k + (k - 1 - i));
                freq[leftChar - 'a']++;
                freq[rightChar - 'a']++;
                totalChars += 2;
            }
            // Find max frequency in this group
            int maxFreq = 0;
            for (int c = 0; c < 26; c++)
                if (freq[c] > maxFreq)
                    maxFreq = freq[c];
            // Add replacements needed for this pair
            replacements += (totalChars - maxFreq);
        }
        // Handle middle character for odd k
        if (k % 2 == 1) {
            Arrays.fill(freq, 0);
            int totalChars = blockCount;
            int mid = k / 2;
            for (int block = 0; block < blockCount; block++) {
                char c = word.charAt(block * k + mid);
                freq[c - 'a']++;
            }
            int maxFreq = 0;
            for (int c = 0; c < 26; c++)
                if (freq[c] > maxFreq)
                    maxFreq = freq[c];
            replacements += (totalChars - maxFreq);
        }
        return new StringBuilder().append(replacements).append("\n");
    }

}
