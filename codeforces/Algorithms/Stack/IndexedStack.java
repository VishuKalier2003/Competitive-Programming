package Stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

public class IndexedStack {
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
        int nums[];
        FastReader fastReader = new FastReader();
        nums = new int[fastReader.nextInt()];
        for(int i = 0; i < nums.length; i++)
            nums[i] = fastReader.nextInt();
        System.out.println(Arrays.toString(nextGreaterElement(nums)));
    }

    public static int[] nextGreaterElement(int nums[]) {
        int result[] = new int[nums.length];
        Stack<Integer> indexStack = new Stack<>();
        Arrays.fill(result, -1);
        for(int i = 0; i < nums.length; i++) {
            while(!indexStack.isEmpty() && nums[i] > nums[indexStack.peek()])
                result[indexStack.pop()] = nums[i];
            indexStack.push(i);
        }
        return result;
    }

    public static int[] circularNextGreaterElement(int nums[]) {
        int result[] = new int[nums.length];
        Stack<Integer> indexStack = new Stack<>();
        Arrays.fill(result, -1);
        for(int i = 0; i < nums.length; i++) {
            while(!indexStack.isEmpty() && nums[i] > nums[indexStack.peek()])
                result[indexStack.pop()] = nums[i];
            indexStack.push(i);
        }
        for(int i = 0; i < nums.length; i++) {
            while(!indexStack.isEmpty() && nums[i] > nums[indexStack.peek()])
                result[indexStack.pop()] = nums[i];
            indexStack.push(i);
        }
        return result;
    }
}
