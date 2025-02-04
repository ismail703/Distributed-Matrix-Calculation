import java.io.*;
import java.util.*;

public class MatrixOpt {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the input file name as an argument.");
            return;
        }

        try {
            MatrixOpt matrixOpt = new MatrixOpt();
            List<int[][]> matrices = matrixOpt.readMatricesFromFile(args[0]);

            if (matrices.size() < 2) {
                System.out.println("The file must contain at least two matrices.");
                return;
            }

            int[][] A = matrices.get(0);
            int[][] B = matrices.get(1);

            int[][] result = matrixOpt.add(A, B);

            if (result != null) {
                try (FileOutputStream fos = new FileOutputStream("result.txt")) {
                    for (int i = 0; i < result.length; i++) {
                        for (int j = 0; j < result[i].length; j++) {
                            fos.write((result[i][j] + " ").getBytes());
                        }
                        fos.write('\n');
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file or writing the result: " + e.getMessage());
        }
    }

    public MatrixOpt() {}

    private int[][] add(int[][] A, int[][] B) {
        if (!hasEqualLength(A, B)) {
            System.out.println("Matrices must have the same dimensions");
            return null;
        }

        int rows = A.length;
        int cols = A[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }

        return result;
    }

    public List<int[][]> readMatricesFromFile(String filename) throws IOException {
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

    public boolean hasEqualLength(int[][] A, int[][] B) {
        if (A.length != B.length) {
            return false;
        }

        for (int i = 0; i < A.length; i++) {
            if (A[i].length != B[i].length) {
                return false;
            }
        }

        return true;
    }

    public boolean isSquare(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            if (A[i].length != A.length) {
                return false;
            }
        }
        return true;
    }
}