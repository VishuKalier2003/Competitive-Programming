import java.util.*;

public class Monsters {
    public record Game(int n, long k, long[] health, int[] pos, int[] max) {}
    public static void main(String[] args) {
        Game games[];
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                games = new Game[sc.nextInt()];
                for(int i = 0; i < games.length; i++) {
                    int n = sc.nextInt();
                    games[i] = new Game(n, sc.nextLong(), new long[n], new int[n], new int[]{0});
                    for(int j = 0; j < n; j++)
                        games[i].health[j] = sc.nextLong();
                    for(int j = 0; j < n; j++) {
                        games[i].pos[j] = sc.nextInt();
                        games[i].max[0] = Math.max(games[i].max[0], Math.abs(games[i].pos[j]));
                    }
                }
            }
            break input;
        } output: {
            Arrays.stream(games).map(game -> canWeSurvive(game.k, game.health, game.pos, game.max[0])).forEach(System.out::println);
            break output;
        }
    }

    public static String canWeSurvive(long k, long health[], int pos[], int max) {
        Map<Integer, Long> monster = new HashMap<>();
        for(int i = 0;i < health.length; i++)
            monster.put(pos[i], monster.getOrDefault(pos[i], 0L)+health[i]);
        if(monster.containsKey(0))
            return "NO";
        long bullets = 0, left = 0;
        for(int i = 1; i <= max; i++) {
            bullets = k+left;
            long monsterHealth = (monster.containsKey(i) ? monster.get(i) : 0) + (monster.containsKey(-i) ? monster.get(-i) : 0);
            if(monsterHealth > bullets)
                return "NO";
            else left = bullets - monsterHealth;
        }
        return "YES";
    }
}
