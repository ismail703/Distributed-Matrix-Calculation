import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.io.IOException;
import java.util.List;

public interface DivideMatrix extends Remote {
    // int[][] divide(int[][] A, int[][] B, int num) throws RemoteException;
    int[][] divide(String filePath, int num) throws RemoteException, IOException;
    // int[][] add(int[][] A, int[][] B) throws RemoteException;
    // int[][] FusionMatrices(List<int[][]> subMats, int size) throws RemoteException;
}