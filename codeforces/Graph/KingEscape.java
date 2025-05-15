/*
 * Ques - King Escape - https://codeforces.com/problemset/problem/1033/A
 * Tags - Graph, Math
 * Rating - 1000
 */

import java.util.Scanner;

public class KingEscape {
    public static void main(String[] args) {
        String q, k, t;
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            sc.nextLine();
            q = sc.nextLine(); k = sc.nextLine(); t = sc.nextLine();
            sc.close();
            break input;
        }
        variables : {   // Variables assignment block...
            int q1 = Integer.parseInt(q.split(" ")[0]);
            int q2 = Integer.parseInt(q.split(" ")[1]);
            int k1 = Integer.parseInt(k.split(" ")[0]);
            int k2 = Integer.parseInt(k.split(" ")[1]);
            int t1 = Integer.parseInt(t.split(" ")[0]);
            int t2 = Integer.parseInt(t.split(" ")[1]);
            System.out.println(canKingReachTarget(q1, q2, k1, k2, t1, t2) ? "YES" : "NO");  // Call...
            break variables;
        }
    }

    public static boolean canKingReachTarget(int q1, int q2, int k1, int k2, int t1, int t2) {
        // Function call for getting quadrants...
        int kingQuadrant = getQuadrant(q1, q2, k1, k2), targetQuadrant = getQuadrant(q1, q2, t1, t2);
        if(kingQuadrant == targetQuadrant && kingQuadrant != -1)    // If King is not currently checked...
            return true;
        return false;
    }

    public static int getQuadrant(int q1, int q2, int a1, int a2) {
        // Quadrant Evaluation code...
        if(a1 < q1 && a2 < q2)  return 4;
        if(a1 > q1 && a2 < q2)  return 1;
        if(a1 > q1 && a2 > q2)  return 2;
        if(a1 < q1 && a2 > q2)  return 3;
        return -1;
    }
}


/*

Test case 1-
8
4 4
1 3
3 1
Output 1-
YES

Test case 2-
8
4 4
2 3
1 6
Output 2-
NO

Test case 3-
8
3 5
1 2
6 1
Output 3-
NO

 */
