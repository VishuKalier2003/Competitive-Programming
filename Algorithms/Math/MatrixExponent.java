package Math;

import java.io.*;
import java.util.*;

// Imp - T.C. : O(log n), S.C : O(n)

public class MatrixExponent {
    // Imp - Used to solve Linear Recurrence DP in logarithmic time, by exponentiating calculations
    public static class FastReader {    // Fast Reader class to read inputs quickly (4x faster than Scanner)
        BufferedReader buffer;
        StringTokenizer tokenizer;

        // Constructor defined for calling Buffer
        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {  // Reads data differently when separated by space
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try{tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException error) {error.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // Convert string to int
        public long nextLong() {return Long.parseLong(next());}     // Convert string to long
    }
    public static void main(String[] args) {
        long n;
        input: {        // Input Segment
            FastReader fastReader = new FastReader();
            n = fastReader.nextLong();
            break input;
        } output: {     // Output Segment
            System.out.println(fib(n));
            break output;
        }
    }

    public static long[][] exponentiation(long mat[][], long n) {
        if(n == 1)      // Imp - Return mat pow 1, as base case when n == 1
            return mat;
        // Imp - For ODD (when 0th bit is 0), hence 2 pow 0, is 0, which makes the number even
        if((n & 1) == 0) {        // If n is even, if LSB is 1, then result is 1, hence odd
            long half[][] = exponentiation(mat, n/2);
            return matMul(half, half); // When even, evaluate exponent by taking half A = (A(n/2))^2
        }
        // When odd performs exponentiation by A = A(A^(n-1))
        else    return matMul(mat, exponentiation(mat, n-1));
    }

    public static long fib(long n) {
        // Define the base cases here
        if(n == 0)  return 0;
        if(n == 1)  return 1;   // Imp - The Transformation Matrix is defined by the recurrence relation
        long TransformationMatrix[][] = new long[][]{{1, 1}, {1, 0}};
        return exponentiation(TransformationMatrix, n-1)[0][0];   // Multiply T with (n-1) to get T(n)
    }

    public static long[][] matMul(long A[][], long B[][]) {
        int m = A.length, n = A[0].length, p = B[0].length; // Matrix variables
        long C[][] = new long[m][p];        // Matrix dimensions are m x p
        // N x N Square Matrix Multiplication
        for(int i = 0; i < m; i++)      // Calls done as m, p and n
            for(int j = 0; j < p; j++)
                for(int k = 0; k < n; k++)
                    // Imp - Matrix Chain Multiplication logic
                    C[i][j] = C[i][j] + (A[i][k]*B[k][j]);
        return C;
    }
}
