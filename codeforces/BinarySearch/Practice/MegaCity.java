import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class MegaCity {
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
        final int n = fast.nextInt(), people = 1000000 - fast.nextInt();    // Extra people needed
        List<double[]> coordinates = new ArrayList<>();     // List to store coordinates
        int totalPopulation = 0;
        for(int i = 0; i < n; i++) {
            int x2 = fast.nextInt(), y2 = fast.nextInt();
            double distance = Math.sqrt((x2*x2)+(y2*y2));       // IMP- Euclidean distance formula
            coordinates.add(new double[]{distance, fast.nextInt()});        // Candidates added
            totalPopulation += coordinates.get(i)[1];       // Finding total population available to add
        }
        System.out.print(createMegaCity(n, people, coordinates, totalPopulation));
    }

    public static String createMegaCity(final int n, final int people, List<double[]> coordinates, int totalPopulation) {
        if(totalPopulation < people)        // If the available population is not sufficient
            return "-1";
        Collections.sort(coordinates, new Comparator<double[]>() {
            @Override       // IMP- Overriding the comparator function
            public int compare(double node1[], double node2[]) {
                return Double.compare(node1[0], node2[0]);
            }
        });
        int prefixSum[] = new int[n];       // Computing prefix sum for each index
        // IMP- Since we want to find, at ith index people we have added, then instead of traversing again and again we can use prefix sums
        prefixSum[0] = (int)coordinates.get(0)[1];
        for(int i = 1; i < n; i++)
            prefixSum[i] = prefixSum[i-1] + (int)coordinates.get(i)[1];
        int index = binarySearchLowerBound(n, prefixSum, people);       // Binary searching the smallest index where required people are met
        return String.format("%.7f", coordinates.get(index)[0]);
    }

    public static int binarySearchLowerBound(final int n, final int nums[], final int people) {
        int left = 0, right = n-1, ans = 0;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);         // mid element
            if(nums[mid] >= people) {       // IMP- Computing lower bound (largest index that meets the criteria)
                ans = mid;
                right = mid-1;
            } else
                left = mid+1;
        }
        return ans;
    }
}
