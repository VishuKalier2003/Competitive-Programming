import java.util.*;

public class Kuriyama {
    public record Stones(int n, long[] nums, List<int[]> queries) {}
    public static void main(String[] args) {
        Stones stone;
        input: {
            try (Scanner sc = new Scanner(System.in)) {
                int n = sc.nextInt();
                stone = new Stones(n, new long[n + 1], new ArrayList<>());
                for (int i = 1; i <= n; i++)
                    stone.nums[i] = sc.nextLong();
                int q = sc.nextInt();
                for (int i = 0; i < q; i++) {
                    int type = sc.nextInt(), l = sc.nextInt(), r = sc.nextInt();
                    stone.queries.add(new int[]{type, l, r});
                }
            }
            break input;
        } output: {
            queryResult(stone.nums, stone.queries);
            break output;
        }
    }

    public static void queryResult(long nums[], List<int[]> queries) {
        int n = nums.length - 1;        // Since we use 1-based indexing
        long type1[] = new long[n + 1], type2[] = new long[n + 1];
        for (int i = 1; i <= n; i++)                // Compute prefix sum for original array
            type1[i] = type1[i - 1] + nums[i];
        long sortedNums[] = Arrays.copyOf(nums, nums.length);   // Create a sorted copy and compute prefix sum
        Arrays.sort(sortedNums, 1, sortedNums.length);
        for (int i = 1; i <= n; i++)
            type2[i] = type2[i - 1] + sortedNums[i];
        StringBuilder sb = new StringBuilder();
        for (int[] query : queries) {
            if (query[0] == 1)
                sb.append(typeQuery(query[1], query[2], type1)).append("\n");
            else
                sb.append(typeQuery(query[1], query[2], type2)).append("\n");
        }
        System.out.print(sb); // Efficient output
    }

    public static long typeQuery(int l, int r, long nums[]) {
        return nums[r] - nums[l - 1]; // Corrected prefix sum subtraction
    }
}
