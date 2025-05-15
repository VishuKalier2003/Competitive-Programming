import java.util.*;

public class Tavas {
    public static void main(String[] args) {
        long number[] = new long[1];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                number[0] = sc.nextLong();
            }
            break input;
        } output: {
            Arrays.stream(number).map(Tavas::findLuckyIndex).forEach(System.out::println);
            break output;
        }
    }

    public static long findLuckyIndex(long num) {
        String n = String.valueOf(num);
        int length = n.length();        // Calculate total lucky numbers with fewer digits
        long count = 0;
        for (int i = 1; i < length; i++)
            count += (long) Math.pow(2, i);     // Convert the lucky number to binary representation
        StringBuilder binary = new StringBuilder();
        for (char c : n.toCharArray())
            binary.append(c == '4' ? '0' : '1');        // Add the position within the current length
        count += Long.parseLong(binary.toString(), 2) + 1;
        return count;
    }
}
