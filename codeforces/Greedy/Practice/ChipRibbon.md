## Problem Overview

Monocarp is playing a game with a ribbon that has \( n \) cells (numbered 1 to \( n \)), where each cell initially contains the integer 0. Monocarp uses a chip to increment the value of cells according to the following rules:
- **First turn:** The chip is placed on cell 1.
- **Subsequent turns:** Monocarp can either:
  1. **Move the chip** to the next cell (i.e., from cell \( i \) to \( i+1 \))—this move is not allowed if the chip is on the last cell.
  2. **Teleport the chip** to any cell (even to the same cell).

At the end of every turn, the value in the cell where the chip is located is incremented by 1.

**Goal:**
Monocarp wants to achieve a final configuration where cell \( i \) contains the integer \( c_i \) for all \( 1 \le i \le n \). He wishes to minimize the number of teleportations required to achieve this configuration.

---

## Input and Output Format

**Input:**
- The first line contains an integer \( t \) (\( 1 \le t \le 10^4 \)) — the number of test cases.
- Each test case consists of two lines:
  - The first line contains an integer \( n \) (\( 1 \le n \le 2 \times 10^5 \)).
  - The second line contains \( n \) integers \( c_1, c_2, \dots, c_n \) (\( 0 \le c_i \le 10^9 \); \( c_1 \ge 1 \)) representing the desired final values in the cells.

It is guaranteed that the total sum of \( n \) over all test cases does not exceed \( 2 \times 10^5 \).

**Output:**
For each test case, output a single integer — the minimum number of times Monocarp has to teleport the chip.

---

## Example

**Input:**
```
4
4
1 2 2 1
5
1 0 1 0 1
5
5 4 3 2 1
1
12
```

**Output:**
```
1
2
4
11
```

**Explanation for Test Case 1:**
1. Place the chip on cell 1; cell 1 becomes 1. (State: [1, 0, 0, 0])
2. Move to cell 2; cell 2 becomes 1. (State: [1, 1, 0, 0])
3. Move to cell 3; cell 3 becomes 1. (State: [1, 1, 1, 0])
4. **Teleport** to cell 2; cell 2 becomes 2. (State: [1, 2, 1, 0])
5. Move to cell 3; cell 3 becomes 2. (State: [1, 2, 2, 0])
6. Move to cell 4; cell 4 becomes 1. (Final state: [1, 2, 2, 1])

The total number of teleportations is 1.

---

## Approach and Intuition

The key idea is to determine how many extra operations (teleportations) are needed:
- **For cell 1:** The chip is placed here initially. To reach a value of \( c_1 \), you need \( c_1 - 1 \) operations.
- **For subsequent cells:** When moving from cell \( i \) to cell \( i+1 \), if the desired value \( c_{i+1} \) is greater than \( c_i \), then extra operations (teleports) are needed. Specifically, \( \max(0, c_{i+1} - c_i) \) additional teleportations are required.

Thus, the **total number of teleportations** is:
\[
\text{operations} = (c_1 - 1) + \sum_{i=2}^{n} \max(0, c_i - c_{i-1})
\]

This greedy approach works because:
- Moves to the next cell (non-teleport) can be done as long as the required value does not increase.
- When an increase is required, the only way to “skip” extra operations is via teleportation.

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ChipRibbon {
    // FastReader class for fast input reading
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor to initialize the BufferedReader
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        // Reads the next token as an integer
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Reads the next token as a long
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt(); // Number of test cases
        final StringBuilder builder = new StringBuilder();
        while (t-- > 0) {
            int n = fast.nextInt();
            long nums[] = new long[n];
            for (int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            builder.append(minOperations(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Computes the minimum number of teleportations required to achieve the final state.
     *
     * @param n the number of cells in the ribbon.
     * @param nums an array representing the desired final value in each cell.
     * @return the minimum number of teleportations required.
     */
    public static long minOperations(final int n, final long nums[]) {
        // For the first cell, you need (c1 - 1) operations since the chip is initially placed there.
        long operations = nums[0] - 1;
        // For every subsequent cell, if the desired value increases, you need extra operations.
        for (int i = 1; i < n; i++)
            operations += Math.max(0, nums[i] - nums[i - 1]);
        return operations;
    }
}
```

---

## Core DSA Concepts and Techniques

1. **Greedy Algorithms:**
   - The problem is solved by making a series of local optimal decisions (calculating extra operations required for each cell transition), which in total give the minimum number of teleportations.

2. **Difference Array Technique:**
   - By analyzing the difference \( c_i - c_{i-1} \), the solution determines the additional increments needed. This is similar to constructing a difference array to efficiently handle range updates.

3. **Problem Decomposition:**
   - The problem is broken down into two parts: processing the first cell and processing the rest of the cells. This modular approach simplifies the reasoning.

---

## Complexity Analysis

- **Time Complexity:**
  - For each test case, we traverse the array once, so the time complexity is \( O(n) \) per test case.
  - Given that the sum of \( n \) over all test cases does not exceed \( 2 \times 10^5 \), the total complexity is acceptable.

- **Space Complexity:**
  - We use an array of size \( n \) for each test case, leading to a space complexity of \( O(n) \) per test case.

---

## Applications of These Techniques

The approach used in this problem is applicable in several scenarios:

1. **Difference Array Problems:**
   - **Example:** Range update queries in an array, where you need to compute the cumulative effect of operations.

2. **Greedy Increment Problems:**
   - **Example:** Minimum operations to equalize or adjust elements in an array (like making all elements equal with minimum increments).

3. **Simulation of Sequential Processes:**
   - **Example:** Problems involving simulation of a process where each step builds upon the previous state.

4. **Resource Allocation Problems:**
   - **Example:** Scheduling tasks or distributing resources where the increment or deficit needs to be addressed step by step.

---

## Final Summary

- **Problem Statement:**
  Determine the minimum number of teleportations needed for Monocarp to increment cells of a ribbon to reach a target configuration.

- **Key Technique:**
  Use a greedy approach by summing up the extra increments required when moving from one cell to the next.

- **DSA Concepts:**
  Greedy algorithms, difference array techniques, and simulation of sequential operations.

- **Complexity:**
  \( O(n) \) time and \( O(n) \) space per test case.

- **Broader Applications:**
  This technique is widely applicable in range update problems, greedy increment problems, simulation of sequential processes, and resource allocation challenges.
