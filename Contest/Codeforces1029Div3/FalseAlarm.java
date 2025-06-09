import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FalseAlarm {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer st;
            // Read number of test cases
            int t = Integer.parseInt(br.readLine().trim());
            // Process each test case in a scalable and robust loop
            while (t-- > 0) {
                // Parse n (number of doors) and x (button duration in seconds)
                st = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                // Read the door states array
                st = new StringTokenizer(br.readLine());
                int firstClosed = -1;
                int lastClosed = -1;
                // Identify the first and last occurrence of a closed door (state == 1)
                for (int i = 1; i <= n; i++) {
                    int state = Integer.parseInt(st.nextToken());
                    if (state == 1) {
                        if (firstClosed == -1) {
                            firstClosed = i;
                        }
                        lastClosed = i;
                    }
                }
                // Compute the span that the button effect must cover
                int requiredSpan = lastClosed - firstClosed + 1;
                // Leverage corporate-grade logic: if the span is within x, traversal is feasible
                if (requiredSpan <= x) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            }
            // Close the buffered reader to release resources
        }
    }
}
