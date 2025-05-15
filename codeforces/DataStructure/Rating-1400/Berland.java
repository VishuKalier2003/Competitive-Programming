import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Berland {
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

    public static class University {        // Class to store the data for each University
        public List<Long> students;
        public int countOfStudents;
        public long prefix[];

        public University() {
            this.students = new ArrayList<>(); this.countOfStudents = 0;
        }

        public void updateCountOfStudents() {
            this.countOfStudents += 1;
        }

        public void postProcess() {
            students.add(0l);
            Collections.sort(students);
            // We will create this array of prefix after we have already sorted all the students of the given university
            this.prefix = new long[this.countOfStudents+1];     // doing +1 since we have added an extra 0 to ease indexing
            // Imp- This prefix sum will be used to remove the students which cannot be put in the team by subtracting the modulo index
            for(int i = 1; i <= this.countOfStudents; i++)
                prefix[i] = prefix[i-1] + students.get(i);
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            long skill[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            for(int i = 0; i < n; i++)
                skill[i] = fast.nextLong();
            builder.append(solve(n, nums, skill)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder solve(final int n, final int nums[], final long skill[]) {
        Map<Integer, University> universityMap = new HashMap<>();
        for(int i = 0; i < n; i++) {
            universityMap.putIfAbsent(nums[i], new University());
            universityMap.get(nums[i]).students.add(skill[i]);
            universityMap.get(nums[i]).updateCountOfStudents();
        }
        for(University university : universityMap.values())
            university.postProcess();
        final StringBuilder result = new StringBuilder();
        for(int k = 1; k <= n; k++) {
            long sum = 0l;
            Iterator<Map.Entry<Integer, University>> it = universityMap.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Integer, University> entry = it.next();
                University university = entry.getValue();
                final int lastIndex = university.countOfStudents;
                // Imp- We need to evaluate prefix sum only when the total students of a university are at least k, else no team can be made
                if(k <= lastIndex) {
                    final int modulo = university.countOfStudents % k;
                    sum += university.prefix[lastIndex] - university.prefix[modulo];
                } else
                    it.remove();
            }
            result.append(sum+" ");
        }
        return result;
    }
}
