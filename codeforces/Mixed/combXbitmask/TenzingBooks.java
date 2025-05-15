import java.util.*;

public class TenzingBooks {
    public record Book(int n, long x, long[][] shelves) {}      // final record class defined
    public static void main(String[] args) {
        Book books[];       // Input variable
        input: {
            try(Scanner sc = new Scanner(System.in)) {      // Input block
                books = new Book[sc.nextInt()];
                for(int i = 0; i < books.length; i++) {
                    int n = sc.nextInt(); long x = sc.nextLong();
                    books[i] = new Book(n, x, new long[3][n]);
                    for(int j = 0; j < 3; j++)
                        for(int k = 0; k < n; k++)
                            books[i].shelves[j][k] = sc.nextLong();
                }
            }
            break input;
        } output: {     // Output block using streams for quick processing
            Arrays.stream(books).map(TenzingBooks::canGainKnowledge).forEach(result -> System.out.println(result ? "YES" : "NO"));
            break output;
        }
    }

    public static boolean canGainKnowledge(Book book) {
        int n = book.n; long x = book.x; long shelves[][] = book.shelves;
        long knowledge = 0;     // Knowledge variable
        for(int i = 0; i < 3; i++)
            // Loop to check the prefixes of books in each shelf that can be read to reach knowledge X
            for(int j = 0; j < n && (~x & shelves[i][j]) == 0; j++)
                knowledge |= shelves[i][j];     // Update the knowledge
        return knowledge == x;      // If the knowledge becomes X, then well and good
    }
}
