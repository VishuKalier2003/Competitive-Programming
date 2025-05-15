import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CardGame {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            long bob = 0, alice = 0;
            final String s = fast.next();
            for(int i = 1; i <= n; i++) {
                char card = s.charAt(i-1);
                if(card == 'A')
                    alice |= 1L << i;
                else
                    bob |= 1L << i;
            }
            output.append(solve(n, alice, bob)).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, final long alice, final long bob) {
        if(cardDeck(bob, n) && cardDeck(bob, 1))
            return new StringBuilder().append("Bob");
        if(cardDeck(bob, n-1) && cardDeck(bob, 1))
            return new StringBuilder().append("Bob");
        if(cardDeck(bob, n) && (bob ^ 1L << n) != 0)
            return new StringBuilder().append("Bob");
        return new StringBuilder().append("Alice");
    }

    public static boolean cardDeck(long deck, final int index) {
        for(int i = 0; i < index; i++, deck >>= 1);
        return (deck & 1) == 1;
    }
}
