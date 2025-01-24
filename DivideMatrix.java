import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.List;

public interface DivideMatrix extends Remote {
    int[][] divide(int[][] A, int[][] B, int num) throws RemoteException;
    // int[][] add(int[][] A, int[][] B) throws RemoteException;
    // int[][] FusionMatrices(List<int[][]> subMats, int size) throws RemoteException;
}