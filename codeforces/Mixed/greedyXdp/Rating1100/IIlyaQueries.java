import java.util.*;

public class IIlyaQueries {
    public record IIlya(String s, List<int[]> queries) {}
    public static void main(String[] args) {
        IIlya illya;
        input: {
            try(Scanner sc = new Scanner(System.in)) {
                illya = new IIlya(sc.next(), new ArrayList<>());
                int q = sc.nextInt();
                for(int i = 0; i < q; i++)
                    illya.queries.add(new int[]{sc.nextInt(), sc.nextInt()});
            }
            break input;
        } output: {
            queryProcessing(illya);
            break output;
        }
    }

    public static void queryProcessing(IIlya iilya) {
        String text = iilya.s; int n = text.length();
        int similar[] = new int[n];
        for(int i = 1; i < similar.length; i++)
            similar[i] = text.charAt(i) == text.charAt(i-1) ? 1 : 0;
        int prefix[] = new int[n];
        for(int i = 1; i < prefix.length; i++)
            prefix[i] = prefix[i-1] + similar[i];
        for(int[] query : iilya.queries)
            System.out.println(queryOutput(text, query[0], query[1], prefix));
        return;
    }

    public static int queryOutput(String s, int l, int r, int prefix[]) {
        return prefix[r-1]-prefix[l-1];
    }
}
