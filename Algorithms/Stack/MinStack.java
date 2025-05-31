package Stack;

import java.util.*;
import java.io.*;

public class MinStack {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public Stack<Integer> minStack, stack;

    public MinStack() {
        minStack = new Stack<>(); stack = new Stack<>();
    }

    public void push(int x) {
        stack.push(x);
        if(minStack.isEmpty() || x <= minStack.peek())
            minStack.push(x);
        return;
    }

    public void pop() {
        stack.pop();
        if(!minStack.isEmpty())
            minStack.pop();
        return;
    }

    public int peekMin() {
        return minStack.peek();
    }

    public int top() {
        return stack.peek();
    }

    public static void main(String[] args) {
        input: {
            break input;
        } output: {
            break output;
        }
    }
}
