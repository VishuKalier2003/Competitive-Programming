package Stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Imp - T.C. : O(n), S.C. : O(n)

public class Boundaries {
    // Imp - using boundaries for left and right of every index behaves in a similar fashion like a stack
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try{tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }
    public static void main(String[] args) {
        input: {
            break input;
        } output: {
            break output;
        }
    }

    public static long trappingRainWater(int nums[]) {
        int n = nums.length;
        int leftBasin[] = new int[n], rightBasin[] = new int[n];
        leftBasin[0] = nums[0]; rightBasin[n-1] = nums[n-1];
        for(int i = 1; i < n; i++)
            leftBasin[i] = Math.max(leftBasin[i-1], nums[i]);
        for(int j = n-2; j >= 0; j--)
            rightBasin[j] = Math.max(rightBasin[j+1], nums[j]);
        long trapped = 0l;
        for(int i = 0; i < n; i++)
            trapped = trapped + (Math.min(leftBasin[i], rightBasin[i]) - nums[i]);
        return trapped;
    }
}
