import java.util.*;

public class MakingGenome {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            String[] fragments = new String[n];
            
            for (int i = 0; i < n; i++) {
                fragments[i] = sc.next();
            }
            
            // Graph
            List<List<Integer>> g = new ArrayList<>();
            for (int i = 0; i < 26; i++) g.add(new ArrayList<>());
            int[] indeg = new int[26];
            boolean[] present = new boolean[26];
            boolean[][] addedEdge = new boolean[26][26];
            
            // Build graph
            for (String s : fragments) {
                for (char c : s.toCharArray()) {
                    present[c - 'a'] = true;
                }
                for (int i = 0; i < s.length() - 1; i++) {
                    int u = s.charAt(i) - 'a';
                    int v = s.charAt(i + 1) - 'a';
                    if (!addedEdge[u][v]) {
                        g.get(u).add(v);
                        indeg[v]++;
                        addedEdge[u][v] = true;
                    }
                }
            }
            
            // Topological sort
            Queue<Integer> q = new ArrayDeque<>();
            for (int i = 0; i < 26; i++) {
                if (present[i] && indeg[i] == 0) {
                    q.add(i);
                }
            }
            
            StringBuilder sb = new StringBuilder();
            while (!q.isEmpty()) {
                int u = q.poll();
                sb.append((char) (u + 'a'));
                for (int v : g.get(u)) {
                    indeg[v]--;
                    if (indeg[v] == 0) {
                        q.add(v);
                    }
                }
            }
            
            // Append any isolated nodes missed in topo sort (shouldn't happen with correct queue init)
            for (int i = 0; i < 26; i++) {
                if (present[i] && sb.indexOf(String.valueOf((char)(i + 'a'))) == -1) {
                    sb.append((char)(i + 'a'));
                }
            }
            
            System.out.println(sb.toString());
        }
    }
}
