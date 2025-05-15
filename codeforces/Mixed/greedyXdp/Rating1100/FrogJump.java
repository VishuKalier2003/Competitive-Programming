import java.util.*;

public class FrogJump {
    public static void main(String[] args) {
        String frogs[];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                frogs = new String[sc.nextInt()];
                for(int i = 0; i < frogs.length; i++)
                    frogs[i] = sc.next();
            }
            break input;
        } output: {
            Arrays.stream(frogs).map(FrogJump::minFrogJump).forEach(System.out::println);
            break output;
        }
    }

    public static int minFrogJump(String s) {
        int dist = Integer.MIN_VALUE;
        List<Integer> Rindices = new ArrayList<>();
        Rindices.add(0);
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) == 'R')
                Rindices.add(i+1);
        Rindices.add(s.length()+1);
        for(int i = 1; i < Rindices.size(); i++)
            dist = Math.max(dist, Rindices.get(i)-Rindices.get(i-1));
        return dist;
    }
}
