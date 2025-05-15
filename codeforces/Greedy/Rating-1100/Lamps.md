## Problem Overview

You are given \(n\) lamps, each identified by an index from 1 to \(n\). Each lamp \(i\) has two parameters:
- \(a_i\): A threshold number.
- \(b_i\): Points you gain when you turn the lamp on.

Initially, all lamps are off. In one operation, you may turn on any lamp that is off. When you turn on a lamp \(i\), you earn \(b_i\) points. Let \(x\) be the number of lamps that are turned on (broken lamps do not count). **Immediately after each operation**, all lamps with \(a_i \le x\) (whether on or off) break. Broken lamps are no longer counted as turned on, but you still keep the points from turning them on.

You can perform an arbitrary number of operations. Your goal is to determine the maximum number of points you can collect.

---

## Input and Output

#### **Input Format**
- The first line contains an integer \(t\) (\(1 \le t \le 10^4\)) — the number of test cases.
- For each test case:
  - The first line contains an integer \(n\) (\(1 \le n \le 2 \times 10^5\)).
  - The next \(n\) lines each contain two integers \(a_i\) and \(b_i\) (\(1 \le a_i \le n\), \(1 \le b_i \le 10^9\)).

It is guaranteed that the sum of \(n\) over all test cases does not exceed \(2 \times 10^5\).

#### **Output Format**
For each test case, output one integer — the maximum number of points you can collect.

#### **Example**

**Input:**
```
4
4
2 2
1 6
1 10
1 13
5
3 4
3 1
2 5
3 2
3 3
6
1 2
3 4
1 4
3 4
3 5
2 3
1
1 1
```

**Output:**
```
15
14
20
1
```

