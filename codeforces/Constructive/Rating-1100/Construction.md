## Problem Overview

You are given two integers \( n \) and \( k \). The goal is to construct a sequence of \( n \) non-negative integers \( a_1, a_2, \dots, a_n \) such that:

\[
\sum_{i=1}^{n} a_i = k
\]

Additionally, you need to maximize the number of `1`s in the binary representation of the bitwise OR of the entire sequence:

\[
a_1 \,|\, a_2 \,|\, \dots \,|\, a_n
\]

In simple terms, you must distribute \( k \) among \( n \) numbers in a way that not only their sum equals \( k \) but also the overall bitwise OR (when combined) has as many set bits as possible.

---

## Input and Output

#### **Input Format**
- The first line contains an integer \( t \) — the number of test cases.
- Each test case consists of a single line with two integers:
  - \( n \) — number of integers in the sequence.
  - \( k \) — the total sum the sequence must achieve.

#### **Output Format**
For each test case, output a sequence of \( n \) non-negative integers that meet the above conditions.

#### **Example**

**Input:**
```
4
1 5
2 3
2 5
6 51
```

**Output:**
```
5
1 2
5 0
3 1 1 32 2 12
```

**Explanation:**
- **Test Case 1:** With \( n = 1 \), the sequence is \([5]\).
- **Test Case 2:** For \( n = 2 \) and \( k = 3 \), one valid sequence is \([1, 2]\). Here, \(1|2 = 3\) (binary `11`) which has two set bits.
- **Test Case 4:** For \( n = 6 \) and \( k = 51 \), one valid sequence is \([3, 1, 1, 32, 2, 12]\) with the OR value having five set bits.

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Construction {

    // FastReader class to efficiently handle input reading.
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor initializes the BufferedReader.
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Reads the next token from input.
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

        // Parses the next token as an integer.
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Parses the next token as a long integer.
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();  // Number of test cases.
        final StringBuilder builder = new StringBuilder();

        // Process each test case.
        while(tests-- > 0) {
            int n = fast.nextInt();      // Number of elements in the sequence.
            long k = fast.nextLong();      // Required sum of the sequence.
            // Append the result of constructing the sequence with maximum set bits.
            builder.append(maxBitConstruction(n, k)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Constructs a sequence of n non-negative integers that sums to k and maximizes
     * the number of 1s in the bitwise OR of all the numbers.
     *
     * Strategy:
     * - For n == 1, the only valid sequence is [k].
     * - For n >= 2, use one number that maximizes set bits: (2^x - 1).
     *   This number has x set bits, where x is the largest exponent such that 2^x <= k.
     * - Use a second number to complete the sum: k - (2^x - 1).
     * - The remaining numbers (if any) are set to 0, as they do not affect the bitwise OR.
     *
     * @param n Number of elements in the sequence.
     * @param k Required sum of the sequence.
     * @return A string representing the sequence with spaces between numbers.
     */
    public static String maxBitConstruction(int n, long k) {
        // When n is 1, the only possible sequence is [k].
        if(n == 1)
            return k + " " + "0 ".repeat(n - 1);

        int x = 0;  // Determine the highest power exponent such that 2^x <= k.
        for(int i = 0; i < 64; i++)
            if(exp(2, i) > k) {  // As soon as 2^i exceeds k, stop.
                x = i - 1;  // x is the maximum exponent with 2^x <= k.
                break;
            }

        StringBuilder array = new StringBuilder();
        // Use one number as (2^x - 1) which has x set bits.
        array.append((exp(2, x) - 1) + " ");
        // Use another number to make the total sum equal to k.
        array.append((k - (exp(2, x) - 1)) + " ");
        n -= 2;  // Two numbers are used.

        // Fill the rest with zeros (which do not affect the bitwise OR).
        while(n-- > 0)
            array.append("0 ");
        return array.toString();
    }

    /**
     * Computes a^b using fast exponentiation.
     * This function uses binary exponentiation which runs in O(log b) time.
     *
     * @param a The base.
     * @param b The exponent.
     * @return The value of a raised to the power b.
     */
    public static long exp(int a, int b) {
        long result = 1L;
        while(b > 0) {
            // If the lowest bit of b is set, multiply result by a.
            if((b & 1) == 1)
                result = result * a;
            a = a * a;    // Square the base.
            b >>= 1;      // Shift b to process the next bit.
        }
        return result;
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Bit Manipulation:**
   - **Concept:** The solution uses the property that \(2^x - 1\) has \(x\) set bits (all lower \(x\) bits are 1).
   - **Application:** This concept is used in many problems where maximizing or controlling the bits in a number is necessary (e.g., bitmasking in dynamic programming, setting/clearing bits in algorithms).

2. **Greedy Algorithms:**
   - **Concept:** The strategy greedily selects the largest number with maximum set bits under the constraint \(2^x \leq k\). This local optimum (maximizing set bits) helps achieve a global goal.
   - **Application:** Greedy methods are common in resource allocation problems, scheduling, and optimization problems where making the best local choice leads to an optimal solution.

3. **Fast Exponentiation:**
   - **Concept:** The `exp` function implements binary exponentiation which calculates powers in \(O(\log b)\) time.
   - **Application:** This technique is widely used in modular arithmetic, cryptographic computations, and any scenario requiring efficient power calculations.

4. **Sequence Construction Under Constraints:**
   - **Concept:** Constructing sequences to meet both a sum constraint and an optimization criterion (maximizing bitwise OR) is a common theme in competitive programming.
   - **Application:** This approach is applicable in problems where you need to distribute resources (e.g., sum constraints) while optimizing a secondary characteristic (e.g., maximizing some bit property).

---

## **Where Else Can We Apply This Logic and Concepts?**

1. **Optimization Problems with Bitwise Operations:**
   - Many problems require you to **maximize or minimize bitwise properties** (e.g., maximum XOR subarray, bitmask DP problems). The idea of choosing numbers with specific bit patterns is a powerful technique.

2. **Resource Allocation:**
   - The logic of distributing a fixed sum among several elements while optimizing a secondary objective is common in **resource allocation problems**, where you must distribute resources under a budget while optimizing overall utility.

3. **Cryptography:**
   - Fast exponentiation is a staple in cryptographic algorithms (such as RSA). Understanding binary exponentiation aids in designing and analyzing algorithms where exponentiation under a modulus is required.

4. **Dynamic Programming with Bitmasking:**
   - When using **bitmasking in DP**, the manipulation and understanding of binary representations (like \(2^x - 1\)) can simplify the handling of state transitions and optimize computations.

5. **Compiler Design and Data Compression:**
   - Techniques to manipulate bits efficiently are also crucial in **compiler optimizations** and **data compression algorithms**, where bit-level operations are used to optimize performance and space.

---

## **Time and Space Complexity Analysis**

- **Time Complexity:**
  - **Finding \(x\):** The loop runs up to 64 iterations, which is \(O(1)\) with respect to \(k\).
  - **Sequence Construction:** Appending numbers to the `StringBuilder` takes \(O(n)\) per test case.
  - **Fast Exponentiation:** The `exp` function runs in \(O(\log b)\), but here \(b \leq 64\), so it is effectively constant time.

  **Overall:** \(O(n)\) per test case, and for \(t\) test cases, the total complexity is \(O\left(\sum n\right)\).

- **Space Complexity:**
  - The space is mainly used by the output string which stores \(n\) numbers, so it is \(O(n)\) per test case.
  - Additional auxiliary space is negligible compared to the sequence storage.

---

## **Final Summary**

- **Problem Statement:** Construct a sequence of \( n \) non-negative integers that sum to \( k \) while maximizing the number of set bits in the bitwise OR of the sequence.
- **Key Insight:** Use \( (2^x - 1) \) to maximize set bits where \( x \) is the largest exponent such that \( 2^x \leq k \), and adjust the rest of the sequence accordingly.
- **DSA Concepts Covered:**
  - **Bit Manipulation:** Maximizing set bits using binary properties.
  - **Greedy Algorithms:** Selecting the best local option to optimize global outcomes.
  - **Fast Exponentiation:** Efficiently computing powers.
  - **Sequence Construction Under Constraints:** Balancing multiple requirements in a solution.
- **Where Else to Apply:**
  - Optimization problems involving bitwise operations.
  - Resource allocation and scheduling.
  - Cryptography and dynamic programming with bitmasking.
  - Compiler design and data compression.
- **Complexity:** The approach runs in \(O(n)\) per test case with \(O(n)\) space complexity.
