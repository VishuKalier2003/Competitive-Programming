import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Incinerate {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static class Monster {
        public long health, power;
        public Monster(long health, long power) {this.health = health; this.power = power;}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            long health[] = new long[n], power[] = new long[n];
            for(int i = 0; i < n; i++)
                health[i] = fast.nextLong();
            for(int i = 0; i < n; i++)
                power[i] = fast.nextLong();
            builder.append(canKill(n, k, health, power)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder canKill(final int n, int k, final long health[], final long power[]) {
        Monster monsters[] = new Monster[n];        // Create monsters array
        for(int i = 0; i < n; i++)
            monsters[i] = new Monster(health[i], power[i]);     // Fill the array
        Arrays.sort(monsters, new Comparator<Monster>() {
            @Override       // Override comparison
            public int compare(Monster m1, Monster m2) {
                return Long.compare(m1.health, m2.health);  // IMP- sorting by health in ascending order
            }
        });
        long minPower[] = new long[n];      // Suffix array to find the min power of the alive monster
        // This array finds the minimum power among all alive monsters (monsters to the right of the index after binary search)
        minPower[n-1] = monsters[n-1].power;
        for(int i = n-2; i >= 0; i--)       // IMP- Suffix min array
            minPower[i] = Math.min(minPower[i+1], monsters[i].power);
        long currentPower = 0, increase = k;
        while(increase > 0) {       // While the gun has some power left
            currentPower += increase;       // Increase current power by given value
            int lastKillIndex = monsterKilled(currentPower, monsters, n);
            if(lastKillIndex == n-1)        // IMP- If after some rounds, all monsters can be killed before power becomes 0
                return new StringBuilder().append("YES");
            increase -= minPower[lastKillIndex+1];
        }
        return new StringBuilder().append("NO");        // Otherwise not possible
    }

    public static int monsterKilled(long power, final Monster monsters[], final int n) {
        int left = 0, right = n-1, ans = -1;        // binary search
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            if(monsters[mid].health <= power) {     // IMP- Lower boundary indexing
                ans = mid;      // Store ans as we might not want to skip it
                left = mid+1;
            }   else    right = mid-1;
        }
        return ans;
    }
}
