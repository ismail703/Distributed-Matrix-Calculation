import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DivideMatrixImpl extends UnicastRemoteObject implements DivideMatrix {

    protected ClientImpl() throws RemoteException {
        super();
    }
    
    @Override
    public int[][] divide(String filePath, int num) throws RemoteException, IOException {
        String hostname = "localhost";
        int[] ports = {5048, 5049, 5105, 5205};

        List<int[][]> matrices = readMatricesFromFile(filePath);
        int[][] A = matrices.get(0);
        int[][] B = matrices.get(1);

        List<int[][]> subMatsA = splitMatrix(A, num);
        List<int[][]> subMatsB = splitMatrix(B, num);

        if (subMatsA == null || subMatsB == null) {
            System.out.println("Failed to divide matrices.");
            return null;
        }

        List<int[][]> results = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            try (Socket socket = new Socket(hostname, ports[i]);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeObject(subMatsA.get(i));
                output.writeObject(subMatsB.get(i));
                output.flush();

                int[][] result = (int[][]) input.readObject();
                results.add(result);

            } catch (Exception e) {
                System.out.println("Failed to communicate with worker: " + e.getMessage());
            }
        }

        return FusionMatrices(results, A.length);
    }

    public List<int[][]> splitMatrix(int[][] M, int num) {
        List<int[][]> subMats = new ArrayList<>();
        num /= 2;

        if (num % 2 != 0 || !isSquare(M) || M.length % num != 0) {
            System.out.println("Invalid Matrix or Number of Splits");
            return null;
        }

        int rows = M.length;
        int cols = M[0].length;
        int rowsPerMat = rows / num;
        int colsPerMat = cols / num;

        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int startRow = i * rowsPerMat;
                int endRow = startRow + rowsPerMat;
                int startCol = j * colsPerMat;
                int endCol = startCol + colsPerMat;

                int[][] subMat = new int[rowsPerMat][colsPerMat];
                for (int r = 0; r < rowsPerMat; r++) {
                    for (int c = 0; c < colsPerMat; c++) {
                        subMat[r][c] = M[startRow + r][startCol + c];
                    }
                }

                subMats.add(subMat);
            }
        }

        return subMats;
    }

    public int[][] FusionMatrices(List<int[][]> subMats, int size) {
        int subMatSize = subMats.get(0).length; 
        int numSubMatsPerRow = size / subMatSize;

        int[][] result = new int[size][size]; 

        int row = 0;

        
        for (int i = 0; i < numSubMatsPerRow; i++) {
            for (int r = 0; r < subMatSize; r++) {
                int resultCol = 0;

                for (int j = 0; j < numSubMatsPerRow; j++) {
                    int subMatIndex = i * numSubMatsPerRow + j;

                    if (subMatIndex >= subMats.size()) {
                        throw new RuntimeException("Invalid submatrix index: " + subMatIndex);
                    }

                    int[][] subMat = subMats.get(subMatIndex);

                    System.arraycopy(subMat[r], 0, result[row], resultCol, subMatSize);

                    resultCol += subMatSize;
                }

                row++;
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