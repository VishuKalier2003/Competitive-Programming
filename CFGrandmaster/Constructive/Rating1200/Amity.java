import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Amity {
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

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Amity-Assessment", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        final String a1 = fr.next(), a2 = fr.next();
        char c1[] = new char[]{a1.charAt(0), a2.charAt(0), a2.charAt(1), a1.charAt(1)};
        final String b1 = fr.next(), b2 = fr.next();
        char c2[] = new char[]{b1.charAt(0), b2.charAt(0), b2.charAt(1), b1.charAt(1)};
        output.append(solve(c1, c2));
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(char c1[], char c2[]) {
        System.out.println("A1 : "+Arrays.toString(c1));
        System.out.println("A2 : "+Arrays.toString(c2));
        if(c1[0] == c2[0] && c1[1] == c2[1] && c1[2] == c2[2] && c1[3] == c2[3])
            return new StringBuilder().append("Yes");
        if(c1[0] == c2[1] && c1[1] == c2[2] && c1[2] == c2[3] && c1[3] == c2[0])
            return new StringBuilder().append("Yes");
        if(c1[0] == c2[2] && c1[1] == c2[3] && c1[2] == c2[0] && c1[3] == c2[1])
            return new StringBuilder().append("Yes");
        if(c1[0] == c2[3] && c1[1] == c2[0] && c1[2] == c2[1] && c1[3] == c2[2])
            return new StringBuilder().append("Yes");
        return new StringBuilder().append("No");
    }
}
