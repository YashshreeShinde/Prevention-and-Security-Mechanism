

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class GenerateKey
{
	public static SecretKey getSecretKey(String algorithm)
	{
		try
		{
			// get a key generator for the HMAC-MD5 keyed-hashing algorithm
			KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);

			// generate a key from the generator
			return keyGen.generateKey();
		}
		catch(NoSuchAlgorithmException NSAE)
		{
			System.out.println("No Such Algorithm "+algorithm);
			return null;
		}
	}
}