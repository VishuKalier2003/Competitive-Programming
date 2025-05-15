import java.util.*;

public class SimilarPairs {
    public record Pair(int n, int[] nums) {}
    public static void main(String[] args) {
        Pair pairs[];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                pairs = new Pair[sc.nextInt()];
                for(int i = 0; i < pairs.length; i++) {
                    int n = sc.nextInt();
                    pairs[i] = new Pair(n, new int[n]);
                    for(int j = 0; j < pairs[i].nums.length; j++)
                        pairs[i].nums[j] = sc.nextInt();
                }
            }
            break input;
        } output: {
            Arrays.stream(pairs).map(Pair::nums).map(SimilarPairs::findSimilarPairs).forEach(System.out::println);
            break output;
        }
    }

    public static String findSimilarPairs(int nums[]) {
        int evenCount = (int)Arrays.stream(nums).filter(num -> num % 2 == 0).count();
        int oddCount = (int)Arrays.stream(nums).filter(num -> num % 2 != 0).count();
        if(evenCount % 2 == 0 && oddCount % 2 == 0)
            return "YES";
        Arrays.sort(nums);
        boolean adjacentFlag = false;
        for(int i = 0; i < nums.length-1; i++)
            if(nums[i+1] - nums[i] == 1) {
                adjacentFlag = true; break;
            }
        return adjacentFlag ? "YES" : "NO";
    }
}
