import java.util.*;

public class Network {
    public static void main(String[] args) {
        int n, m;
        int degree[];
        input: {
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            m = sc.nextInt();
            sc.nextLine();
            degree = new int[n+1];
            for(int i = 0; i < m; i++) {
                String str[] = sc.nextLine().split(" ");
                degree[Integer.parseInt(str[0])]++;
                degree[Integer.parseInt(str[1])]++;
            }
            sc.close();
            break input;
        }
        System.out.println(findTopology(degree, n));
    }

    public static String findTopology(int degrees[], int n) {
        int count = 0, onlyTwos = 0;
        for(int i = 1; i < degrees.length; i++) {
            onlyTwos += degrees[i] == 2 ? 1 : 0;
            count += degrees[i] == 1 ? 1 : 0;
        }
        if(count == 2 && onlyTwos == n-2)  return "bus topology";
        boolean allTwo = true;
        for(int i = 1; i < degrees.length; i++)
            if(degrees[i] != 2)
                allTwo = false;
        if(allTwo)     return "ring topology";
        int maxDegree = 0, nonOnes = 0;
        for(int i = 1; i < degrees.length; i++) {
            nonOnes += degrees[i] != 1 ? 1 : 0;
            maxDegree = Math.max(maxDegree, degrees[i]);
        }
        if(maxDegree == n-1 && nonOnes == 1)    return "star topology";
        return "unknown topology";
    }
}
