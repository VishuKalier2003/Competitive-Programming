import java.util.*;

public class SpecialNumbers {
    public record Number(long n, long k) {}

    public static void main(String[] args) {
        Number numbers[];
        input: {
            try (Scanner sc = new Scanner(System.in)) {
                numbers = new Number[sc.nextInt()];
                for (int i = 0; i < numbers.length; i++) {
                    long n = sc.nextLong();
                    numbers[i] = new Number(n, sc.nextLong());
                }
            }
            break input;
        }
        output: {
            Arrays.stream(numbers).map(SpecialNumbers::findKthNumber).forEach(System.out::println);
            break output;
        }
    }

    public static final int MOD = 1_000_000_007;

    public static long findKthNumber(Number num) {
        long n = num.n, k = num.k;
        String binary = Long.toBinaryString(k);     // Convert k to binary
        long number = 0, currentPower = 1;      // Start with n^0 = 1
        for (int i = 0; i < binary.length(); i++) {     // Traverse binary from LSB to MSB
            if (binary.charAt(binary.length() - 1 - i) == '1')
                number = (number + currentPower) % MOD;
            currentPower = (currentPower * n) % MOD;        // Update power (n^i mod MOD)
        }
        return number;
    }
}
