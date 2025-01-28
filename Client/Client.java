import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        try {

            Socket soc=new Socket("127.0.0.1", 5217);
            TransferFileClient t = new TransferFileClient(soc);
            t.Send();

            BufferedReader br = new BufferedReader(new FileReader("confserver.txt"));
            int nRegestry = Integer.parseInt(br.readLine().trim());

            Registry registry = LocateRegistry.getRegistry(nRegestry);

            DivideMatrix server = (DivideMatrix) registry.lookup("Sum");

            int[][] result = server.divide("matrices.txt", 4);

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

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }    
}

class TransferFileClient
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    TransferFileClient(Socket soc)
    {
        try {

            ClientSoc = soc;
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception ex)
        {
        }        
    }

    void SendFile() throws Exception
    {        
        
        String filename;
        System.out.print("Enter File Name :");
        filename=br.readLine();
            
        File f=new File(filename);
        if(!f.exists())
        {
            System.out.println("File not Exists...");
            dout.writeUTF("File not found");
            return;
        }
        
        dout.writeUTF(filename);
        
        String msgFromServer=din.readUTF();
        if(msgFromServer.compareTo("File Already Exists")==0)
        {
            String Option;
            System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
            Option=br.readLine();            
            if(Option=="Y")    
            {
                dout.writeUTF("Y");
            }
            else
            {
                dout.writeUTF("N");
                return;
            }
        }
        
        System.out.println("Sending File ...");
        FileInputStream fin=new FileInputStream(f);
        int ch;
        do
        {
            ch=fin.read();
            dout.writeUTF(String.valueOf(ch));
        }
        while(ch!=-1);
        fin.close();
        System.out.println(din.readUTF());
        
    }

    public void Send() throws Exception
    {
        System.out.println("Matrices File (.txt)");
        dout.writeUTF("SEND");
        SendFile();

        System.out.println("Opeartion file (.jar)");
        dout.writeUTF("SEND");
        SendFile();

    }
}

