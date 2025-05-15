import java.util.*;

public class QuesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        outer:
        while (t-- > 0) {
            int n = sc.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = sc.nextInt();

            if (n == 1) {
                System.out.println("YES");
                continue;
            }

            int k = (n + 1) / 2; // index of median

            for (int flip : new int[]{1, -1}) {
                int x = a[0] * flip; // Try both a[0] and -a[0]
                int lessEq = 0, greaterEq = 0, flex = 0;

                for (int i = 1; i < n; i++) {
                    int abs = Math.abs(a[i]);
                    if (abs <= x) lessEq++;
                    else if (-abs >= x) greaterEq++;
                    else flex++;
                }

                int needLow = Math.max(0, k - 1 - lessEq);
                int needHigh = Math.max(0, k - 1 - greaterEq);

                if (needLow + needHigh <= flex) {
                    System.out.println("YES");
                    continue outer;
                }
            }

            System.out.println("NO");
        }
    }
}
