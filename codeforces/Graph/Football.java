/*
 * Ques - Football - https://codeforces.com/problemset/problem/417/C
 * Tags - Graph, Math
 * Rating - 1400
 */

import java.util.*;

public class Football {
    public static void main(String[] args) {
        int n, k;
        input: {
            Scanner sc = new Scanner(System.in);
            String s[] = sc.nextLine().split(" ");
            n = Integer.parseInt(s[0]); k = Integer.parseInt(s[1]);
            sc.close();
            break input;
        }
        generateGames(n, k);
    }

    public static void generateGames(int n, int k) {
        List<String> gamesPlayed = new ArrayList<String>();
        int i = 0, j = 1, closeness = 1;
        Map<Integer, Set<Integer>> games = new HashMap<>();
        for(int t = 1; t <= n; t++)
            games.put(t, new HashSet<Integer>());
        while(i != n*k) {
            if(j > n) {
                j = 1;
                closeness++;
            }
            int team1 = j, team2 = j+closeness > n ? j+closeness-n : j+closeness;
            boolean query1 = games.get(team1).add(team2), query2 = games.get(team2).add(team1);
            if(!query1 || !query2)
                break;
            gamesPlayed.add(team1+" "+team2);
            i++; j++;
        }
        if(i != n*k) {
            System.out.println(-1);
            return;
        }
        System.out.println(n*k);
        for(String game : gamesPlayed)
            System.out.println(game);
        return;
    }
}
