import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Slave {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Slave is listening on port " + port);

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("Task received");

                new Worker(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }  
}

class Worker extends Thread {
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            int[][] A = (int[][]) input.readObject();
            int[][] B = (int[][]) input.readObject();

            int[][] result = add(A, B);

            if (result != null) {
                output.writeObject(result);
                output.flush();
                System.out.println("Result Send Sucessfely");
            } else {
                System.out.println("Matrix addition failed: Matrices must have the same dimensions.");
            }        
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Slave exception: " + ex.getMessage());
        }
    }

    private int[][] add(int[][] A, int[][] B) {
        if (!hasEqualLength(A, B)) {
            System.out.println("Matrices must have the same length");
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
} 