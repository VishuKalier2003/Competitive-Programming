import java.util.Scanner;

public class Fence {
    public record Plank(int k, int[] nums) {}
    public static void main(String[] args) {
        Plank plank;
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                int n = sc.nextInt();
                plank = new Plank(sc.nextInt(), new int[n]);
                for(int i = 0; i < n; i++)
                    plank.nums[i] = sc.nextInt();
            }
            break input;
        } output: {
            System.out.println(findMinPlankIndex(plank.nums, plank.k));
            break output;
        }
    }

    public static int findMinPlankIndex(int nums[], int k) {
        int sum = 0, idx = 0;
        for(int i = 0; i < k; i++)
            sum += nums[i];
        int min = sum;
        for(int i = k; i < nums.length; i++) {
            sum = sum - nums[i-k] + nums[i];
            if(sum < min) {
                min = sum;
                idx = i-k+1;
            }
        }
        return idx+1;   // For 1-based indexing
    }
}
