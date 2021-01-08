import java.security.MessageDigest;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class MessageDigestImplementation
{
	/*create Java String from the contents of a file*/
	private static String readFile(String path)
	{
		String fileContents=null;
		FileInputStream stream=null;
		try
		{
			stream = new FileInputStream(new File(path));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
	  		fileContents = Charset.defaultCharset().decode(bb).toString();
	  		stream.close();
	  	}
	  	catch(IOException ioe)
	  	{
			ioe.printStackTrace();
		}
		/*catch(FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		}*/
	  	finally
	  	{

			return fileContents;
		}
	}

	public static String writeMessageDigest(String path)
	{
		try
		{
			StringTokenizer st = new StringTokenizer(path,".");
			String extension="";
			while(st.hasMoreElements())
			{
				extension = st.nextToken();
			}
			String filePath = "input"+"."+extension;
			FileOutputStream fos = new FileOutputStream(filePath);
			MessageDigest md = MessageDigest.getInstance("SHA");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			String data = readFile(path);
			byte buf[] = data.getBytes();
			md.update(buf);
			oos.writeObject(data);
			oos.writeObject(md.digest());
			return filePath;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
	}

	public static byte[] getMessageDigest(String path)
		{
			try
			{

				MessageDigest md = MessageDigest.getInstance("SHA");

				String data = readFile(path);
				byte buf[] = data.getBytes();
				md.update(buf);

				return md.digest();
			}
			catch (Exception e)
			{
				System.out.println(e);
				return null;
			}
	}

	public static void readAndCompareMessageDigest(String path)
	{
		try
		{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			if (!(o instanceof String))
			{
				System.out.println("Unexpected data in file");
				System.exit(-1);
			}

			String data = (String) o;
			//System.out.println("Got message " + data);



			o = ois.readObject();
			if (!(o instanceof byte[]))
			{
				System.out.println("Unexpected data in file");
				System.exit(-1);
			}

			byte origDigest[] = (byte []) o;
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(data.getBytes());
			if (MessageDigest.isEqual(md.digest(), origDigest))
			{
				System.out.println("Message is valid");
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				oos.writeObject(data);

				oos.close();
			}
			else
				System.out.println("Message was corrupted");
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}