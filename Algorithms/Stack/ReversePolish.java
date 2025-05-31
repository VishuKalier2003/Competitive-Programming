package Stack;

import java.util.*;
import java.io.*;

public class ReversePolish {
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
    }

    public static void main(String args[]) {
        String text;
        input: {
            FastReader fastReader = new FastReader();
            text = fastReader.next();
            break input;
        } output: {
            System.out.println(reversePolishNotation(text));
            break output;
        }
    }

    public static long reversePolishNotation(String text) {
        String tokens[] = extractNotation(text);
        Stack<Integer> stack = new Stack<>();
        for(String token : tokens) {
            System.out.println(token);
            if(token.equals("+") || token.equals("*") || token.equals("-") || token.equals("/")) {
                switch(token) {
                    case "*" -> stack.push(stack.pop()*stack.pop());
                    case "+" -> stack.push(stack.pop()+stack.pop());
                    case "/" -> stack.push(stack.pop()/stack.pop());
                    case "-" -> stack.push(stack.pop()-stack.pop());
                }
            }
            else    stack.push(Integer.parseInt(token));
        }
        return stack.peek();
    }

    public static String[] extractNotation(String text) {
        List<String> tokens = new ArrayList<>();
        int n = text.length();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < n; i++) {
            char ch = text.charAt(i);
            if(ch == '*' || ch == '+' || ch == '-' || ch == '/' || ch == ' ') {
                tokens.add(builder.toString());
                tokens.add(""+ch);
                builder.setLength(0);
            }
            else    builder.append(ch);
        }
        return tokens.stream().toArray(String[]::new);
    }
}
