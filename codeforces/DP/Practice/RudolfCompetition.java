import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class RudolfCompetition {
    public static class FastReader {            // Reading input
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() { return Integer.parseInt(next()); }
        public long nextLong() { return Long.parseLong(next()); }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), m = fast.nextInt(), h = fast.nextInt();
            List<PriorityQueue<Integer>> heapList = new ArrayList<>();
            for(int i = 0; i < n; i++) {
                heapList.add(new PriorityQueue<>());
                for(int j = 0; j < m; j++)
                    heapList.get(i).add(fast.nextInt());
            }
            builder.append(rudolfRank(n, m, h, heapList)).append("\n");
        }
        System.out.print(builder);
    }

    public static class Participant {
        int score, penalty;
        public Participant(int score, int penalty) {
            this.score = score; this.penalty = penalty;
        }
    }

    public static int rudolfRank(final int n, final int m, final int h, List<PriorityQueue<Integer>> heapList) {
        // We will compute each participant’s (score, penalty)
        // and also separately capture Rudolf’s (score, penalty).
        int rudolfScore = 0, rudolfPenalty = 0;
        Participant[] participants = new Participant[n];

        for (int i = 0; i < n; i++) {
            PriorityQueue<Integer> heap = heapList.get(i);
            int score = 0, penalty = 0;
            int timeSpent = 0;
            while(!heap.isEmpty() && timeSpent + heap.peek() <= h) {
                int t = heap.poll();
                timeSpent += t;
                penalty += timeSpent;
                score++;
            }
            participants[i] = new Participant(score, penalty);
            if (i == 0) { // Rudolf is the first participant
                rudolfScore = score;
                rudolfPenalty = penalty;
            }
        }

        // Count how many participants are strictly better than Rudolf.
        int countBetter = 0;
        for (Participant p : participants) {
            // p is considered better if:
            // - it has a higher score, or
            // - it has the same score and a lower penalty.
            if (p.score > rudolfScore || (p.score == rudolfScore && p.penalty < rudolfPenalty))
                countBetter++;
        }

        // Rudolf's rank is countBetter + 1
        return countBetter + 1;
    }

}
