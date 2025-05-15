import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashSet;

public class Common {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        int t = Integer.parseInt(br.readLine().trim());
        while (t-- > 0) {
            int n = Integer.parseInt(br.readLine().trim());
            StringTokenizer st = new StringTokenizer(br.readLine());
            HashSet<Integer> distinct = new HashSet<>();
            for (int i = 0; i < n; i++) {
                distinct.add(Integer.parseInt(st.nextToken()));
            }
            // The maximum beautiful subsequence is just all distinct values.
            out.println(distinct.size());
        }
        out.flush();
    }
}
