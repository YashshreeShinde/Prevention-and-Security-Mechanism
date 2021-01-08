
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JOptionPane;
import java.util.Arrays;
import javax.crypto.SecretKey;

public class Client {

    //public static final String FILE_PATH = "D:\\TechnoMinds\\PollutionDetection\\FileTransfer\\pic-3085.jpg";
    //public static final String SERVER_IP = "192.168.1.2";
    public static final int PORT = 3333;
    public static final int BUFFER_SIZE = 100;

    public static int sendFile(String filePath, String ipAddress, int transactionID)
    {
        try
        {
            File file = new File(filePath);

            Socket socketController = new Socket(Constants.CONTROLLER_IP, Constants.IP_SENDER_PORT);

            /* A timeout of zero is interpreted as an infinite timeout.*/
			//socketController.setSoTimeout(0);
			/* make sure the connection is actually alive.*/
			//socketController.setKeepAlive(true);

            ObjectOutputStream outController = new ObjectOutputStream(socketController.getOutputStream());
            ObjectInputStream inController = new ObjectInputStream(socketController.getInputStream());

            outController.writeObject(Constants.FORWARD_REQUEST);
            outController.writeObject(transactionID);
            Object object = inController.readObject();
            //int transID = (int)object;

            //object = inController.readObject();
            SecretKey keyForMACCalculation=null;
            if(object instanceof SecretKey)
            {
            	keyForMACCalculation = (SecretKey)object;
			}

            /*Calculate Message Digest for the file to be transferred*/
            //byte digest[] = MessageDigestImplementation.getMessageDigest(filePath);
            byte digest[] = MACGeneration.getMAC(filePath,keyForMACCalculation);
			//outController.writeObject(digest.length);
            //outController.writeObject(Arrays.copyOf(digest, digest.length));

            Socket socket = new Socket(ipAddress, PORT);

			/* A timeout of zero is interpreted as an infinite timeout.*/
			//socket.setSoTimeout(0);
			/* make sure the connection is actually alive.*/
			//socket.setKeepAlive(true);

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			corruptFile(filePath);


            oos.writeObject(transactionID);
            oos.writeObject(file.getName());
            Object o = ois.readObject();
            if (o instanceof String)
            {
                if (o.toString().compareTo("SEND_FILE") == 0)
                {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    Integer bytesRead = 0;

                    while ((bytesRead = fis.read(buffer)) > 0)
                    {
                        oos.writeObject(bytesRead);
                        oos.writeObject(Arrays.copyOf(buffer, buffer.length));
                    }

                    //byte digest[] = GenerateMACForFile.getMAC(FILE_PATH);
                    //byte digest[] = MessageDigestImplementation.getMessageDigest(filePath);
                    oos.writeObject(digest.length);
                    oos.writeObject(Arrays.copyOf(digest, digest.length));
                    oos.close();
            		ois.close();
            		socketController.close();
            		socket.close();
                    return 0;
                }
                else if (o.toString().compareTo("REQUEST_REJECTED") == 0)
                {
					return 1;
                }

            }

        }
        catch (SocketException se)
        {
            JOptionPane.showMessageDialog(null, se.getMessage() + "\nMake sure server is on", "Error!!!", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static void corruptFile(String filename)
    {
		try
		{
			FileWriter fstream = new FileWriter(filename,true);
		    BufferedWriter fbw = new BufferedWriter(fstream);
		    //corrupting the file
		    for(int i=0;i<1024*1000;i++)
		    {
		    	fbw.write("!@#$%^&*()_+)(*&^%$#@!");
		    	fbw.newLine();
			}
		    fbw.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
        }
	}

    public static void sendSuccess(int transactionID)
	{
		try
	    {
			Socket socketController = new Socket(Constants.CONTROLLER_IP, Constants.IP_SENDER_PORT);

	        /* A timeout of zero is interpreted as an infinite timeout.*/
			//socketController.setSoTimeout(300000);
			/* make sure the connection is actually alive.*/
			//socketController.setKeepAlive(true);

	        ObjectOutputStream outController = new ObjectOutputStream(socketController.getOutputStream());
	        ObjectInputStream inController = new ObjectInputStream(socketController.getInputStream());

	        outController.writeObject(Constants.SUCCESS_MESSAGE);
	        outController.writeObject(transactionID);
		}
		catch (SocketException se)
		{
			JOptionPane.showMessageDialog(null, se.getMessage() + "\nMake sure server is on", "Error!!!", JOptionPane.ERROR_MESSAGE);
			//return -1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//return -1;
		}
	}


    /*
     * public static void main(String[] args) throws Exception { File file = new
     * File(FILE_PATH); Socket socket = new Socket("192.168.1.2", Server.PORT);
     * ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
     * ObjectOutputStream oos = new
     * ObjectOutputStream(socket.getOutputStream());
     *
     * oos.writeObject(file.getName());
     *
     * FileInputStream fis = new FileInputStream(file); byte [] buffer = new
     * byte[Server.BUFFER_SIZE]; Integer bytesRead = 0;
     *
     * while ((bytesRead = fis.read(buffer)) > 0) { oos.writeObject(bytesRead);
     * oos.writeObject(Arrays.copyOf(buffer, buffer.length)); }
     *
     * //byte digest[] = GenerateMACForFile.getMAC(FILE_PATH); byte digest[] =
     * MessageDigestImplementation.getMessageDigest(FILE_PATH);
     * oos.writeObject(digest.length); oos.writeObject(Arrays.copyOf(digest,
     * digest.length));
     *
     * oos.close(); ois.close();
     *
     * System.out.println("File sent successfully...");
    }
     */
}