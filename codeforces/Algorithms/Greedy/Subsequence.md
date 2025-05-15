Below is a comprehensive summary file for the **Alternating Subsequence** problem. This document includes the problem name, a detailed problem description, explanations of sample test cases, fully commented code (with inline comments for each class and function), a flowchart diagram illustrating the overall process, time and space complexity analysis, and a summary paragraph discussing the approach and its optimality.

---

## Problem Name

**Alternating Subsequence**

---

## Problem Description

You are given a sequence \( a = [a_1, a_2, \dots, a_n] \) of nonzero integers (both positive and negative). An alternating subsequence is one in which the signs of the elements alternate (e.g. positive, negative, positive, … or negative, positive, negative, …).

Your task is to choose a subsequence that is **maximum by length** among all alternating subsequences. Among all such maximum‐length alternating subsequences, you must choose one that has the **maximum sum** of its elements. You need to output that maximum sum.

**Key Points:**

- A subsequence is derived by deleting zero or more elements (without changing the order).
- The subsequence must alternate in sign.
- The goal is to maximize the length first; among all maximum-length choices, maximize the sum.

**Intuition:**
It turns out that the optimal strategy is to group consecutive elements with the same sign and pick the largest element from each group. This greedy approach both produces an alternating subsequence of maximum possible length and maximizes the sum.

---

## Sample Test Cases

**Example 1:**

- **Input:**
  \( n = 5 \)
  \( a = [1, 2, 3, -1, -2] \)

- **Explanation:**
  - The maximum-length alternating subsequence is obtained by selecting one element from each contiguous group of same sign.
  - The positive group \([1, 2, 3]\) yields \(3\) (the maximum element).
  - The negative group \([-1, -2]\) yields \(-1\) (the maximum among negatives, i.e. the least negative).
  - Sum = \(3 + (-1) = 2\).

- **Output:**
  `2`

**Example 2:**

- **Input:**
  \( n = 4 \)
  \( a = [-1, -2, -1, -3] \)

- **Explanation:**
  - There is only one group of negative numbers, but note that the maximum-length alternating subsequence in a sequence of all same sign is just one element.
  - The best (maximum sum) choice is \([-1]\).

- **Output:**
  `-1`

**Example 3:**

- **Input:**
  \( n = 10 \)
  \( a = [-2, 8, 3, 8, -4, -15, 5, -2, -3, 1] \)

- **Explanation:**
  - Group the sequence by sign:
    - Group 1 (negative): \([-2]\) → \(-2\)
    - Group 2 (positive): \([8, 3, 8]\) → maximum is \(8\)
    - Group 3 (negative): \([-4, -15]\) → maximum is \(-4\)
    - Group 4 (positive): \([5]\) → \(5\)
    - Group 5 (negative): \([-2, -3]\) → maximum is \(-2\)
    - Group 6 (positive): \([1]\) → \(1\)
  - The alternating subsequence is \([-2, 8, -4, 5, -2, 1]\) and its sum is:
    \(-2 + 8 + (-4) + 5 + (-2) + 1 = 6\).

- **Output:**
  `6`

**Example 4:**

- **Input:**
  \( n = 6 \)
  \( a = [1, -1000000000, 1, -1000000000, 1, -1000000000] \)

- **Explanation:**
  - The sequence already alternates.
  - The sum is:
    \(1 + (-1000000000) + 1 + (-1000000000) + 1 + (-1000000000) = -2999999997\).

- **Output:**
  `-2999999997`

---

## Code with Detailed Inline Comments

```java
import java.io.*;
import java.util.*;

// Class: Subsequence
// Purpose: Solve the problem of finding the maximum sum of the maximum-length alternating subsequence.
public class Subsequence {

    // ----------------------------------------------------
    // Class: FastReader
    // ----------------------------------------------------
    // Purpose: Provides efficient input reading using BufferedReader and StringTokenizer.
    // ----------------------------------------------------
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        // Constructor: Initializes BufferedReader with System.in.
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Method: next()
        // Returns the next token from input.
        public String next() {
            try {
                while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                    tokenizer = new StringTokenizer(buffer.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tokenizer.nextToken();
        }

        // Method: nextInt()
        // Parses and returns the next token as an integer.
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Method: nextLong()
        // Parses and returns the next token as a long.
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    // ----------------------------------------------------
    // Record: TestCase
    // ----------------------------------------------------
    // Purpose: Represents a single test case containing the sequence of numbers.
    // ----------------------------------------------------
    public record TestCase(long[] nums) {}

    // ----------------------------------------------------
    // Main Method
    // ----------------------------------------------------
    // Purpose: Reads the number of test cases, then for each test case reads the sequence,
    // and outputs the maximum sum of the maximum-length alternating subsequence.
    // ----------------------------------------------------
    public static void main(String[] args) {
        FastReader fastReader = new FastReader();
        int t = fastReader.nextInt(); // Number of test cases.
        TestCase[] testCases = new TestCase[t];
        for (int i = 0; i < t; i++) {
            // For each test case, first read n (the number of elements).
            int n = fastReader.nextInt();
            long[] arr = new long[n];
            // Read the n elements.
            for (int j = 0; j < n; j++) {
                arr[j] = fastReader.nextLong();
            }
            testCases[i] = new TestCase(arr);
        }
        // Process each test case and output the result.
        Arrays.stream(testCases)
              .map(TestCase::nums)
              .map(Subsequence::maxAlternatingSum)
              .forEach(System.out::println);
    }

    // ----------------------------------------------------
    // Function: maxAlternatingSum
    // ----------------------------------------------------
    // Purpose: Computes the maximum sum of an alternating subsequence that is of maximum length.
    //
    // Logic:
    // - The optimal alternating subsequence can be obtained by taking, from each contiguous block
    //   of numbers with the same sign, the maximum element (for positive blocks, the largest positive;
    //   for negative blocks, the "largest" among negatives, i.e., the one with the least absolute value).
    // - This is because to maximize the sum and achieve maximum length, we need to pick one element from each block.
    //
    // Step-by-Step:
    // 1. Determine the sign of the first element.
    // 2. Initialize two variables: 'max' to track the maximum element in the current block,
    //    and 'sum' to accumulate the result.
    // 3. Iterate through the array:
    //    - For a block of same-signed numbers (depending on the current expected sign), update 'max' with the largest element.
    //    - When the sign changes, add the recorded 'max' to 'sum', reset 'max', and flip the expected sign.
    // 4. After processing the entire array, add the last block's 'max' to 'sum'.
    // 5. Return the computed 'sum'.
    //
    // Why it works:
    // - This greedy approach ensures that the subsequence is as long as possible (one element per block)
    //   and that among all maximum-length subsequences, the sum is maximized.
    // ----------------------------------------------------
    public static long maxAlternatingSum(long nums[]) {
        // Determine initial expected sign: true if positive, false if negative.
        boolean positive = nums[0] > 0;
        // 'max' stores the best candidate in the current block; initialize to smallest possible value.
        long max = Integer.MIN_VALUE, sum = 0;
        int i = 0;
        while (i < nums.length) {
            // For a positive block:
            if (positive) {
                // Process consecutive positive numbers.
                while (i < nums.length && nums[i] > 0) {
                    max = Math.max(max, nums[i]);
                    i++;
                }
            } else { // For a negative block:
                // Process consecutive negative numbers.
                while (i < nums.length && nums[i] < 0) {
                    max = Math.max(max, nums[i]);
                    i++;
                }
            }
            // Flip the expected sign for the next block.
            positive = !positive;
            // Add the maximum element of the current block to the overall sum.
            sum += max;
            // Reset 'max' for the next block.
            max = Integer.MIN_VALUE;
        }
        return sum;
    }
}
```

