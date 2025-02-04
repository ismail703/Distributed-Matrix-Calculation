import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Slave {
    public static void main(String[] args) {
        int port = 2003;

        // try (/*ServerSocket serverSocket = new ServerSocket(port)*/) {
            // System.out.println("Slave is listening on port " + port);

            // while (true) {
                // Socket socket = serverSocket.accept();
                // System.out.println("Task received from " + socket.getInetAddress().getHostAddress());

                // Execute calc.jar using ProcessBuilder
                try {
                    ProcessBuilder pb = new ProcessBuilder("java", "-jar", "MatrixOpt.jar", "matrices.txt");
                    Process process = pb.start();

                    // Read the output from calc.jar
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }

                    int exitCode = process.waitFor();
                    System.out.println("calc.jar executed with exit code: " + exitCode);
                } catch (IOException | InterruptedException e) {
                    System.err.println("Error executing calc.jar: " + e.getMessage());
                }// finally {
                //     socket.close();
                // }
            // }
        // } catch (IOException ex) {
            // System.out.println("Server exception: " + ex.getMessage());
            // ex.printStackTrace();
        // }
    }
}
