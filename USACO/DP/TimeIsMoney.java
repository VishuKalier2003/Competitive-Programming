
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TimeIsMoney {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {this.buffer = new BufferedReader(new FileReader("time.in"));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static final int MAX_TIME = 1000;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final PrintWriter printer = new PrintWriter(new FileWriter("time.out"));
        final int n = fast.nextInt(), m = fast.nextInt(), c = fast.nextInt();
        int moonies[] = new int[n];
        for(int i = 0; i < n; i++)
            moonies[i] = fast.nextInt();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for(int i = 0; i < n; i++)
            graph.put(i, new ArrayList<>());
        for(int i = 0; i < m; i++)
            graph.get(fast.nextInt()-1).add(fast.nextInt()-1);
        printer.print(solve(n, m, c, moonies, graph));
        printer.close();
    }

    public static int solve(final int n, final int m, final int c, final int moonie[], final Map<Integer, List<Integer>> graph) {
        // Imp- Since we are traveling time first, we are checking for each time, then for each city, so our dp will store time as first and city as second field
        int dp[][] = new int[MAX_TIME][n];
        for(int row[] : dp)     // Fill all with -1 as unreached
            Arrays.fill(row, -1);
        dp[0][0] = 0;       // start from city 1
        int maxMoonie = 0;
        for(int t = 0; t < MAX_TIME; t++) {     // Traverse for each time, since 1000 is the max time allowed (1000t - t^2)
            // Imp- For each city at a given time, since each city is checked the value of city 0 will be updated everytime
            for(int city = 0; city < n; city++) {
                if(dp[t][city] == -1)       // If the city is unreachable at that time, we skip it
                    continue;
                // We check for each neighbor of the city
                for(int neighbor : graph.get(city)) {
                    if(t+1 < MAX_TIME)
                        // Imp- Update the neighbor as moonie get from reaching the next time unit, from current time unit at current city
                        dp[t+1][neighbor] = Math.max(dp[t+1][neighbor], dp[t][city] + moonie[neighbor]);
                }
            }
            maxMoonie = Math.max(maxMoonie, dp[t][0]-c*t*t);    // Update if city 1 is reached at current time
        }
        return maxMoonie;
    }
}
