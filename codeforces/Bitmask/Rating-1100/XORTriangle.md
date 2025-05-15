## Problem Overview

You are given an integer \(x\) (\(x \ge 2\)). Your task is to determine if there exists a positive integer \(y\) (\(1 \le y < x\)) such that the three side lengths:

- \(x\)
- \(y\)
- \(x \oplus y\) (bitwise XOR of \(x\) and \(y\))

can form a non-degenerate triangle. A triangle with side lengths \(a\), \(b\), and \(c\) is non-degenerate if it satisfies the triangle inequalities:
- \(a + b > c\)
- \(a + c > b\)
- \(b + c > a\)

If there exists such a \(y\), output any valid \(y\); otherwise, output \(-1\).

---

## Input and Output

#### **Input Format**
- The first line contains an integer \(t\) — the number of test cases.
- For each test case, there is a single integer \(x\) (\(2 \le x \le 10^9\)).

#### **Output Format**
- For each test case, print one integer on a separate line:
  - A valid \(y\) (with \(1 \le y < x\)) if one exists.
  - Otherwise, print \(-1\).

#### **Example**

**Input:**
```
7
5
2
6
3
69
4
420
```

**Output:**
```
3
-1
5
-1
66
-1
320
```

*Explanation:*
- In the first test case, \(x = 5\) and one valid answer is \(y = 3\). Note that \(3 \oplus 5 = 6\) and the sides \(3\), \(5\), \(6\) form a non-degenerate triangle.
- In some cases, like \(x = 2\) or \(x = 3\), no valid \(y\) exists that can form such a triangle, hence the answer is \(-1\).

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class XORTriangle {

    // FastReader class for efficient input reading
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor initializes the BufferedReader
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

        // Parse next token as integer
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Parse next token as long
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        // Process each test case
        while (tests-- > 0) {
            long num = fast.nextLong();
            // Create a mask that has all bits set to 1 up to (and including) the highest set bit of x
            long mask = (Long.highestOneBit(num) << 1) - 1;
            // Check if x is all ones (e.g., 3 (11), 7 (111), 15 (1111)) or if x is a power of 2
            boolean allOnes = (mask == num);
            boolean isPowerOf2 = (num == Long.highestOneBit(num));
            if (allOnes || isPowerOf2)
                builder.append("-1").append("\n");
            else {
                // The strategy:
                // 1. We compute (mask ^ num) to get all bits that are off in x, but are within the mask.
                // 2. Then we take the lowest set bit in x using Long.lowestOneBit(num).
                // 3. By OR-ing these two numbers, we obtain a candidate for y.
                long notPresentBit = mask ^ num;
                long presentBit = Long.lowestOneBit(num);
                // This candidate y satisfies the triangle condition with x and (x XOR y)
                builder.append(String.valueOf(notPresentBit | presentBit)).append("\n");
            }
        }
        System.out.print(builder);
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Bit Manipulation:**
   - **Understanding Binary Representations:**
     The solution leverages bitwise operations. The expression `Long.highestOneBit(num)` returns the largest power of 2 that is less than or equal to \(x\), and shifting it left and subtracting 1 gives a mask with all bits set up to that position.
   - **Creating and Using Masks:**
     The mask \((\text{highestOneBit}(x) << 1) - 1\) is used to create a number with all bits set within the range of \(x\). This is then used to determine which bits are off in \(x\) using XOR (`mask ^ x`).
   - **Bitwise OR and XOR:**
     The final candidate for \(y\) is formed by OR-ing a bit that is on in \(x\) (using `Long.lowestOneBit(num)`) with the bits that are off (from `mask ^ num`). This combination is critical to ensure the triangle inequality holds.

2. **Triangle Inequality:**
   - **Non-degenerate Triangle Condition:**
     For three side lengths to form a valid (non-degenerate) triangle, they must satisfy the triangle inequalities. The solution is designed so that with the chosen \(y\), the sides \(x\), \(y\), and \(x \oplus y\) satisfy these conditions.

3. **Edge Case Handling:**
   - **Handling Powers of Two and All-Ones Numbers:**
     If \(x\) is a power of 2 or \(x\) has all bits set (e.g., \(x = 3, 7, 15, \ldots\)), it is proven that no valid \(y\) exists, and the answer is \(-1\).

---

## **Where Else Can We Apply This Logic and These Concepts?**

1. **Bit Manipulation Problems:**
   - **Optimization and Masking:**
     The technique of creating masks and manipulating bits is common in problems that involve subsets, permissions, and flags. Problems that require checking or modifying specific bits in an integer are a frequent theme in competitive programming.
   - **Subset XOR Problems:**
     This concept is useful in problems where you need to find subsets with a particular XOR property, often seen in dynamic programming with bitmasking.

2. **Geometry and Triangle Conditions:**
   - **Triangle Formation Conditions:**
     The triangle inequality is a standard condition in many geometry problems. Understanding how to manipulate numerical properties to satisfy these conditions can be applied to a wide range of computational geometry problems.

3. **Algorithmic Problem Solving:**
   - **Edge Case Analysis:**
     The careful handling of edge cases (like powers of two or all-ones numbers) teaches you to always consider special conditions in problems, a vital skill in competitive programming.
   - **Combining Different Techniques:**
     This problem combines bit manipulation with geometric properties. Such interdisciplinary approaches are common in advanced algorithm problems where multiple strategies must be merged.

4. **Cryptography:**
   - **XOR Operations:**
     XOR is a fundamental operation in cryptography. Understanding its properties and how to manipulate bits efficiently can be directly applicable to designing and analyzing encryption algorithms.

---

## Time and Space Complexity Analysis

- **Time Complexity:**
  - The main loop processes each test case in \(O(1)\) time since all operations (bit shifting, XOR, OR) are constant time operations.
  - Given \(t\) test cases, the overall time complexity is \(O(t)\).

- **Space Complexity:**
  - The algorithm uses a constant amount of additional space (a few long variables and a StringBuilder for output).
  - Therefore, the space complexity is \(O(1)\).

---

## Final Summary

- **Problem Statement:**
  Determine if there exists a \(y\) (\(1 \le y < x\)) such that \(x\), \(y\), and \(x \oplus y\) can form a non-degenerate triangle. If such a \(y\) exists, output it; otherwise, output \(-1\).

- **Key Ideas and Techniques:**
  - **Bit Manipulation:** Creating masks, extracting set bits, and combining bits using OR and XOR.
  - **Triangle Inequality:** Ensuring that the chosen values form a valid triangle.
  - **Edge Case Handling:** Returning \(-1\) for powers of two or numbers with all bits set.

- **DSA Concepts Covered:**
  - Bit-level operations and mask creation.
  - Logical conditions for geometric validity (triangle inequalities).
  - Efficient edge case analysis.

- **Where Else to Apply:**
  - Problems involving subset XOR and bitmask dynamic programming.
  - Geometric problems requiring verification of triangle inequalities.
  - Cryptographic algorithms utilizing XOR and bit-level manipulations.
  - General optimization problems where combining multiple techniques is required.

- **Complexity:**
  The solution runs in \(O(t)\) time with \(O(1)\) space complexity.
