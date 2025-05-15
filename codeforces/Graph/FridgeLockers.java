/*
 * Ques - Fridge Lockers - https://codeforces.com/problemset/problem/1255/B
 * Tags - Graph, Greedy, Math
 * Rating - 1200
 */

import java.util.Scanner;

public class FridgeLockers {
    // Record class defined for test cases with final vairables...
    private record Fridge(int n, int chains, int[] cost) {}
    public static void main(String[] args) {
        Fridge fridges[];
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            sc.nextLine();
            fridges = new Fridge[t];
            for(int i = 0; i < t; i++) {
                int arr[] = convertToArray(sc.nextLine());
                fridges[i] = new Fridge(arr[0], arr[1], updateCosts(arr[0], sc.nextLine()));
            }
            sc.close();
            break input;
        }
        output: {   // Output block...
            for(Fridge fridge : fridges) {
                if(fridge.n > fridge.chains || fridge.n == 2)    // If there are fewer than n chains...
                    System.out.println("-1");
                else        // Otherwise print the required output...
                    System.out.println(minCostSteelChains(fridge.n, fridge.chains, fridge.cost));
            }
            break output;
        }
    }

    private static int[] convertToArray(String s) {     // Function to convert to 2 length array...
        return new int[]{Integer.parseInt(s.split(" ")[0]), Integer.parseInt(s.split(" ")[1])};
    }

    private static int[] updateCosts(int n, String s) {     // Function to convert the the nums...
        int cost[] = new int[n+1]; String str[] = s.split(" ");
        for(int i = 0; i < n; i++)
            cost[i] = Integer.parseInt(str[i]);
        cost[n] = cost[0];      // An extra value is placed, to create the cycle...
        return cost;
    }

    private static String minCostSteelChains(int n, int chains, int[] cost) {
        int extraChains = chains-n;     // Finding the extra chains...
        int minCost = Integer.MAX_VALUE, sum = 0, idx = 0;
        oneCycle: {     // Code logic...
            for(int i = 0; i < cost.length-1; i++) {
                sum += (cost[i]+cost[i+1]);     // Sum of cost cycle...
                if(minCost > cost[i]+cost[i+1]) {
                    minCost = Math.min(minCost, cost[i]+cost[i+1]);
                    idx = i;    // Updating index for accessing the min fridge chain...
                }
            }
            break oneCycle;
        }
        if(extraChains > 0)     // If there are extra chains...
            sum += (extraChains * minCost); // Place all chains on minimum cost fridge...
        System.out.println(sum);
        edgePrinting: {     // Printing the edges...
            System.out.println(1+" "+n);
            for(int i = 0; i < n-1; i++)
                System.out.println((i+1)+" "+(i+2));
            for(int i = 0; i < extraChains; i++)
                System.out.println((idx+1)+" "+(idx+2));
            break edgePrinting;
        }
        return "";
    }
}
