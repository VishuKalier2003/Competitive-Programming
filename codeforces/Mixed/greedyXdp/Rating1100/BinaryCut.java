import java.util.*;

public class BinaryCut {
    public static void main(String[] args) {
        String inputs[];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                inputs = new String[sc.nextInt()];
                for(int i = 0; i < inputs.length; i++)
                    inputs[i] = sc.next();
            }
            break input;
        } output: {
            Arrays.stream(inputs).map(BinaryCut::findCuts).forEach(System.out::println);
            break output;
        }
    }

    public static int findCuts(String s) {
        int n = s.length(), blocks = 0; boolean sub01 = false;
        for(int i = 0; i < n-1; i++) {
            if(s.charAt(i) != s.charAt(i+1))
                blocks++;
            if(s.substring(i, i+2).equals("01"))
                sub01 = true;
        }
        return sub01 ? blocks : blocks+1;
    }
}
