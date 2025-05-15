import java.util.*;
import java.util.stream.Collectors;

public class EqualWithModulus {
    public record Equality(long[] nums) {
        public long[] getNums() {
            return nums;
        }
    }

    public static void main(String[] args) {
        Equality equalities[];
        input: {
            try (Scanner sc = new Scanner(System.in)) {
                equalities = new Equality[sc.nextInt()];
                for (int i = 0; i < equalities.length; i++) {
                    equalities[i] = new Equality(new long[sc.nextInt()]);
                    for (int j = 0; j < equalities[i].nums.length; j++)
                        equalities[i].nums[j] = sc.nextLong();
                }
            }
            break input;
        }
        output: {
            Arrays.stream(equalities).map(Equality::getNums).map(EqualWithModulus::findOptimalK).forEach(System.out::println);
            break output;
        }
    }

    public static long findOptimalK(long nums[]) {
        for (int j = 1; j <= 64; j++) {
            final int bitLength = j;        // Create a final variable for `j`
            Set<Long> modResults = Arrays.stream(nums).map(num -> num & ((1L << bitLength) - 1)) // Use the final variable
                    .boxed().collect(Collectors.toSet());
            if (modResults.size() == 2) {
                return 1L << bitLength; // Return 2^j
            }
        }
        return 0L;
    }

}
