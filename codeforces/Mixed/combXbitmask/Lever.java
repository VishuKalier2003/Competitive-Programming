import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Lever {
    public record Bit(long[] values) {
    } // Record to create a final class

    public static void main(String[] args) {
        Bit bits[];
        input: { // Input block
            try (Scanner sc = new Scanner(System.in)) {
                bits = new Bit[sc.nextInt()];
                for (int i = 0; i < bits.length; i++) {
                    bits[i] = new Bit(new long[sc.nextInt()]);
                    for (int j = 0; j < bits[i].values.length; j++)
                        bits[i].values[j] = sc.nextLong();
                }
            }
            break input;
        }
        output: { // Streams and mapping the values to the caller function
            Arrays.stream(bits).map(bit -> bit.values).map(values -> countPairs(values)).forEach(System.out::println);
            break output;
        }
    }

    public static long countPairs(long values[]) {
        Map<Integer, List<Long>> msbMap = new HashMap<>();
        for (long value : values) { // Creating map of msbIndex key and count values
            // Finding MSB using in-built function and log base 2 division to find the index (0 based)
            int msbIndex = (int) (Math.log(Long.highestOneBit(value)) / Math.log(2));
            if (!msbMap.containsKey(msbIndex))
                msbMap.put(msbIndex, new ArrayList<Long>());
            msbMap.get(msbIndex).add(value);
        }
        // Streams to streamline filtering, counting pairs for each unique MSB and summing them
        return msbMap.entrySet().stream().map(Map.Entry::getValue).filter(value -> value.size() > 1).mapToLong(list -> {
            return (long) (0.5 * list.size() * (list.size() - 1));
        }).sum();
    }
}
