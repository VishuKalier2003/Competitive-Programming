## Problem Overview

You are given an array **a** of length \(2n\) containing each integer from \(1\) to \(n\) exactly twice. You are also given an integer \( k \) with \(1 \le k \le \lfloor \frac{n}{2} \rfloor\).

Your task is to choose two subsets (or subsequences) \( l \) and \( r \) from the array such that:

- \( l \) is chosen from the first half \([a_1, a_2, \dots, a_n]\).
- \( r \) is chosen from the second half \([a_{n+1}, a_{n+2}, \dots, a_{2n}]\).
- Both \( l \) and \( r \) have exactly \(2k\) elements.
- The bitwise XOR of all elements in \( l \) is equal to the bitwise XOR of all elements in \( r \).

It is guaranteed that at least one solution always exists, and if there are multiple solutions, you may output any one of them.

---

## Input and Output

#### **Input Format**
- The first line contains an integer \( t \) — the number of test cases.
- For each test case:
  - The first line contains two integers \( n \) and \( k \).
  - The second line contains \(2n\) integers \(a_1, a_2, \dots, a_{2n}\) where each integer from \(1\) to \(n\) appears exactly twice.

#### **Output Format**
For each test case, output two lines:
- First line: \(2k\) integers representing the subsequence \( l \) (from the first half).
- Second line: \(2k\) integers representing the subsequence \( r \) (from the second half).

#### **Example**

**Input:**
```
4
2 1
1 2 2 1
6 1
6 4 2 1 2 3 1 6 3 5 5 4
4 1
1 2 3 4 1 2 3 4
6 2
5 1 3 3 5 1 2 6 4 6 4 2
```

**Output:**
```
2 1
2 1
6 4
1 3
1 2
1 2
5 1 3 3
6 4 2 4
```

