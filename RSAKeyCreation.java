import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import crypt.RSA;


public class RSAKeyCreation {
	public static void main(String[] args){
		String OwnerName = "JDoe";
		KeyPair keys = RSA.generateKeyPair();
		PublicKey pubKey = keys.getPublic();
		PrivateKey prvKey = keys.getPrivate();
		
		FileOutputStream pubKeyOs = null;
		FileOutputStream prvKeyOs = null;
		File pubKeyFile, prvKeyFile;
		pubKeyFile = new File(OwnerName+".pub");
		prvKeyFile = new File(OwnerName+".prv");
		try {
			pubKeyOs = new FileOutputStream(pubKeyFile);
			prvKeyOs = new FileOutputStream(prvKeyFile);

			pubKeyOs.write(OwnerName.length());
			pubKeyOs.write(OwnerName.getBytes());
			pubKeyOs.write(pubKey.toString().length());
			pubKeyOs.write(pubKey.toString().getBytes());
			
			prvKeyOs.write(OwnerName.length());
			prvKeyOs.write(OwnerName.getBytes());
			prvKeyOs.write(prvKey.toString().length());
			prvKeyOs.write(prvKey.toString().getBytes());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
