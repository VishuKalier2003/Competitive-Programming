package Rating1200;

import java.util.*;
import java.io.*;

public class SumOfCubes {
    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try{
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }
    public static void main(String[] args) {
        long nums[];
        input: {
            FastReader fastReader = new FastReader();
            nums = new long[fastReader.nextInt()];
            for(int i = 0; i < nums.length; i++)
                nums[i] = fastReader.nextLong();
            break input;
        } output: {
            fillCubes();
            Arrays.stream(nums).mapToObj(SumOfCubes::sumOfCubesCheck).forEach(System.out::println);
            break output;
        }
    }

    public static final long[] cubes = new long[10000];

    public static void fillCubes() {
        for(int i = 0; i < 10000; i++)
            cubes[i] = (long)Math.pow(i+1, 3);
    }

    public static String sumOfCubesCheck(long num) {
        Set<Long> complement = new HashSet<>();
        for(long cube : cubes)
            complement.add(num-cube);
        for(long cube : cubes)
            if(complement.contains(cube))
                return "YES";
        return "NO";
    }
}
