import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        try {

            BufferedReader br = new BufferedReader(new FileReader("confserver.txt"));
            int nRegestry = Integer.parseInt(br.readLine().trim());


            List<int[][]> matrices = readMatricesFromFile("matrices.txt");

            int[][] A = matrices.get(0);
            int[][] B = matrices.get(1);

            System.out.println("Matrice 1");
            printMatrix(A);
            System.out.println("Matrice 2");
            printMatrix(B);

            Registry registry = LocateRegistry.getRegistry(nRegestry);

            DivideMatrix server = (DivideMatrix) registry.lookup("Sum");

            int[][] result = server.divide(A, B, 4);

            if (result != null) {
                System.out.println("Result of Matrix Addition:");
                printMatrix(result);
            } else {
                System.out.println("Invalid Matrices");
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.getMessage());
        }
    }

    private static List<int[][]> readMatricesFromFile(String filename) throws IOException {
        List<int[][]> matrices = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        List<int[]> rows = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("{")) {
                if (!line.equals("{")) {
                    line = line.replace("{", "").replace("}", "").trim();
                    String[] elements = line.split(",");
                    int[] row = new int[elements.length];
                    for (int i = 0; i < elements.length; i++) {
                        row[i] = Integer.parseInt(elements[i].trim());
                    }
                    rows.add(row);
                }
            } else if (line.startsWith("}")) {
                int[][] matrix = new int[rows.size()][];
                for (int i = 0; i < rows.size(); i++) {
                    matrix[i] = rows.get(i);
                }
                matrices.add(matrix);
                rows.clear(); 
            }
        }
        reader.close();

        return matrices;
    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }    
}