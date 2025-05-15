## Problem Overview

You are given \(n\) students, each with a skill level \(a_i\). Your task is to form exactly two teams of equal size \(x\) with the following conditions:
- The **first team** must have students with **distinct skills** (all skills are unique).
- The **second team** must have students with **identical skills** (all skills are the same).

Note that a student cannot belong to both teams. Your goal is to determine the maximum possible value of \(x\) (team size) for which it is possible to form such a pair of teams.

**Example Explanation:**
- For skills \([4, 2, 4, 1, 4, 3, 4]\), one valid solution is:
  - First team: \([1, 2, 4]\) — all unique.
  - Second team: \([4, 4, 4]\) — all identical.
  - Thus, the maximum team size is 3.

---

## Input and Output

#### **Input Format**
- The first line contains an integer \(t\) (\(1 \le t \le 10^4\)) — the number of test cases.
- For each test case:
  - The first line contains an integer \(n\) (\(1 \le n \le 2 \times 10^5\)) — the number of students.
  - The second line contains \(n\) integers \(a_1, a_2, \dots, a_n\) (\(1 \le a_i \le n\)), representing the skill of each student.

The sum of \(n\) over all test cases does not exceed \(2 \times 10^5\).

#### **Output Format**
For each test case, output the maximum possible team size \(x\) such that:
- One team has \(x\) distinct skills.
- The other team has \(x\) students with the same skill.

If no valid pair of teams can be formed, the answer is 0.

#### **Example**

**Input:**
```
4
7
4 2 4 1 4 3 4
5
2 1 5 4 3
1
1
4
1 1 1 3
```

**Output:**
```
3
1
0
2
```

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

public class TwoTeams {

    // FastReader class for efficient input reading
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token from input
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        // Read next token as an integer
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Read next token as a long
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();

        // Process each test case
        while (t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(twoTeamsComposing(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Returns the maximum possible team size x such that:
     *  - One team of x students has distinct skills.
     *  - The other team of x students has identical skills.
     *
     * Approach:
     * 1. Sort the array to easily count the frequencies of each skill.
     * 2. Use a set to count the number of distinct skills.
     * 3. Use a "modified Kadane" (greedy approach) to find the longest contiguous subarray of the same value,
     *    which represents the maximum number of students with the same skill (potential second team).
     * 4. If the number of distinct skills equals the longest equal sequence,
     *    then one student must be removed from the distinct team to ensure team sizes are equal.
     * 5. Otherwise, the answer is the minimum of the number of distinct skills and the longest equal sequence.
     *
     * @param n The number of students.
     * @param nums Array of students' skills.
     * @return The maximum possible team size.
     */
    public static int twoTeamsComposing(final int n, int nums[]) {
        // Sort the array to group identical skills together.
        Arrays.sort(nums);

        // Variables to store the longest contiguous subarray (max frequency of a single skill)
        int longestSubarray = 0;
        int prev = Integer.MIN_VALUE, subarray = 0;

        // Set to store distinct skills.
        Set<Integer> unique = new HashSet<>();

        // Process the sorted array to count the longest sequence and distinct skills.
        for (int i = 0; i < n; i++) {
            // If the current number is different from the previous, reset the subarray counter.
            if (prev != nums[i]) {
                prev = nums[i];
                subarray = 0;
            }
            subarray++;
            unique.add(nums[i]);  // Add skill to the set for distinct count.
            longestSubarray = Math.max(subarray, longestSubarray);
        }

        // When the number of distinct skills equals the maximum frequency,
        // one student must be moved from the second team to the first team (thus reducing team size by 1).
        if (unique.size() == longestSubarray)
            return longestSubarray - 1;
        else
            // Otherwise, the maximum team size is the minimum of distinct skills and maximum frequency.
            return Math.min(unique.size(), longestSubarray);
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Sorting & Frequency Counting:**
   - **Concept:** Sorting the array groups identical skills together, making it easier to count the frequency of each skill.
   - **Application:** Sorting is used extensively in problems where order and grouping are crucial. In this case, it allows for a straightforward computation of both the distinct count and the maximum frequency.

2. **Set for Distinct Count:**
   - **Concept:** Using a `HashSet` to collect unique elements provides the count of distinct skills.
   - **Application:** This technique is standard in problems where uniqueness matters. It’s also useful for quickly checking membership.

3. **Greedy / Modified Kadane Approach:**
   - **Concept:** Although not a classic Kadane’s algorithm (which finds the maximum sum subarray), the idea of iteratively tracking a "subarray" (consecutive equal values) is applied to determine the frequency of the most common skill.
   - **Application:** Greedy approaches are common when making locally optimal decisions (here, counting frequencies) leads to the overall optimal solution.

4. **Balancing Two Constraints:**
   - **Concept:** The final answer is determined by balancing the two constraints: the number of distinct skills and the maximum frequency.
   - **Application:** Many optimization problems require balancing two or more competing factors, such as resource allocation, scheduling, and partitioning.

---

## **Where Else Can We Apply This Logic and These Concepts?**

1. **Team Formation and Resource Allocation:**
   - **Real-World Example:** Assigning tasks or resources where one group requires diversity (unique skills) and another requires uniformity (same skill).
   - **Problem Solving:** Problems involving partitioning a set into groups with different properties can use similar balancing techniques.

2. **Frequency Analysis:**
   - **Text and Data Analysis:** Counting distinct elements (e.g., words in a document) versus the frequency of the most common element.
   - **Histogram Computation:** Creating histograms or frequency distributions where both the variety and the concentration of data matter.

3. **Greedy and Sorting Techniques:**
   - **Algorithm Design:** Many algorithms rely on sorting and greedy strategies to achieve an optimal solution, such as scheduling problems, interval selection, or even certain graph algorithms.
   - **Competitive Programming:** These methods are staples in contests and are applicable in a wide range of problems where order and frequency are key.

4. **Dynamic Programming with Constraints:**
   - **Partition Problems:** Balancing constraints to partition a set into subsets that meet certain criteria.
   - **Resource Distribution:** Problems in which you distribute resources under multiple constraints (e.g., distinct versus identical attributes).

---

## Time and Space Complexity Analysis

- **Time Complexity:**
  - **Sorting:** Takes \(O(n \log n)\).
  - **Frequency Counting and Set Operations:** Linear scan \(O(n)\) with \(O(1)\) average for each set insertion.
  - **Overall per Test Case:** \(O(n \log n)\). Given \(\sum n \le 2 \times 10^5\), this is efficient.

- **Space Complexity:**
  - **Auxiliary Structures:** Uses an additional array for sorting and a `HashSet` for distinct values — both are \(O(n)\).
  - **Overall:** \(O(n)\) space per test case.

---

## Final Summary

- **Problem Statement:**
  Given the skills of \(n\) students, determine the maximum team size \(x\) such that:
  - One team of \(x\) students has distinct skills.
  - The other team of \(x\) students has identical skills.

- **Key Techniques:**
  - **Sorting:** To group identical skills.
  - **Frequency Counting:** To determine the maximum frequency (for the second team) and distinct count (for the first team).
  - **Greedy Strategy:** Balancing the two constraints by taking the minimum of distinct count and maximum frequency (adjusted when they are equal).

- **DSA Concepts Covered:**
  - Sorting and frequency counting.
  - Use of hash sets for uniqueness.
  - Greedy algorithms for balancing constraints.

- **Where Else to Apply:**
  - Team formation and resource allocation problems.
  - Data analysis problems involving frequency and uniqueness.
  - Partitioning problems and dynamic programming with multiple constraints.

- **Complexity:**
  \(O(n \log n)\) time per test case and \(O(n)\) space per test case.
