import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Berpizza {
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

    public static class Customers {
        public int index, tip;

        public Customers(int index, int tip) {
            this.index = index; this.tip = tip;
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt(), arrival = 1;        // This arrival serves as a hash (unique identifier) to find the customers arrival order
        final StringBuilder result = new StringBuilder();
        polycarpHeap = new PriorityQueue<>(new Comparator<Customers>() {
            @Override       // Imp- Priority Queue overridden
            public int compare(Customers c1, Customers c2) {
                if(c1.tip == c2.tip)
                    return Integer.compare(c1.index, c2.index);
                return Integer.compare(c2.tip, c1.tip);
            }
        });
        monocarpQueue = new LinkedList<>();
        served = new HashSet<>();
        while(t-- > 0) {
            int query = fast.nextInt();
            if(query == 1) {
                int value = fast.nextInt();     // Filling data in heap and queue
                polycarpHeap.add(new Customers(arrival, value));
                monocarpQueue.add(new Customers(arrival, value));
                arrival++;
            }
            else {
                if(query == 2)      // If monocarp has to serve
                    result.append(monocarpServes()+" ");
                else result.append(polycarpServes()+" ");   // If polycarp has to serve
            }
        }
        System.out.print(result);
    }

    public static Set<Integer> served;
    public static PriorityQueue<Customers> polycarpHeap;
    public static Queue<Customers> monocarpQueue;

    public static String monocarpServes() {
        // Imp- We will remove all the customers that are already served from the queue (lazy propagation)
        while(served.contains(monocarpQueue.peek().index))
            monocarpQueue.remove();
        String serve = monocarpQueue.poll().index+"";
        served.add(Integer.parseInt(serve));        // Add the first unserved customer to served
        return serve;
    }

    public static String polycarpServes() {
        // Imp- We will serve all the customers that are laready served from the heap (lazy propagation)
        while(served.contains(polycarpHeap.peek().index))
            polycarpHeap.remove();
        String serve = polycarpHeap.poll().index+"";
        served.add(Integer.parseInt(serve));        // Add the first-largest unserved customer to served
        return serve;
    }
}
