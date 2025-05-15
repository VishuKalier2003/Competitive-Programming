Below is the complete summary file for the **Binary Inversions** problem, including the problem statement, input/output details, step-by-step explanation of the approach and code (with clear comments), key DSA concepts, complexity analysis, and related applications of the techniques used.

---

## Problem Overview

You are given a binary array (i.e. an array of 0s and 1s) of length \( n \). You are allowed to perform **at most one operation**: choose any element and flip it (turn a 0 into a 1 or vice versa).
The goal is to maximize the number of **inversions** in the array after performing at most one operation.
An inversion is defined as a pair of indices \( (i, j) \) such that \( i < j \) and \( a_i > a_j \).
For a binary array, inversions occur when a 1 appears before a 0.

---

## Input and Output Format

**Input:**
- The first line contains an integer \( t \) (\(1 \le t \le 10^4\)) — the number of test cases.
- Each test case consists of:
  - A line with an integer \( n \) (\(1 \le n \le 2 \times 10^5\)) — the length of the array.
  - A line with \( n \) space-separated integers, each either 0 or 1.

It is guaranteed that the sum of \( n \) over all test cases does not exceed \( 2 \times 10^5 \).

**Output:**
- For each test case, output a single integer — the maximum number of inversions achievable after performing at most one flip.

---

## Example

**Input:**
```
5
4
1 0 1 0
6
0 1 0 0 1 0
2
0 0
8
1 0 1 1 0 0 0 1
3
1 1 1
```

**Output:**
```
3
7
1
13
2
```

**Explanation (Test Case 2):**
- Original array: \([0, 1, 0, 0, 1, 0]\)
- Original inversions: (2,3), (2,4), (2,6), (5,6) = 4 inversions.
- By flipping the first element to 1, the array becomes \([1, 1, 0, 0, 1, 0]\) with inversions:
  - Inversions from first 1: (1,3), (1,4), (1,6)
  - Inversions from second 1: (2,3), (2,4), (2,6)
  - Inversions from third 1 (at index 5): (5,6)
  - Total = 7 inversions, which is the maximum possible.

---

## Approach and Intuition

1. **Initial Inversion Count:**
   Compute the number of inversions in the original binary array. For a binary array, we count inversions by scanning from right-to-left and maintaining a count of 0s encountered. For every 1, add the count of 0s to the inversion total.

2. **Effect of a Flip:**
   When you flip an element, it changes the inversion count.
   - **Flipping a 1 to 0:**
     - **Loss:** Inversions where this 1 was before some 0s are lost.
     - **Gain:** New inversions are created by counting the number of 1s before this index that now become inversions with the new 0.
   - **Flipping a 0 to 1:**
     - **Gain:** New inversions are created by counting the number of 0s after this index that now form inversions with the new 1.
     - **Loss:** Inversions that involved this 0 as a "tail" of a 1 are lost.

3. **DP Arrays (Cumulative Counts):**
   - **dpOne:** For each index \( i \), count of ones to the left of \( i \).
   - **dpZero:** For each index \( i \), count of zeros to the right of \( i \).

   These arrays help quickly determine the impact (increase or decrease in inversion count) if you flip the element at index \( i \).

4. **Maximizing the Inversion Count:**
   For each index, compute the candidate change in inversion count if that element were flipped:
   - For a 1, candidate change = (number of 1s to the left) - (number of 0s to the right).
   - For a 0, candidate change = (number of 0s to the right) - (number of 1s to the left).

   Choose the flip that yields the maximum increase. If no flip improves the count, do not flip any element.

5. **Final Answer:**
   The maximum possible inversions = original inversion count + best candidate increase (or the original inversion count if the best candidate is negative).

---

