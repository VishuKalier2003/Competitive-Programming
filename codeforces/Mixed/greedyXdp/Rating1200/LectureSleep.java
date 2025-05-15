import java.util.Scanner;

public class LectureSleep {
    public static void main(String[] args) {
        int n, k;
        int nums[], sleep[];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                n = sc.nextInt(); k = sc.nextInt();
                nums = new int[n]; sleep = new int[n];
                for(int i = 0; i < n; i++)
                    nums[i] = sc.nextInt();
                for(int i = 0; i < n; i++)
                    sleep[i] = sc.nextInt();
            }
            break input;
        } output: {
            System.out.println(totalTheoremsWritten(n, k, nums, sleep));
            break output;
        }
    }

    public static long totalTheoremsWritten(int n, int k, int nums[], int sleep[]) {
        long activeReads = sleep[0] == 1 ? nums[0] : 0;
        long prefix[] = new long[n];
        prefix[0] = sleep[0] == 1 ? 0 : nums[0];
        for(int i = 1; i < n; i++) {
            prefix[i] = sleep[i] == 0 ? prefix[i-1]+nums[i] : prefix[i-1];
            activeReads += sleep[i] == 1 ? nums[i] : 0;
        }
        long maxReads = prefix[k-1];
        for(int i = k; i < n; i++)
            maxReads = Math.max(maxReads, prefix[i]-prefix[i-k]);
        return activeReads+maxReads;
    }
}
