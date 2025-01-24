import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 


public class Server extends DivideMatrixImpl {
	
	public static void main(String args[]) {
		try {
				DivideMatrix server = new DivideMatrixImpl();
                DivideMatrix stub = (DivideMatrix) UnicastRemoteObject.exportObject((DivideMatrix) server, 0);

                Registry registry = LocateRegistry.createRegistry(2003);

                registry.rebind("Sum", stub);
                System.out.println("Server ready ...."); 
		} catch (Exception e) { 
                System.err.println("Server exception: " + e.toString()); 
        } 

	}


}