## Code Walkthrough with Detailed Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Inversions {

    // FastReader class for fast input reading
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor initializes BufferedReader
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token from the input stream
        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        // Returns the next integer from input
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Returns the next long integer from input
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt(); // Number of test cases
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            // Append the maximum inversion count for each test case
            builder.append(maxInversions(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Computes the maximum inversions possible after performing at most one flip.
     *
     * @param n Length of the binary array.
     * @param nums The binary array.
     * @return The maximum number of inversions achievable.
     */
    public static long maxInversions(int n, int nums[]) {
        // Compute the inversion count of the original array.
        long originalInversion = inversion(n, nums);

        // dpOne[i] = number of ones to the left of index i.
        int dpOne[] = new int[n];
        // dpZero[i] = number of zeros to the right of index i.
        int dpZero[] = new int[n];
        dpOne[0] = 0;
        dpZero[n - 1] = 0;

        // Compute cumulative count of ones from the left.
        for (int i = 1; i < n; i++)
            dpOne[i] = dpOne[i - 1] + (nums[i - 1] == 1 ? 1 : 0);

        // Compute cumulative count of zeros from the right.
        for (int j = n - 2; j >= 0; j--)
            dpZero[j] = dpZero[j + 1] + (nums[j + 1] == 0 ? 1 : 0);

        // Determine the best possible change in inversion count if we flip one element.
        long bestIncrease = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long candidate;
            if (nums[i] == 1)
                // Flipping 1 to 0: Loss of inversions where 1 was before zeros, gain of inversions where 1 was after ones.
                candidate = dpOne[i] - dpZero[i];
            else
                // Flipping 0 to 1: Gain inversions with zeros to the right, loss inversions with ones to the left.
                candidate = dpZero[i] - dpOne[i];
            bestIncrease = Math.max(bestIncrease, candidate);
        }
        // If flipping does not improve the inversion count, choose not to flip (increase = 0).
        bestIncrease = Math.max(bestIncrease, 0);
        return originalInversion + bestIncrease;
    }

    /**
     * Counts the number of inversions in the binary array.
     * An inversion is a pair (i, j) such that i < j, nums[i] == 1, and nums[j] == 0.
     *
     * @param n Length of the array.
     * @param nums The binary array.
     * @return The inversion count.
     */
    public static long inversion(int n, int nums[]) {
        long inversion = 0, count0 = 0;
        // Process from right to left: count zeros and add to inversion count when encountering a 1.
        for (int i = n - 1; i >= 0; i--) {
            if (nums[i] == 0)
                count0++;
            else
                inversion += count0;
        }
        return inversion;
    }
}
```

---

## Core DSA Concepts and Techniques

1. **Inversion Counting:**
   - **Technique:** Count inversions by traversing the array from right-to-left while maintaining a count of zeros.
   - **Application:** Widely used in sorting algorithms (like merge sort inversion count) and in problems involving order statistics.

2. **Dynamic Programming with Cumulative Arrays:**
   - **dpOne and dpZero:** These arrays store cumulative counts, enabling efficient computation of the impact of flipping a particular element.
   - **Usage:** This technique is often used in range query problems, sliding window problems, and situations where you need to quickly compute aggregated data over subarrays.

3. **Greedy Choice:**
   - **Technique:** Evaluate the effect of flipping each element and select the flip that yields the maximum increase in inversions.
   - **Usage:** Greedy strategies are key in optimization problems where local choices lead to the best global outcome.

---

## Complexity Analysis

- **Time Complexity:**
  - **Inversion Counting:** \( O(n) \).
  - **Building dpOne and dpZero arrays:** \( O(n) \).
  - **Iterating to determine best flip candidate:** \( O(n) \).
  - **Overall per test case:** \( O(n) \).

- **Space Complexity:**
  - \( O(n) \) for the dpOne and dpZero arrays.
  - Overall, \( O(n) \) space per test case.

---

## Related Applications and Problems

The techniques used here can be applied in several other problems, such as:
1. **Inversion Count Problems:**
   - **Merge Sort Inversion Count:** Counting inversions while sorting an array.
   - **Competitive Programming Challenges:** Many problems require counting inversions or order discrepancies.

2. **Cumulative Sum / Prefix Sum Problems:**
   - **Range Sum Queries:** Using prefix sums to answer queries quickly.
   - **Subarray Sum Problems:** Problems like "Subarray Sum Equals K" use cumulative sum techniques.

3. **Greedy Optimization Problems:**
   - **Maximizing or Minimizing a Certain Metric:** For example, maximizing subarray sums with one allowed change.
   - **Flipping Bits for Maximum Effect:** Similar to problems where you flip bits (or characters) to maximize/minimize a target function.

---

## Final Summary

- **Problem Statement:**
  Given a binary array, determine the maximum number of inversions that can be achieved by flipping at most one element.

- **Approach:**
  - Calculate the original inversion count.
  - Use cumulative arrays (`dpOne` and `dpZero`) to quickly assess the effect of flipping each element.
  - Choose the flip (or no flip) that maximizes the inversion count.

- **Key Techniques:**
  - Inversion counting using right-to-left traversal.
  - Dynamic programming with cumulative (prefix/suffix) arrays.
  - Greedy selection to optimize a single allowed operation.

- **Complexity:**
  \( O(n) \) time per test case and \( O(n) \) space.

- **Broader Applications:**
  These techniques are applicable in inversion count problems, range sum queries, and various greedy optimization challenges in competitive programming.

This summary provides a detailed explanation of the problem, the solution approach, and the key concepts used, along with related applications in similar problem domains.
