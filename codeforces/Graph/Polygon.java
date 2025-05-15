/*
 * Ques - Polygon - https://codeforces.com/problemset/problem/1360/E
 * Tags - Graph, Math, Matrix DP
 * Rating - 1200
 */

import java.util.Scanner;

public class Polygon {
    // class defined using record having final parameters...
    public record Training(int n, char[][] polygon) {}
    public static void main(String[] args) {
        Training trainings[];   // Test cases array...
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            trainings = new Training[t];
            for(int i = 0; i < t; i++) {
                int n = sc.nextInt();   // Taking input...
                trainings[i] = new Training(n, getPolygon(n, sc));
            }
            sc.close();
            break input;
        }
        output: {   // Output segment...
            for(Training training : trainings)
                System.out.println(didArmyTrained(training.n, training.polygon) ? "YES" : "NO");
            break output;
        }
    }

    public static char[][] getPolygon(int n, Scanner sc) {  // Using the entered string into matrix...
        char input[][] = new char[n][n];
        for(int i = 0; i < n; i++) {
            String s = sc.next();
            for(int j = 0; j < n; j++)
                input[i][j] = s.charAt(j);
        }
        return input;   // Returning the matrix of characters...
    }

    public static boolean didArmyTrained(int n, char polygon[][]) {
        // DP approach...
        for(int i = n-2; i >= 0; i--)
            for(int j = n-2; j >= 0; j--)
                // Checking if both bottom and right cells are not 1...
                if(polygon[i][j] == '1' && (polygon[i+1][j] == '0' && polygon[i][j+1] == '0'))
                    return false;   // The the current state of polygon is not possible...
        return true;
    }
}
