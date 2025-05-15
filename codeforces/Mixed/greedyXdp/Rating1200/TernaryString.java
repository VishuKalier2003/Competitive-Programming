import java.util.*;

public class TernaryString {
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
            Arrays.stream(inputs).map(TernaryString::minCollapseWindow).forEach(System.out::println);
            break output;
        }
    }

    public static int minCollapseWindow(String s) {
        int left = 3, right = s.length();
        while(left <= right) {
            int mid = left + (right-left)/2;
            if(ternaryWindowFound(mid, s))
                right = mid-1;
            else    left = mid+1;
        }
        return left > s.length() ? 0 : left;
    }

    public static boolean ternaryWindowFound(int window, String s) {
        int flags[] = new int[3];
        for(int i = 0; i < window; i++)
            flags[s.charAt(i)-'1']++;
        if(flags[0] > 0 && flags[1] > 0 && flags[2] > 0)
            return true;
        for(int i = window; i < s.length(); i++) {
            flags[s.charAt(i-window)-'1']--;
            flags[s.charAt(i)-'1']++;
            if(flags[0] > 0 && flags[1] > 0 && flags[2] > 0)
                return true;
        }
        return false;
    }
}
