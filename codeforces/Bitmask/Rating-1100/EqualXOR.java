// IMP- Meet in the Middle (DAC), X^X = 0

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class EqualXOR {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {
            int n = fast.nextInt(), k = fast.nextInt();
            int nums[] = new int[n*2];          // Input array of double size
            for(int i = 0; i < n*2; i++)
                nums[i] = fast.nextInt();
            // Function call returns String Builder itself
            builder.append(findSubsequence(n, k, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder findSubsequence(int n, int k, int nums[]) {
        int firstHalf[] = new int[n+1], secondHalf[] = new int[n+1];
        // IMP- use counting sort logic to find the frequency of first n and next n elements separately
        for(int i = 0; i < 2*n; i++) {
            if(i < n)
                firstHalf[nums[i]]++;
            else    secondHalf[nums[i]]++;
        }
        Set<Integer> leftChosen = new HashSet<>(), rightChosen = new HashSet<>();
        int temp = k;
        // IMP- use x ^ x = 0, thus we find elements with frequency 2 and add them into the set to keep xor same (later to be processed)
        for(int i = 1; i <= n && k > 0; i++)
            if(firstHalf[i] == 2 && k > 0) {
                leftChosen.add(-i);
                k--;
            }
        k = temp;
        // IMP- use x ^ x = 0, thus we find elements with frequency 2 and add them into the set to keep xor same (later to be processed)
        for(int j = 1; j <= n && k > 0; j++)
            if(secondHalf[j] == 2 && k > 0) {
                rightChosen.add(-j);
                k--;
            }
        k *= 2;     // If some elements left, we update k to 2 times
        // Now we take elements which are present in both slots (have frequency of 1)
        for(int t = 1; Math.min(t, k) > 0; t++)
            if(firstHalf[t] == 1) {     // Adding in both the sequences
                leftChosen.add(t);
                rightChosen.add(t);
                k--;
            }
        return generateAndMeetSubsequence(leftChosen, rightChosen, n, nums);
    }

    public static StringBuilder generateAndMeetSubsequence(Set<Integer> firstSet, Set<Integer> secondSet, int n, int nums[]) {
        StringBuilder lSeq = new StringBuilder(), rSeq = new StringBuilder();
        for(int i = 0; i < n; i++) {        // Finding the number from left to right (generating the left subsequence)
            if(firstSet.contains(-nums[i])) {       // IMP- checking if character occurs twice (negative refers to being present twice)
                lSeq.append(nums[i]+" ");
                firstSet.remove(-nums[i]);
                firstSet.add(nums[i]);
            }
            else if(firstSet.contains(nums[i])) {   // IMP- checking if character occurs once
                lSeq.append(nums[i]+" ");
                firstSet.remove(nums[i]);
            }
        }
        for(int j = n; j < 2*n; j++) {          // Finding the number from left to right (generating the right subsequence)
            if(secondSet.contains(-nums[j])) {      // IMP- checking if character occurs twice (negative refers to being present twice)
                rSeq.append(nums[j]+" ");
                secondSet.remove(-nums[j]);
                secondSet.add(nums[j]);
            }
            else if(secondSet.contains(nums[j])) {      // IMP- checking if character occurs once
                rSeq.append(nums[j]+" ");
                secondSet.remove(nums[j]);
            }
        }
        // IMP- Merging the two subsequences as a string builder (meet in the middle)
        return lSeq.append("\n").append(rSeq);
    }
}
