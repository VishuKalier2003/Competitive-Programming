/*
 * Ques - Friends - https://codeforces.com/problemset/problem/94/B
 */

import java.util.*;

public class Friends {
    public static void main(String[] args) {
        Set<String> relations = new HashSet<String>();
        input: {
            Scanner sc = new Scanner(System.in);
            int m = sc.nextInt();
            sc.nextLine();
            for(int i = 0; i < m; i++) {
                String relation = sc.nextLine(), relationReverse = new StringBuilder(relation).reverse().toString();
                relations.add(relation);
                relations.add(relationReverse);
            }
            sc.close();
            break input;
        }
        System.out.println(findAcquaintances(relations) ? "WIN" : "FAIL");
    }

    public static boolean findAcquaintances(Set<String> set) {
        for(int i = 1; i <= 5; i++) {
            int no = 0, yes = 0;
            for(int j = 1; j <= 5; j++) {
                if(i == j)  continue;
                if(set.contains(i+" "+j))   yes++;
                else    no++;
            }
            if(no == 3 || yes == 3)
                return true;
        }
        return false;
    }
}
