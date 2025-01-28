import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 
import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends DivideMatrixImpl {
	
	public static void main(String args[]) {
		try {
			    ServerSocket soc = new ServerSocket(5217);
                System.out.println("FTP Server Started on Port Number 5217");

                System.out.println("Waiting for Files ...");
                TransferFile t = new TransferFile(soc.accept());      


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

class TransferFile extends Thread
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    
    public TransferFile(Socket soc)
    {
        try {
            ClientSoc = soc;                        
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            System.out.println("FTP Client Connected ...");
            start();
            
        }
        catch(Exception ex)
        {
        }        
    }
    
    void ReceiveFile() throws Exception
    {
        String filename = din.readUTF();
        if(filename.compareTo("File not found") == 0)
        	return;    

        File f = new File(filename);
        String option;
        
        if(f.exists())
        {
            dout.writeUTF("File Already Exists");
            option = din.readUTF();
        }
        else
        {
            dout.writeUTF("SendFile");
            option="Y";
        }
            
            if(option.compareTo("Y")==0)
            {
                FileOutputStream fout=new FileOutputStream(f);
                int ch;
                String temp;
                do
                {
                    temp=din.readUTF();
                    ch=Integer.parseInt(temp);
                    if(ch!=-1)
                    {
                        fout.write(ch);                    
                    }
                } while(ch!=-1);
                fout.close();
                dout.writeUTF("File Send Successfully");
            }
            else
            {
                return;
            }
            
    }

    public void run()
    {
        for(int i = 0; i < 2; i++)
        {
            try {
                System.out.println("Waiting for Command ...");
                String Command = din.readUTF();
                System.out.println("\tSEND Command Receiced ...");                
                ReceiveFile();
            }

            catch(Exception ex)
            {
            }
        }
    }
}


