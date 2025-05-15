import java.util.*;

public class InterestingDrink {
    public record Drink(int n, long[] prices, List<Long> queries) {}

    public static void main(String[] args) {
        Drink[] drink = new Drink[1];
        input: {
            try (Scanner sc = new Scanner(System.in)) {
                int n = sc.nextInt();
                drink[0] = new Drink(n, new long[n], new ArrayList<>());
                for (int i = 0; i < n; i++)
                    drink[0].prices[i] = sc.nextLong();
                int q = sc.nextInt();
                for (int i = 0; i < q; i++)
                    drink[0].queries.add(sc.nextLong());
            }
            break input;
        }

        output: {
            long[] prefix = countDrinks(drink[0].prices);
            drink[0].queries.forEach(query -> System.out.println(getColaDrinks(prefix, query)));
            break output;
        }
    }

    public static long getColaDrinks(long prefix[], long query) {
        if (query >= prefix.length) {
            return prefix[prefix.length - 1]; // Return the last valid element
        }
        return prefix[(int) query];
    }

    public static long[] countDrinks(long prices[]) {
        int maxValue = 0;
        for (long price : prices)
            maxValue = Math.max((int) price, maxValue);
        maxValue++; // Add 1 only once

        long sum[] = new long[maxValue];
        for (long price : prices)
            sum[(int) price]++;

        long prefix[] = new long[maxValue];
        prefix[0] = sum[0];
        for (int i = 1; i < maxValue; i++)
            prefix[i] = prefix[i - 1] + sum[i];

        return prefix;
    }
}
