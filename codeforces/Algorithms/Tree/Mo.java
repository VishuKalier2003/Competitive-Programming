
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Mo {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            final int n = sc.nextInt(), q = sc.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = sc.nextInt();
            List<Query> queries = new ArrayList<>();
            for(int i = 0; i < q; i++)
                queries.add(new Query(sc.nextInt(), sc.nextInt(), i));
            MoAlgo moAlgo = new MoAlgo(n, q, nums, queries);
            System.out.print(moAlgo.solveQueries());
        }
    }

    public static final class Query {       // Info: Query class to store the left, right and the index of query in the query list
        public int left, right, index;

        public Query(int l, int r, int idx) {
            this.left = l;
            this.right = r;
            this.index = idx;       // Index at which the query appears in the query list
        }
    }

    // Note: Mo Algorithm for solving queries effectively by sorting queries and performing square root decomposition on the array by partitioning into blocks
    public static final class MoAlgo {
        private int curL, curR, count;
        private final int freq[], nums[];       // Info: frequency map and input array stored here
        private final List<Query> queries;
        private final int BLOCK;        // Info: Block size as sqrt(N)

        public MoAlgo(final int n, final int max, final int nums[], final List<Query> queries) {
            this.queries = queries;
            this.nums = nums;
            this.BLOCK = (int)Math.max(1, Math.sqrt(n));        // Square root N
            // Note: Sort queries first by their block number on left index and then by their right index
            Collections.sort(queries, (Query q1, Query q2) -> {
                if(q1.left/BLOCK == q2.left/BLOCK)
                    return Integer.compare(q1.right, q2.right);     // If block indices are same, sort by right
                return Integer.compare(q1.left/BLOCK, q2.left/BLOCK);       // Otherwise sort by block indexes
            });
            // Initialize the pointers and other variables
            this.curL = queries.get(0).left;
            this.curR = queries.get(0).left-1;
            this.count = 0;
            this.freq = new int[max];
        }

        public void addOfCurrent(int pos) {     // Info: the index of nums array is passed as parameters
            int value = this.nums[pos];
            this.freq[value]++;
            if(this.freq[value] == 1)       // If the value becomes one, we have found a unique element
                this.count++;
        }

        public void deleteOfCurrent(int pos) {      // Info: the index of nums array is passed as parameters
            int value = this.nums[pos];
            this.freq[value]--;
            if(this.freq[value] == 0)       // If the value becomes zero, one unique element has been removed
                this.count--;
        }

        // Hack: Solves q queries in O((N + Q) sqrt(N)) complexity, faster than quadratic
        public StringBuilder solveQueries() {       // Function for solving queries
            int result[] = new int[this.queries.size()];
            StringBuilder answer = new StringBuilder();
            for(Query q : queries) {
                // First we shift the left pointer (curL) and keep updating the frequency and count
                while(this.curL > q.left) {
                    this.curL--;        // Note: I dont want to remove current element, so I will skip it by moving left
                    addOfCurrent(this.curL);
                }
                while(this.curL < q.left) {
                    deleteOfCurrent(this.curL);
                    this.curL++;
                }
                // Then we shift the right pointer (curR) and keep updating the frequency and count
                while (this.curR > q.right) {
                    deleteOfCurrent(this.curR);
                    this.curR--;
                }
                while(this.curR < q.right) {
                    this.curR++;        // Note: I dont want to remove current element, so I will skip it by moving right
                    addOfCurrent(this.curR);
                }
                // Hack: When all conditions are completed, my mo's pointers are at correct positions for the current query
                result[q.index] = this.count;   // Info: since queries are sorted we need to access the query index which restores the original query sequence
            }
            for(int value : result)
                answer.append(value).append(" ");
            return answer;
        }
    }
}
