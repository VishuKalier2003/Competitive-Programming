package Stack;

import java.util.*;
import java.io.*;

public class ValidStack {
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

    public static void main(String args[]) {
        String parenthesis;
        input: {
            FastReader fastReader = new FastReader();
            parenthesis = fastReader.next();
            break input;
        } output: {
            System.out.println(validParenthesis(parenthesis) ? "YES" : "NO");
            break output;
        }
    }

    public static boolean validParenthesis(String s) {
        Stack<Character> stack = new Stack<>();
        for(char ch : s.toCharArray()) {
            if(ch == '(' || ch == '[' || ch == '{')
                stack.push(ch);
            else if(ch == ')' || ch == ']' || ch == '}') {
                if(stack.isEmpty())
                    return false;
                char top = stack.pop();
                if((ch == ')' && top != '(') || (ch == ']' && top != '[') || (ch == '}' && ch =='{'))
                    return false;
            }
        }
        return true;
    }
}
