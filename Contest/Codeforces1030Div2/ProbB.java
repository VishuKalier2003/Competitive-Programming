import java.util.*;

public class ProbB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt(); // Number of test cases
        
        while (t-- > 0) {
            int n = scanner.nextInt(); // Size of the matrix
            List<int[]> operations = new ArrayList<>();
            
            // For each row i (1-based), transform it to [i, i+1, ..., n, 1, 2, ..., i-1]
            for (int i = 1; i <= n; i++) {
                // Row i needs to be rotated left by i-1 positions
                int shift = i - 1;
                if (shift == 0) continue; // No operation needed for row 1
                // To rotate left by k, we can:
                // 1. Reverse [1, k]
                // 2. Reverse [k+1, n]
                // This transforms [1, 2, ..., n] to [k, k-1, ..., 1, n, n-1, ..., k+1]
                // Then we may need to adjust further, but let's try to achieve the target
                if (shift > 0) {
                    // First operation: reverse [1, shift]
                    if (shift >= 2) {
                        operations.add(new int[]{i, 1, shift});
                    }
                    // Second operation: reverse [shift+1, n]
                    if (shift < n) {
                        operations.add(new int[]{i, shift + 1, n});
                    }
                }
            }
            
            // Output the result
            System.out.println(operations.size());
            for (int[] op : operations) {
                System.out.println(op[0] + " " + op[1] + " " + op[2]);
            }
        }
        scanner.close();
    }
}