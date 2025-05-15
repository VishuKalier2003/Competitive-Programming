import java.util.Arrays;
import java.util.Scanner;

public class BinaryCafe {
    public record Cafeteria(int n, int k) {

        public long getUniqueChoices() {    // Getting the number of choices using bit masking
            return Math.min((long)Math.pow(2, k), n+1);
        }
    }
    public static void main(String[] args) {
        Cafeteria cafeterias[];
        input: {        // Input block
            try(Scanner sc = new Scanner(System.in)) {
                cafeterias = new Cafeteria[sc.nextInt()];
                for(int i = 0; i < cafeterias.length; i++) {
                    int n = sc.nextInt();
                    cafeterias[i] = new Cafeteria(n, sc.nextInt());
                }
            }
            break input;
        } output: {     // Output calling using streams
            Arrays.stream(cafeterias).map(Cafeteria::getUniqueChoices).forEach(System.out::println);
            break output;
        }
    }
}
