package BinarySearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class RotatedSorted {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try{tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static void main(String[] args) {
        input: {
            break input;
        } output: {
            break output;
        }
    }

    public static int searchRotatedSorted(int nums[], int target) {
        int low = 0, high = nums.length-1;
        while(low <= high) {
            int mid = (low+high) >> 1;
            if(nums[mid] == target)
                return mid;
            if(nums[low] <= nums[mid]) {
                if(target >= nums[low] && target < nums[mid])
                    high = mid-1;
                else    low = mid+1;
            } else {
                if(target >= nums[mid] && target < nums[high])
                    low = mid+1;
                else    high = mid-1;
            }
        }
        return -1;
    }
}
