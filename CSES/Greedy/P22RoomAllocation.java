import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class P22RoomAllocation {
    public static class FastReader {
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
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread th1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "room-allocation", 1 << 26);
        th1.start();
        try {
            th1.join();
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
        private final int time, type, id;

        public Event(int time, int type, int id) {
            this.time = time;
            this.type = type;
            this.id = id;
        }

        public int getTime() { return this.time; }
        public int getType() { return this.type; }
        public int getId() { return this.id; }
    }

    public static void solve(final int n, final int nums[][]) {
        // Create events: 0 for departure, 1 for arrival (so departures are processed first)
        PriorityQueue<Event> events = new PriorityQueue<>((e1, e2) -> {
            if (e1.getTime() != e2.getTime())
                return Integer.compare(e1.getTime(), e2.getTime());
            // Process departures (0) before arrivals (1) on the same day
            return Integer.compare(e1.getType(), e2.getType());
        });

        // Add all events
        for (int i = 0; i < n; i++) {
            events.add(new Event(nums[i][0], 1, i));      // arrival
            events.add(new Event(nums[i][1] + 1, 0, i));   // departure (day after checkout)
        }

        // Priority queue to keep track of available rooms (min-heap)
        PriorityQueue<Integer> availableRooms = new PriorityQueue<>();
        int[] roomAssignment = new int[n];
        int nextRoomNumber = 1;

        while (!events.isEmpty()) {
            Event event = events.poll();
            
            if (event.getType() == 1) {  // arrival
                int assignedRoom;
                if (availableRooms.isEmpty()) {
                    // Need a new room
                    assignedRoom = nextRoomNumber++;
                } else {
                    // Use the smallest available room
                    assignedRoom = availableRooms.poll();
                }
                roomAssignment[event.getId()] = assignedRoom;
            } else {  // departure
                // Free up the room
                availableRooms.add(roomAssignment[event.getId()]);
            }
        }

        // Output
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.println(nextRoomNumber - 1);  // Total number of rooms used
        for (int i = 0; i < n; i++) {
            if (i > 0) writer.print(" ");
            writer.print(roomAssignment[i]);
        }
        writer.println();
        writer.flush();
    }
}