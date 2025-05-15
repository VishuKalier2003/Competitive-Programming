// https://codeforces.com/problemset/problem/1352/D

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem1352D {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
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

    final static StringBuilder output = new StringBuilder();

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for(int idx = 0; idx < n; idx++)    // Keep in mind about not using i here since it is assigned as global
                nums[idx] = fast.nextInt();
            solve(n, nums);
            output.append("\n");
        }
        System.out.print(output);
    }

    public static long aliceSum, bobSum;
    public static int i, j;

    public static void solve(final int n, final int nums[]) {
        i = 0; j = n-1;
        long alice = 0, bob = 0, c = 0;
        boolean t = true;
        aliceSum = 0; bobSum = 0;
        while(i <= j) {
            if(t) {
                alice = aliceMoves(nums, bob);
                aliceSum += alice;
            }
            else {
                bob = bobMoves(nums, alice);
                bobSum += bob;
            }
            t ^= true;
            c++;
        }
        output.append(c).append(" ").append(aliceSum).append(" ").append(bobSum);
    }

    public static long aliceMoves(final int nums[], long bob) {
        long sum = 0l;
        while(i <= j && sum <= bob) {
            sum += nums[i];
            i++;
        }
        return sum;
    }

    public static long bobMoves(final int nums[], long alice) {
        long sum = 0l;
        while(i <= j && sum <= alice) {
            sum += nums[j];
            j--;
        }
        return sum;
    }
}
