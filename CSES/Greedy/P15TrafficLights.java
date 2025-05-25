import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

public class P15TrafficLights {
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
        }, "traffic-lights", 1 << 26);
        th1.start();
        try {
            th1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int x = fast.nextInt(), n = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(x, nums);
    }

    public static void subOptimal(final int x, final int nums[]) {
        TreeSet<Integer> lights = new TreeSet<>();
        TreeMap<Integer, Integer> segments = new TreeMap<>();
        // Initialize with boundary lights and the full segment
        lights.add(0);
        lights.add(x);
        segments.put(x, 1);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int i = 0; i < nums.length; i++) {
            int pos = nums[i];
            // Find the lights immediately to the left and right of pos
            Integer left = lights.floor(pos);
            Integer right = lights.ceiling(pos);
            // Calculate the segment that will be split
            int oldSegment = right - left;
            // Hack: Try to use get() and remove() as little as possible since they run in O(log n) time and contribute to TLE
            int freq = segments.get(oldSegment)-1;
            if(freq == 0)
                segments.remove(oldSegment);
            else
                segments.put(oldSegment, freq);
            // Add the new light
            lights.add(pos);
            // Add the two new segments created by splitting
            segments.put(pos-left, segments.getOrDefault(pos-left, 0)+1);      // Left segment
            segments.put(right-pos, segments.getOrDefault(right-pos, 0)+1);     // Right segment
            // Output the maximum segment length
            output.append(segments.lastKey()).append(" ");
        }
        writer.write(output.toString());        // Info: Use writer only once when printing the final output
        writer.flush();
    }

    public static void solve(final int n, final int nums[]) {
        TreeSet<Integer> lights = new TreeSet<>();
        lights.add(0);
        lights.add(n);
        for(int num : nums)
            lights.add(num);
        int maxGap = 0, prev = 0;
        for(int light : lights) {
            maxGap = Math.max(light-prev, maxGap);
            prev = light;
        }
        int res[] = new int[nums.length];
        // Our result for nth insertion is to be stored logically at res.length-1 index
        res[nums.length-1] = maxGap;
        for(int i = nums.length-1; i > 0; i--) {
            int light = nums[i];
            lights.remove(light);
            int left = lights.floor(light);
            int right = lights.ceiling(light);
            maxGap = Math.max(maxGap, right-left);
            res[i-1] = maxGap;      // Our i th light when placed, stores the result in i-1 th index
        }
        final StringBuilder output = new StringBuilder();
        for(int i = 0; i < res.length; i++)
            output.append(res[i]).append(" ");
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}