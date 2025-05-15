Below is the comprehensive summary file for the **TMT Document** problem.

---

## Problem Overview

In this problem, you are given a string of length \( n \) (divisible by 3) composed only of the characters 'T' and 'M'. The task is to determine whether it is possible to partition the string into several disjoint subsequences (each character used exactly once) such that each subsequence is exactly equal to "TMT". In other words, you must check if the jumbled sequence can be rearranged by deletion (without reordering the remaining characters) into several copies of "TMT".

---

## Input and Output

#### Input Format
- The first line contains an integer \( t \) (\(1 \leq t \leq 5000\)) — the number of test cases.
- For each test case:
  - The first line contains an integer \( n \) (\(3 \leq n < 10^5\)), where \( n \) is divisible by 3.
  - The second line contains a string of length \( n \) consisting solely of the characters 'T' and 'M'.

It is guaranteed that the total sum of \( n \) over all test cases does not exceed \( 10^5 \).

#### Output Format
For each test case, print "Yes" if the string can be partitioned into disjoint subsequences equal to "TMT", and "No" otherwise.

#### Example

**Input:**
```
5
3
TMT
3
MTT
6
TMTMTT
6
TMTTTT
6
TTMMTT
```

**Output:**
```
Yes
No
Yes
No
Yes
```

**Explanation:**
- In the first test case, the string is already "TMT".
- In the third test case, the string can be partitioned into two subsequences "TMT" (for instance, by choosing appropriate indices).

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TMT {
    // FastReader class for efficient input reading
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token from the input
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt(); // Number of test cases
        final StringBuilder builder = new StringBuilder();
        // Process each test case
        while (t-- > 0)
            builder.append(possibleSubsequences(fast.nextInt(), fast.next()));
        System.out.print(builder);
    }

    /**
     * Determines whether it is possible to partition the string into disjoint subsequences,
     * each equal to "TMT".
     *
     * Approach:
     * 1. Basic checks:
     *    - n must be divisible by 3.
     *    - The first character must be 'T' and the last must be 'T'
     *      (otherwise, it is impossible to have a valid TMT partition).
     * 2. Store indices of all 'T's and all 'M's.
     * 3. Verify that the number of 'T's is exactly double the number of 'M's.
     * 4. For each occurrence of 'M' (in order), check:
     *    - There must be a 'T' before it (i-th 'T' must come before the i-th 'M').
     *    - There must be a 'T' after it (the (i + k)-th 'T' must come after the i-th 'M'),
     *      where \( k \) is the total number of 'M's.
     * This greedy index-check guarantees that each "TMT" subsequence can be formed.
     *
     * @param n The length of the string.
     * @param s The string consisting only of characters 'T' and 'M'.
     * @return "Yes" if a valid partition exists, otherwise "No".
     */
    public static String possibleSubsequences(final int n, final String s) {
        // Quick invalidity checks:
        if (n % 3 != 0 || s.charAt(0) == 'M' || s.charAt(n - 1) == 'M')
            return "No\n";

        // Lists to store the indices of 'T' and 'M'
        List<Integer> tIndex = new ArrayList<>(), mIndex = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'T')
                tIndex.add(i);
            else
                mIndex.add(i);
        }

        int k = mIndex.size(), tSize = tIndex.size();
        // There should be exactly twice as many 'T's as 'M's.
        if (k * 2 != tSize)
            return "No\n";

        // For each 'M', ensure there is a 'T' before and after it.
        for (int i = 0; i < k; i++)
            // Check: the i-th 'T' must come before the i-th 'M' AND
            // the (i+k)-th 'T' (i.e., a 'T' later in the order) must come after the i-th 'M'.
            if (tIndex.get(i) > mIndex.get(i) || tIndex.get(i + k) < mIndex.get(i))
                return "No\n";

        return "Yes\n";
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Greedy Matching with Index Tracking:**
   - The solution uses a greedy strategy by tracking the indices of 'T's and 'M's.
   - For each 'M', we ensure that there exists a 'T' before and another 'T' after it. This maintains the required order for forming "TMT".

2. **Subsequence Formation:**
   - The problem revolves around forming subsequences from the original string while preserving the original order.
   - This is similar to other subsequence validation problems where order constraints are critical.

3. **Two-Pointer / Index Comparison Technique:**
   - By storing indices in two lists and iterating over them simultaneously, the algorithm effectively mimics a two-pointer approach.
   - This is a common technique in problems that require simultaneous traversal of multiple sequences.

---

## **Where Else Can We Apply This Concept?**

The technique of greedy matching using index tracking (or a two-pointer approach) to validate and form subsequences is widely applicable. Here are some example problem scenarios where you can use this concept:

1. **Valid Parentheses Sequence:**
   - **Problem Example:** Given a string of parentheses, check if it is valid.
   - **Technique:** Use a stack (or two pointers) to ensure that each opening bracket has a corresponding closing bracket in the correct order.

2. **Longest Common Subsequence (LCS):**
   - **Problem Example:** Given two strings, find their longest common subsequence.
   - **Technique:** Use dynamic programming combined with two-pointer traversal to extract the common subsequence.

3. **Subsequence Partitioning:**
   - **Problem Example:** Given a string, partition it into palindromic subsequences.
   - **Technique:** Greedy or dynamic programming approaches that preserve the order of characters while satisfying partition conditions.

4. **String Reconstruction Problems:**
   - **Problem Example:** Rearranging or validating strings based on given constraints (e.g., rearranging characters so that no two adjacent characters are the same).
   - **Technique:** Use index tracking and greedy placement to ensure that required ordering constraints are met.

5. **Competitive Programming Contests:**
   - Many problems involve verifying if a string can be segmented into valid subsequences (like “TMT Document”) by maintaining order.
   - **Example Problems:** "Yet Another String Game", "Balanced Remainders", and other string partitioning problems.

---

## Time and Space Complexity Analysis

- **Time Complexity:**
  - The solution performs a single pass to collect indices \(O(n)\) and then iterates over the collected indices \(O(n)\).
  - Overall, each test case runs in \(O(n)\).

- **Space Complexity:**
  - The indices of 'T' and 'M' are stored in lists, which in the worst-case require \(O(n)\) space.
  - Hence, the space complexity is \(O(n)\).

---

## Final Summary

- **Problem Statement:**
  Check if a given string of 'T' and 'M' characters can be partitioned into disjoint subsequences, each exactly equal to "TMT".

- **Key Techniques:**
  - Greedy matching using index tracking.
  - Two-pointer approach to ensure order constraints (each 'M' is flanked by appropriate 'T's).

- **DSA Concepts Covered:**
  - Subsequence formation.
  - Greedy algorithms.
  - Two-pointer/index tracking techniques.

- **Where Else to Apply This Concept:**
  - Validating parentheses sequences.
  - Longest Common Subsequence (LCS) problems.
  - Partitioning a string into subsequences satisfying given constraints.
  - String reconstruction and rearrangement problems.
  - Numerous competitive programming problems involving string segmentation.

- **Complexity:**
  \(O(n)\) time per test case and \(O(n)\) space per test case.

This summary offers a detailed explanation of the problem, a step-by-step code walkthrough with clear comments, insights into the core techniques and DSA concepts used, and examples of other problems where similar techniques can be effectively applied.
