/*
 * Ques - The Lakes - https://codeforces.com/problemset/problem/1829/E
 * Tags - Graph, DSU, Connected Component, Matrix
 * Rating - 1200
 */

import java.util.Scanner;
import java.util.function.BiFunction;

public class Lakes {
    public static class TestCase {      // Class to store data on each test case...
        int n, m;
        int world[][];

        public TestCase(String str) {   // Parametrized Constructor...
            String s[] = str.split(" ");        // Converting string to integer for variable assignment...
            this.n = Integer.parseInt(s[0]);
            this.m = Integer.parseInt(s[1]);
            this.world = new int[this.n][this.m];
        }

        private void updateWorldRow(int idx, String str) {  // Function to fill matrix...
            String s[] = str.split(" ");
            for(int i = 0; i < this.m; i++)
                this.world[idx][i] = Integer.parseInt(s[i]);
        }
    }
    public static void main(String[] args) {
        TestCase tests[];
        input : {   // Input block...
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            sc.nextLine();
            tests = new TestCase[t];
            for(int i = 0; i < t; i++) {
                tests[i] = new TestCase(sc.nextLine());
                for(int j = 0; j < tests[i].n; j++)
                    tests[i].updateWorldRow(j, sc.nextLine());
            }
            sc.close();
            break input;
        }
        output : {      // Output block...
            for(TestCase test : tests)
                System.out.println(findLargestLake(test.n, test.m, test.world));
            break output;
        }
    }

    private static class DSU {      // Disjoint Set Union class for finding Connected Components...
        int parent[], rank[], volume[];

        public DSU(int size) {      // Parametrized Constructor...
            this.parent = new int[size];
            this.rank = new int[size];
            this.volume = new int[size];
            for(int i = 0; i < size; i++)   // In starting each node is a parent of themselves...
                parent[i] = i;
        }

        public int find(int x) {    // Parent finding...
            if(parent[x] != x)
                parent[x] = find(parent[x]);    // Path compression technique...
            return parent[x];
        }

        public void union(int x, int y) {   // Union algorithm...
            int rootX = find(x), rootY = find(y);
            // This places the smaller component into larger component via union...
            if(rootX != rootY) {
                if(rank[rootX] < rank[rootY]) {     // When X is smaller component...
                    parent[rootX] = rootY;
                    volume[rootY] += volume[rootX];
                }
                else if(rank[rootY] < rank[rootX]) {    // When Y is smaller component...
                    parent[rootY] = rootX;
                    volume[rootX] += volume[rootY];
                }       // If both components are equal...
                else {
                    parent[rootY] = rootX;
                    volume[rootX] += volume[rootY];
                    rank[rootX]++;      // Increment the rank of component X...
                }
            }
            return;
        }

        public void addVolume(int x, int value) {   // Adding water depth to the root of the current component...
            volume[find(x)] += value;       // Updating volume from the grid into the DSU...
        }
    }

    public static int directions[][] = new int[][]{{-1,0}, {1,0}, {0,-1}, {0,1}};   // Four cardinal directions...
    public static int findLargestLake(int n, int m, int world[][]) {
        DSU dsu = new DSU(n * m);       // Class object defined...
        BiFunction<Integer, Integer, Integer> index2d = (i,j) -> i*m+j; // Function to map grid indexing and array indexing...
        dsu: {  // Disjoint Set Union Algorithm...
            for(int i = 0; i < n; i++)
                for(int j = 0; j < m; j++)
                    if(world[i][j] > 0) {
                        int currentIndex = index2d.apply(i, j);     // Applying bi function...
                        dsu.addVolume(currentIndex, world[i][j]);   // Updating the volume array...
                        for(int dir[] : directions) {
                            int ni = dir[0]+i, nj = dir[1]+j;   // Checking the adjacent cell for every direction...
                            if(ni >= 0 && ni < n && nj >= 0 && nj < m && world[ni][nj] > 0)
                                dsu.union(currentIndex, index2d.apply(ni, nj));     // Union the two cells...
                        }
                    }
            break dsu;
            }
        int maxLake = 0;    // Variable to store the maximum lake depth...
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                maxLake = Math.max(maxLake, dsu.volume[index2d.apply(i, j)]);
        return maxLake;
    }
}
