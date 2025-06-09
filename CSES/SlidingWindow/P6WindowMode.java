import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class P6WindowMode {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
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

    // freqMap: maps value → its current frequency in the window
    public static Map<Integer, Integer> fMap;
    // sfMap: maps frequency → TreeSet of all values that appear exactly that many times
    public static SortedMap<Integer, TreeSet<Integer>> sfMap;

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain();
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "window-mode", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain() throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt();
        final int k = fr.readInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.readInt();
        solve(n, k, nums);
    }

    public static void solve(final int n, final int k, final int[] nums) {
        fMap = new HashMap<>();
        sfMap = new TreeMap<>();

        StringBuilder out = new StringBuilder();

        // Build first window [0..k-1]
        for (int i = 0; i < k; i++) {
            increase(nums[i]);
        }
        // Record mode of first window
        int mode = getCurrentMode();
        out.append(mode);

        // Slide the window
        for (int i = k; i < n; i++) {
            decrease(nums[i - k]); // element leaving
            increase(nums[i]);     // element entering
            mode = getCurrentMode();
            out.append(' ').append(mode);
        }

        // Print all modes
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        pw.println(out.toString());
        pw.flush();
    }

    // Increment frequency of 'num' in the current window
    @SuppressWarnings("unused")
    public static void increase(int num) {
        int oldFreq = fMap.getOrDefault(num, 0);
        int newFreq = oldFreq + 1;
        fMap.put(num, newFreq);

        // Remove from old frequency bucket (if > 0)
        if (oldFreq > 0) {
            TreeSet<Integer> oldSet = sfMap.get(oldFreq);
            oldSet.remove(num);
            if (oldSet.isEmpty()) {
                sfMap.remove(oldFreq);
            }
        }

        // Add to new frequency bucket
        sfMap.computeIfAbsent(newFreq, x -> new TreeSet<>()).add(num);
    }

    // Decrement frequency of 'num' in the current window
    @SuppressWarnings("unused")
    public static void decrease(int num) {
        int oldFreq = fMap.get(num); // must exist
        // Remove from old frequency bucket
        TreeSet<Integer> oldSet = sfMap.get(oldFreq);
        oldSet.remove(num);
        if (oldSet.isEmpty()) {
            sfMap.remove(oldFreq);
        }

        int newFreq = oldFreq - 1;
        if (newFreq == 0) {
            fMap.remove(num);
        } else {
            fMap.put(num, newFreq);
            // Add to new frequency bucket
            sfMap.computeIfAbsent(newFreq, x -> new TreeSet<>()).add(num);
        }
    }

    // Retrieve the current mode = smallest element in the highest-frequency bucket
    public static int getCurrentMode() {
        int highestFreq = sfMap.lastKey(); // largest frequency present
        return sfMap.get(highestFreq).first();
    }
}
