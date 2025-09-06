import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class P9ManhattanSum {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        long[] x = new long[n], y = new long[n];
        
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            x[i] = Long.parseLong(st.nextToken());
            y[i] = Long.parseLong(st.nextToken());
        }
        
        BigInteger result = solve(x).add(solve(y));
        System.out.println(result);
    }

    private static BigInteger solve(long[] arr) {
        Arrays.sort(arr);
        BigInteger sum = BigInteger.ZERO;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            BigInteger coeff = BigInteger.valueOf(2L * i - n + 1);
            BigInteger val = BigInteger.valueOf(arr[i]);
            sum = sum.add(coeff.multiply(val));
        }
        return sum;
    }
}
