import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P5RestaurantCustomers {
    public static class FastReader {        // Note: Fastest reading data
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t3 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "restaurants", 1 << 26);
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[][] = new int[n][2];
        for(int i = 0; i < n; i++) {
            nums[i][0] = fast.nextInt();
            nums[i][1] = fast.nextInt();
        }
        solve(n, nums);
    }

    public static class Event {
        private final int time, type;

        public Event(int time, int type) {
            this.time = time;
            this.type = type;
        }
    }

    // Hack: Use sweep line algorithm to traverse in time and process only those intervals that are getting changed
    public static void solve(final int n, final int times[][]) {
        List<Event> events = new ArrayList<>();
        // Info: Add events as points, since the data will change when event will start and end
        for(int i = 0; i < n; i++) {
            events.add(new Event(times[i][0], 1));      // Arrival
            events.add(new Event(times[i][1], -1));         // Departure
        }
        Collections.sort(events, (Event e1, Event e2) -> {
            if(e1.time == e2.time)
                // Note: works for the base cases, we want to process (sort) end events before arrival, when the time are same
                return Integer.compare(e1.type, e2.type);
            return Integer.compare(e1.time, e2.time);       // Otherwise smaller time goes first
        });
        int max = 0, current = 0;
        for(Event e : events) {
            current += e.type;      // Add time for each event
            max = Math.max(max, current);
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(max);
        writer.write(output.toString());
        writer.flush();
    }
}
