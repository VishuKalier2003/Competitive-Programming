package DynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;

public class LCS    // Longest Common Subsequence (LCS)
{
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
        String text1, text2;
        input: {
            FastReader fastReader = new FastReader();
            text1 = fastReader.next();
            text2 = fastReader.next();
            break input;
        } output: {
            System.out.println(lcs(text1, text2));
            break output;
        }
    }

    public static int dp[][];       // Memory dp defined

    public static int lcs(String text1, String text2) {
        int n1 = text1.length(), n2 = text2.length();
        dp = new int[n1+1][n2+1];
        for(int array[] : dp)
            Arrays.fill(array, -1);
        return lcsHelper(text1, text2, n1, n2);
    }

    public static int lcsHelper(String text1, String text2, int m, int n) {
        if(m == n)
            return 0;
        if(dp[m][n] != -1)
            return dp[m][n];
        if(text1.charAt(m) == text2.charAt(n))
            dp[m][n] = 1+lcsHelper(text1, text2, m-1, n-1);
        else
            dp[m][n] = Math.max(lcsHelper(text1, text2, m-1, n), lcsHelper(text1, text2, m, n-1));
        return dp[m][n];
    }
}