*Explanation:*
- In the first test case, \( l = [2, 1] \) (from the first half) and \( r = [2, 1] \) (from the second half) produce \(2 \oplus 1 = 3\) in both halves.

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class EqualXOR {
    // FastReader class to handle fast input reading.
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor initializes BufferedReader.
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token from input.
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

        // Parses next token as integer.
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Parses next token as long.
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();  // Number of test cases.
        final StringBuilder builder = new StringBuilder();

        // Process each test case.
        while(tests-- > 0) {
            int n = fast.nextInt(), k = fast.nextInt();
            int nums[] = new int[n * 2];  // Array of size 2n.
            for (int i = 0; i < n * 2; i++)
                nums[i] = fast.nextInt();
            // Append the result for current test case.
            builder.append(findSubsequence(n, k, nums)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Finds two subsequences l and r such that:
     * - l is a subset of the first half and r is a subset of the second half.
     * - Both l and r have 2k elements.
     * - XOR of all elements in l equals XOR of all elements in r.
     *
     * The method uses frequency counting (a counting-sort idea) to select elements.
     */
    public static StringBuilder findSubsequence(int n, int k, int nums[]) {
        int firstHalf[] = new int[n + 1], secondHalf[] = new int[n + 1];

        // Count frequencies for the first half and second half separately.
        for (int i = 0; i < 2 * n; i++) {
            if (i < n)
                firstHalf[nums[i]]++;
            else
                secondHalf[nums[i]]++;
        }

        // Use sets to store chosen numbers for left and right subsequences.
        Set<Integer> leftChosen = new HashSet<>(), rightChosen = new HashSet<>();
        int temp = k;
        // Select elements that appear twice in the first half.
        // Using negative values as markers to indicate "double occurrence".
        for (int i = 1; i <= n && k > 0; i++)
            if (firstHalf[i] == 2 && k > 0) {
                leftChosen.add(-i);
                k--;
            }
        k = temp;
        // Do the same for the second half.
        for (int j = 1; j <= n && k > 0; j++)
            if (secondHalf[j] == 2 && k > 0) {
                rightChosen.add(-j);
                k--;
            }
        k *= 2;  // Adjust k to represent the total number of elements required.

        // For elements that appear once in the halves, add them to both sets.
        for (int t = 1; Math.min(t, k) > 0; t++)
            if (firstHalf[t] == 1) {  // When the element appears once, include it in both subsequences.
                leftChosen.add(t);
                rightChosen.add(t);
                k--;
            }
        return generateAndMeetSubsequence(leftChosen, rightChosen, n, nums);
    }

    /**
     * Generates the final subsequences from the chosen sets.
     * The method iterates over the original array halves to extract the elements in the order they appear.
     *
     * @param firstSet Chosen numbers for the left subsequence (from first half).
     * @param secondSet Chosen numbers for the right subsequence (from second half).
     * @param n Half-length of the array.
     * @param nums The input array of 2n numbers.
     * @return A StringBuilder containing the two subsequences separated by a newline.
     */
    public static StringBuilder generateAndMeetSubsequence(Set<Integer> firstSet, Set<Integer> secondSet, int n, int nums[]) {
        StringBuilder lSeq = new StringBuilder(), rSeq = new StringBuilder();

        // Generate left subsequence from the first half.
        for (int i = 0; i < n; i++) {
            if (firstSet.contains(-nums[i])) {  // Check for element marked as appearing twice.
                lSeq.append(nums[i]).append(" ");
                firstSet.remove(-nums[i]);      // Remove marker.
                firstSet.add(nums[i]);          // Add as positive to indicate one occurrence used.
            } else if (firstSet.contains(nums[i])) {  // For elements appearing once.
                lSeq.append(nums[i]).append(" ");
                firstSet.remove(nums[i]);
            }
        }

        // Generate right subsequence from the second half.
        for (int j = n; j < 2 * n; j++) {
            if (secondSet.contains(-nums[j])) {
                rSeq.append(nums[j]).append(" ");
                secondSet.remove(-nums[j]);
                secondSet.add(nums[j]);
            } else if (secondSet.contains(nums[j])) {
                rSeq.append(nums[j]).append(" ");
                secondSet.remove(nums[j]);
            }
        }
        // Merge the two subsequences into one output.
        return lSeq.append("\n").append(rSeq);
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Counting Sort / Frequency Counting:**
   - **Concept:** The code uses arrays `firstHalf[]` and `secondHalf[]` to count the frequency of each number in the first and second halves.
   - **Application:** This technique is used to quickly determine how many times an element appears, which is useful for problems where elements have constraints on frequency (e.g., sorting integers with a limited range).

2. **Bitwise XOR Properties:**
   - **Concept:** The property \( x \oplus x = 0 \) ensures that pairing identical numbers results in zero. This idea is central when constructing two subsequences with equal XOR.
   - **Application:** Bitwise XOR properties are extensively used in problems like finding unique elements, subset XOR problems, and cryptographic algorithms.

3. **Greedy Selection:**
   - **Concept:** The solution greedily chooses elements that appear twice (by marking them with negative values) to ensure that when paired, they cancel out in the XOR operation.
   - **Application:** Greedy algorithms are used when local optimal decisions (like selecting pairs that yield zero XOR) lead to a valid global solution.

4. **Subsequence Construction:**
   - **Concept:** A subsequence can be constructed by selectively picking elements from an array while preserving order.
   - **Application:** This concept is common in problems related to longest common subsequence (LCS), sequence alignment, and dynamic programming on sequences.

---

## **Where Else Can We Apply This Logic and Concepts?**

1. **Optimization with Bitwise Operations:**
   - **Subset XOR Problems:** Many challenges ask for a subset of numbers with a specific XOR result (e.g., finding subsets with XOR equal to zero).
   - **Error Detection/Correction:** XOR is a fundamental operation in parity checks and error-detecting codes.

2. **Resource Distribution Problems:**
   - **Balancing and Partitioning:** The idea of splitting elements into two groups with equal XOR can be extended to more general partitioning problems, where you need to balance certain properties (such as sum, product, etc.) across groups.

3. **Frequency-Based Algorithms:**
   - **Counting Sort and Histogram Methods:** The frequency counting approach is useful in sorting and data analysis tasks where you need to quickly compute statistics from a limited range of numbers.

4. **Greedy Algorithms in Pairing Problems:**
   - **Matching and Pairing:** Greedy techniques to pair elements (like using the property \( x \oplus x = 0 \)) are applicable in problems like pairing socks, matching tasks, and even network flow in bipartite graphs.

5. **Subsequence and Sequence Construction:**
   - **Dynamic Programming on Sequences:** Understanding how to construct subsequences with specific properties is essential for problems such as Longest Increasing Subsequence (LIS) and other sequence alignment challenges.
   - **Data Compression:** Selecting optimal subsequences can be relevant in data compression where redundancy is removed while preserving certain features.

---

## **Time and Space Complexity Analysis**

- **Time Complexity:**
  - **Frequency Counting:** Iterates through \(2n\) elements, which is \(O(n)\) per test case.
  - **Subsequence Construction:** Iterates through each half of the array separately, also \(O(n)\).
  - **Overall per Test Case:** \(O(n)\). Given that the sum of \( n \) across test cases is bounded by \(5 \times 10^4\), the approach is efficient.

- **Space Complexity:**
  - **Auxiliary Arrays:** Two frequency arrays of size \(n+1\), which is \(O(n)\).
  - **Sets for Chosen Elements:** At most \(O(n)\) in worst-case.
  - **Output Storage:** Uses a `StringBuilder` for output, which is also \(O(n)\) per test case.

Thus, the overall space complexity per test case is \(O(n)\).

---

## **Final Summary**

- **Problem Statement:** Given an array with each integer from 1 to \( n \) occurring twice, split it into two subsequences from the first and second halves such that each has \(2k\) elements and both subsequences have equal bitwise XOR.
- **Key Ideas:**
  - **Frequency Counting:** Distinguish elements appearing twice vs. once.
  - **XOR Properties:** Utilize \( x \oplus x = 0 \) to cancel out contributions.
  - **Greedy Selection:** Choose pairs that balance the XOR.
- **DSA Concepts Covered:**
  - Counting sort / frequency counting.
  - Bitwise operations and XOR properties.
  - Greedy algorithms.
  - Subsequence construction.
- **Where Else to Apply:**
  - Problems involving subset XOR and error detection.
  - Resource allocation and partitioning challenges.
  - Frequency analysis and matching problems.
  - Sequence construction in dynamic programming and data compression.
- **Complexity:** The solution runs in \(O(n)\) time and uses \(O(n)\) space per test case.
