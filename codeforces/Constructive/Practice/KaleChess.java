import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class KaleChess {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        String chess[] = new String[8];
        int board[][] = new int[8][8];
        for(int i = 0; i < 8; i++)
            chess[i] = fast.next();
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                board[i][j] = chess[i].charAt(j) == 'B' ? 1 : 0;
        System.out.println(minPaint(board));
    }

    public static int minPaint(int chess[][]) {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            int blackCells = 0;
            for(int j = 0; j < 8; j++)
                blackCells += chess[i][j] == 1 ? 1 : 0;
            if(blackCells == 8)
                count++;
        }
        if(count == 8)
            return count;
        for(int j = 0; j < 8; j++) {
            int blackCells = 0;
            for(int i = 0; i < 8; i++)
                blackCells += chess[i][j] == 1 ? 1 : 0;
            if(blackCells == 8)
                count++;
        }
        return count;
    }
}
