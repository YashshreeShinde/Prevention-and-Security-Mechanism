import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

import java.nio.file.*;
import java.io.File;
import java.io.IOException;

public class MACGeneration
{
	public static final String FILE_PATH = "Resume.docx";
	public static byte[] getMAC(String filePath,SecretKey key)
	{

		try
		{
			// create a MAC and initialize with the above key
			Mac mac = Mac.getInstance(key.getAlgorithm());
			mac.init(key);

			File file = new File(filePath);
			Path path = file.toPath();


			// get the file as  bytes
			byte[] b =Files.readAllBytes(path);

			// create a digest from the byte array
			byte[] digest = mac.doFinal(b);

			System.out.println("MD : "+digest);

			return digest;

		}

		catch (NoSuchAlgorithmException e)
		{
			System.out.println("No Such Algorithm:" + e.getMessage());
			return null;
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("Unsupported Encoding:" + e.getMessage());
			return null;
		}
		catch (InvalidKeyException e)
		{
			System.out.println("Invalid Key:" + e.getMessage());
			return null;
		}
		catch (IOException e)
		{
			System.out.println("Read/write error: " + e.getMessage());
			return null;
	}

}

	public static void main(String args[]) throws Exception
	{
		// get a key generator for the HMAC-MD5 keyed-hashing algorithm
					KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
					//KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");

					// generate a key from the generator
				SecretKey key = keyGen.generateKey();
		System.out.println(MACGeneration.getMAC("D:\\TechnoMinds\\PollutionDetection\\FileTransfer\\costing.txt",key));
	}
}