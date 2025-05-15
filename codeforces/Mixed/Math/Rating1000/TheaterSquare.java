import java.util.*;
import java.io.*;

public class TheaterSquare {
    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException error) {error.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public long nextLong() {return Long.parseLong(next());}
    }
    public static void main(String args[]) {
        long input[][];
        input: {
            FastReader fastReader = new FastReader();
            input = new long[][]{{fastReader.nextLong(), fastReader.nextLong(), fastReader.nextLong()}};
            break input;
        } output: {
            Arrays.stream(input).mapToLong(TheaterSquare::countFlagTiles).forEach(System.out::println);
            break output;
        }
    }

    public static long countFlagTiles(long data[]) {
        // Imp : formula to add 1 to the quotient if dividend is not completely divisible by divider -> (dividend+divisor-1)/divisor 
        return ((data[0]+data[2]-1)/data[2]) * ((data[1]+data[2]-1)/data[2]);
    }
}
