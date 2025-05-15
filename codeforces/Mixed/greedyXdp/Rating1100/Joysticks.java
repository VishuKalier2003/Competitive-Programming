import java.util.*;

public class Joysticks {
    public record Stick(int a1, int a2) {}
    public static void main(String[] args) {
        Stick stick;
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                int a = sc.nextInt();
                stick = new Stick(a, sc.nextInt());
            }
            break input;
        } output: {
            System.out.println(maxPlayTime(stick));
            break output;
        }
    }

    public static Set<String> visited = new HashSet<>();

    public static int maxPlayTime(Stick start) {
        if(start.a1 == 1 && start.a2 == 1)
            return 0;
        Queue<Stick> queue = new LinkedList<>();
        queue.add(start);
        int maxTime = 0, time = 0;
        visited.add(start.a1+" "+start.a2);
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                Stick node = queue.poll();
                int a1 = node.a1, a2 = node.a2;
                if(a1 <= 0 || a2 <= 0)
                    maxTime = Math.max(maxTime, time);
                else {
                    if(!visited.contains((a1-2)+" "+(a2+1))) {
                        queue.add(new Stick(a1-2, a2+1));
                        visited.add((a1-2)+" "+(a2+1));
                    }
                    if(!visited.contains((a1+1)+" "+(a2-2))) {
                        queue.add(new Stick(a1+1, a2-2));
                        visited.add((a1+1)+" "+(a2-2));
                    }
                }
            }
            time++;
        }
        return maxTime;
    }
}