*Explanation (Test Case 1):*
One way to get 15 points is:
1. Turn on lamp 4, earning 13 points. Now \(x=1\), so all lamps with \(a_i \le 1\) break (lamps 2, 3, and 4 break).
2. Only lamp 1 is available; turning it on gives 2 more points (now \(x=1\) again, but lamp 1 doesn't break because \(a_1=2\)).
Total points: \(13 + 2 = 15\).

---

## Code Explanation with Clear Comments

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Lamps {

    // FastReader class for efficient input reading.
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        // Constructor initializes BufferedReader.
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Returns the next token.
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

        // Reads next token as an integer.
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Reads next token as a long.
        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while (t-- > 0) {
            final int n = fast.nextInt();
            int a[] = new int[n];
            long b[] = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = fast.nextInt();
                b[i] = fast.nextLong();
            }
            builder.append(maxLampsScore(n, a, b)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * Computes the maximum possible score using the lamps operations.
     *
     * Approach:
     * 1. Group lamps by their threshold \(a_i\). For each group, maintain a max-heap (priority queue)
     *    of the \(b_i\) values (points).
     * 2. For each threshold value (key) in the map, you can only turn on at most 'key' lamps
     *    from that group (since turning them on increases the count \(x\), which may break lamps).
     * 3. Thus, for each key, poll from its max-heap up to 'key' times and add these points to the total score.
     *
     * @param n The number of lamps.
     * @param a Array containing the threshold \(a_i\) for each lamp.
     * @param b Array containing the points \(b_i\) for each lamp.
     * @return The maximum total points achievable.
     */
    public static long maxLampsScore(final int n, final int a[], final long b[]) {
        // Create a map to group lamps by threshold a[i].
        Map<Integer, PriorityQueue<Long>> heapMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            // If key a[i] does not exist, initialize a max-heap for it.
            if (!heapMap.containsKey(a[i]))
                heapMap.put(a[i], new PriorityQueue<Long>(Collections.reverseOrder()));
            // Add the point value b[i] to the corresponding group.
            heapMap.get(a[i]).add(b[i]);
        }
        long score = 0L;
        // Iterate through each group in the map.
        for (int key : heapMap.keySet()) {
            // The key value also represents the maximum number of lamps you can take from that group.
            int count = key;
            PriorityQueue<Long> heap = heapMap.get(key);
            // Poll the top values (highest points) from the heap, up to 'count' times.
            while (!heap.isEmpty() && count-- > 0)
                score += heap.poll();
        }
        return score;
    }
}
```

---

## Detailed Discussion and DSA Concepts

### **Core Concepts:**

1. **Grouping Using HashMap:**
   - **Concept:** Group elements by a key—in this case, by the threshold \(a_i\) for each lamp.
   - **Application:** Grouping is a common strategy to partition data into meaningful categories, making it easier to process subsets independently.

2. **Priority Queue (Heap) for Selecting Top Elements:**
   - **Concept:** For each group, maintain a max-heap (priority queue) to efficiently extract the highest point values \(b_i\).
   - **Application:** This is used when you need to select the best \(k\) items from a group. The heap allows efficient retrieval of the maximum element.

3. **Greedy Selection Under Constraints:**
   - **Concept:** For each group, you can only pick a limited number of items (up to the threshold value). Hence, you greedily pick the top items in each group.
   - **Application:** Greedy algorithms are useful when making locally optimal choices leads to a global optimum.

---

## **Where Else Can We Apply the Heap Map Technique?**

The technique of using a HashMap to group items combined with a heap (priority queue) to select the top elements is widely applicable in many problems. Here are some examples:

1. **Task Scheduling Problems:**
   - **Example:** *Job Sequencing with Deadlines*
     Group jobs by their deadlines and use a max-heap to select the most profitable jobs that fit into a given time slot.

2. **Team Formation and Resource Allocation:**
   - **Example:** *Two Teams Composing* (or similar team formation problems)
     You might group candidates by skill or other attributes and then use a heap to choose the best candidates from each group.

3. **Maximum Sum Subsequence Problems:**
   - **Example:** *Maximum Sum of k Elements in Each Category*
     When items are categorized (e.g., by type, region, or other features), a heap can be used to select the top scoring items from each category.

4. **Event or Meeting Scheduling:**
   - **Example:** *Meeting Room Allocation*
     Group meetings by starting times or durations and then use a heap to select meetings with the highest priority or profit under scheduling constraints.

5. **Competitive Programming Problems:**
   - **Example:** *CSES Problem Set – Concert Tickets*
     Although not identical, the idea of grouping by a key (ticket price) and using a heap to manage available tickets can be adapted to similar problems where selection order matters.

By mastering the use of HashMap plus heap (priority queue), you can tackle many problems where items are grouped by some attribute and you need to make the best selection from each group under certain constraints.

---

## Time and Space Complexity Analysis

- **Time Complexity:**
  - **Grouping:** \(O(n)\) per test case to build the HashMap.
  - **Heap Operations:** Each lamp is added to a heap in \(O(\log k)\) time (where \(k\) is the size of the heap), and each heap poll operation is \(O(\log k)\). Overall, across all groups, this is \(O(n \log n)\) in the worst-case.
  - **Overall:** \(O(n \log n)\) per test case. Given the total \(n \le 2 \times 10^5\) over all test cases, this is efficient.

- **Space Complexity:**
  - **HashMap and Heaps:** Requires \(O(n)\) space in the worst-case for storing groups and heap elements.
  - **Overall:** \(O(n)\) space per test case.

---

## Final Summary

- **Problem Statement:**
  Given \(n\) lamps with thresholds \(a_i\) and points \(b_i\), determine the maximum points you can collect by turning on lamps, with the twist that turning on a lamp may cause lamps with \(a_i \le x\) (current count) to break.

- **Key Techniques:**
  - **HashMap Grouping:** Group lamps by threshold \(a_i\).
  - **Priority Queue (Heap):** For each group, maintain a max-heap of point values \(b_i\) to extract the best candidates.
  - **Greedy Selection:** From each group, take up to \(a_i\) lamps (the maximum allowed by the threshold) by selecting the highest point values.

- **DSA Concepts Covered:**
  - Grouping data using a HashMap.
  - Selecting top elements efficiently with a priority queue (heap).
  - Greedy algorithms under constraints.

- **Where Else to Apply the Heap Map Technique:**
  - **Task Scheduling Problems:** (e.g., Job Sequencing with Deadlines)
  - **Team Formation / Resource Allocation:** (e.g., problems where candidates are grouped by skills and you select the top ones)
  - **Maximum Sum Subsequence or K-Selection Problems:** (e.g., selecting top k items in various categories)
  - **Event Scheduling and Meeting Room Allocation:** (grouping by time slots and choosing highest priority events)
  - **Competitive Programming Problems:** (e.g., Concert Tickets, or similar grouping and selection problems)

- **Complexity:**
  The solution runs in \(O(n \log n)\) time per test case and uses \(O(n)\) space.
