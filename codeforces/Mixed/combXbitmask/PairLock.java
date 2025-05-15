import java.util.*;

public class PairLock {
    public static void main(String[] args) {
        int nums[];
        input: {
            try (Scanner sc = new Scanner(System.in)) {
                nums = new int[sc.nextInt()];
                for (int i = 0; i < nums.length; i++)
                    nums[i] = sc.nextInt();
            }
            break input;
        }
        output: {
            System.out.println(canOpenLock(nums) ? "YES" : "NO");
            break output;
        }
    }

    public static boolean canOpenLock(int nums[]) {
        return helper(0, nums, 0, new HashSet<>());
    }

    public static boolean helper(int i, int nums[], int degree, Set<String> dp) {
        degree = (degree % 360 + 360) % 360; // Normalize degree to [0, 360)
        if (i >= nums.length)
            return degree == 0;
        String state = i + "," + degree;    // Create a unique state for (i, degree)
        if (dp.contains(state))     // If the state is already visited, skip it
            return false;
        dp.add(state);      // Mark the state as visited
        // Recurse with clockwise and anti-clockwise rotations
        boolean clockWise = helper(i + 1, nums, degree + nums[i], dp);
        boolean antiClockWise = helper(i + 1, nums, degree - nums[i], dp);
        return clockWise || antiClockWise;
    }
}