---

## Detailed Explanation & Intuition Behind the Code

### 1. **Input Reading (FastReader Class)**
- **What it does:**
  The `FastReader` class efficiently reads input tokens using a `BufferedReader` and `StringTokenizer`.
- **Why it’s needed:**
  Since the sum of lengths over all test cases can be up to \(2 \times 10^5\), efficient input is essential.

### 2. **Test Case Parsing and Main Method**
- **What it does:**
  The main method reads the number of test cases and for each test case, the sequence of integers.
- **Why:**
  Each test case is processed independently using the `maxAlternatingSum` function.
- **How:**
  We use a record `TestCase` to encapsulate each test case's array and then process them via Java Streams.

### 3. **Greedy Strategy in maxAlternatingSum**
- **Core Idea:**
  The optimal alternating subsequence of maximum length is achieved by selecting exactly one number from each contiguous block of same-signed numbers.
- **Why Select the Maximum in Each Block?**
  - For positive blocks, the larger number contributes more to the sum.
  - For negative blocks, choosing the maximum (least negative) minimizes the loss.
  - This selection guarantees maximum length (since you pick one per block) and maximum sum.
- **Process:**
  - Start with the first element’s sign.
  - Traverse through consecutive elements of the same sign, updating the candidate maximum.
  - When the sign changes, add the candidate maximum to the sum, reset, and switch the expected sign.
- **Why This Works:**
  The greedy approach works here because any alternating subsequence must choose at most one number per block to maintain maximum length, and the best choice from each block is the one that maximizes the contribution (largest positive or least negative).

### 4. **Flowchart Diagram**

```
                +--------------------------------+
                |            Start               |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | Read number of test cases (t)  |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | For each test case:            |
                |   Read n and sequence nums[]   |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | Call maxAlternatingSum(nums)   |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | Initialize:                    |\n|   positive = (nums[0] > 0)     |\n|   max = MIN_VALUE, sum = 0      |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | While i < n:                   |\n|   If expected sign is positive:\n|      While (i < n && nums[i] > 0):\n|          max = max(max, nums[i]); i++\n|   Else (expected negative):\n|      While (i < n && nums[i] < 0):\n|          max = max(max, nums[i]); i++\n|   Flip expected sign\n|   sum += max\n|   Reset max to MIN_VALUE      |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | Return sum                     |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                | Print answer for test case     |
                +---------------+----------------+
                                |
                                v
                +---------------+----------------+
                |            End                 |
                +--------------------------------+
```

---

## Time and Space Complexity

- **Time Complexity:**
  - Each test case processes its sequence in one pass: \(O(n)\).
  - Given that the sum of \(n\) over all test cases is \(\leq 2 \times 10^5\), the overall time is efficient.
- **Space Complexity:**
  - Only a few variables are used (for loop counters, sum, max, and a boolean flag), resulting in \(O(1)\) extra space per test case.

---

## Summary Paragraph

The solution to the **Alternating Subsequence** problem uses a greedy approach to construct the maximum-length alternating subsequence with the maximum sum. By iterating through the sequence and grouping consecutive elements of the same sign, the algorithm selects the best candidate (largest positive or least negative) from each group. This selection guarantees that the subsequence is as long as possible (one element per group) and maximizes the overall sum. The approach runs in linear time with \(O(n)\) complexity per test case and uses constant extra space, making it optimal given the problem constraints.

---

This summary file includes a complete breakdown of the problem, sample test cases, fully commented code with detailed explanations, a flowchart diagram, complexity analysis, and a concluding summary. If you have any further questions or need additional clarifications, feel free to ask!
