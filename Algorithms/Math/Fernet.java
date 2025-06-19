import java.util.Scanner;

public class Fernet {

    public static long powerflt(long a, long b, final long MOD) {
        return exp(a, b % (MOD - 1), MOD);
    }

    public static long inverseflt(long a, long MOD) {
        return exp(a, MOD - 2, MOD);
    }

    private static final java.util.Random RANDOM = new java.util.Random();

    public static boolean fermatPrimalityTest(long a, int iterations) {
        if (a <= 1 || a == 4)
            return false;
        if (a <= 3)
            return true;
        for (int i = 0; i < iterations; i++) {
            long rand = 2 + (RANDOM.nextLong() % (a - 3)); // 2 <= rand <= a-2
            if (exp(rand, a - 1, a) != 1)
                return false;
        }
        return true;
    }

    public static long exp(long a, long b, final long m) {
        long res = 1L;
        while (b > 0) {
            if ((b & 1) == 1)
                res = (res * a) % m;
            a = (a * a) % m;
            b >>= 1;
        }
        return res;
    }

    public static final long MOD = 1_000_000_007;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Power (a, b) : " + powerflt(sc.nextInt(), sc.nextInt(), MOD));
            System.out.println("Inverse (a) : " + inverseflt(sc.nextInt(), MOD));
            System.out.println("Primality Test : " +fermatPrimalityTest(sc.nextInt(), sc.nextInt()));
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
