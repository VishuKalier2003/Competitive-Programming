import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class HoofPaperScissor {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {this.buffer = new BufferedReader(new FileReader("hps.in"));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final PrintWriter printer = new PrintWriter(new FileWriter("hps.out"));
        final int n = fast.nextInt(), k = fast.nextInt();
        char moves[] = new char[n+1];
        for(int i = 0; i < n; i++)
            moves[i+1] = fast.next().charAt(0);
        printer.print(solve(n, k, moves));
        printer.close();
    }

    public static int solve(final int n, final int k, char moves[]) {
        int dp[][][] = new int[n+1][k+2][3];
        int winCount = 0;
        for(int game = 1; game <= n; game++) {
            for(int sw = 1; sw <= k+1; sw++) {
                // Taking all three possibilities
                int winByH = 0;
                if(moves[game] == 'H')      // If current game is 'H'
                    winByH++;
                // Choose max state from previous H, previous game and previous shift (sw) of P and previous game and previous shift (sw) of S
                dp[game][sw][0] = Math.max(dp[game-1][sw][0] + winByH, Math.max(dp[game-1][sw-1][1] + winByH, dp[game-1][sw-1][2] + winByH));
                int winByP = 0;
                if(moves[game] == 'P')
                    winByP++;
                // Choose max state from previous P, previous game and previous shift (sw) of H and previous game and previous shift (sw) of S
                dp[game][sw][1] = Math.max(dp[game-1][sw][1] + winByP, Math.max(dp[game-1][sw-1][0] + winByP, dp[game-1][sw-1][2] + winByP));
                int winByS = 0;
                if(moves[game] == 'S')
                    winByS++;
                // Choose max state from previous S, previous game and previous shift (sw) of H and previous game and previous shift (sw) of P
                dp[game][sw][2] = Math.max(dp[game-1][sw][2] + winByS, Math.max(dp[game-1][sw-1][0] + winByS, dp[game-1][sw-1][1] + winByS));
                // When the last game is reached, we choose the max of all last states, this is done for each k and later for each 3 possibility
                if(game == n)
                    winCount = Math.max(Math.max(winCount, dp[game][sw][0]), Math.max(dp[game][sw][1], dp[game][sw][2]));
            }
        }
        return winCount;
    }
}
