import java.io.*;
import java.util.*;

public class ProbD {
    static class State {
        long pos;
        int dir;
        int tmod;
        State(long pos, int dir, int tmod) {
            this.pos = pos;
            this.dir = dir;
            this.tmod = tmod;
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State)) return false;
            State s = (State) o;
            return pos == s.pos && dir == s.dir && tmod == s.tmod;
        }
        @Override
        public int hashCode() {
            return Objects.hash(pos, dir, tmod);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        while (t-- > 0) {
            String[] nk = br.readLine().split(" ");
            int n = Integer.parseInt(nk[0]);
            int k = Integer.parseInt(nk[1]);
            String[] ps = br.readLine().split(" ");
            String[] ds = br.readLine().split(" ");
            Map<Long, Integer> pos2delay = new HashMap<>();
            for (int i = 0; i < n; i++) {
                pos2delay.put(Long.parseLong(ps[i]), Integer.parseInt(ds[i]));
            }
            int q = Integer.parseInt(br.readLine());
            String[] as = br.readLine().split(" ");
            for (int qi = 0; qi < q; qi++) {
                long pos = Long.parseLong(as[qi]);
                int dir = 1;
                long time = 0;
                Set<State> visited = new HashSet<>();
                boolean escaped = false;
                while (true) {
                    State state = new State(pos, dir, (int)(time % k));
                    if (visited.contains(state)) {
                        System.out.println("NO");
                        break;
                    }
                    visited.add(state);
                    if (pos < 1 || pos > 1_000_000_000_000_000L) {
                        System.out.println("YES");
                        escaped = true;
                        break;
                    }
                    Integer delay = pos2delay.get(pos);
                    if (delay != null && (time % k) == delay) {
                        dir *= -1;
                    }
                    pos += dir;
                    time++;
                }
            }
        }
    }
}