import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

import crypt.AES;
import crypt.RSA;


public class SendSecureFile {
	public static void main(String[] args){
		RSA rsa = RSA.create("blub.pub", "blub.prv");
		AES aes = AES.create(128);
		byte[] encryptedData = aes.encrypt("test");
		SecretKey aesKey = aes.getSkey();
		byte[] signedAesKey = rsa.signAesKey(aesKey);
		byte[] encryptedAesKey = rsa.encryptAesKey(aesKey);
		try {
		File ssfFile = new File("test.ssf");
		FileOutputStream ssfFos = new FileOutputStream(ssfFile);
		DataOutputStream ssfDos = new DataOutputStream(ssfFos);
		ssfDos.writeInt(encryptedAesKey.length);
		ssfDos.write(encryptedAesKey);
		ssfDos.writeInt(signedAesKey.length);
		ssfDos.write(signedAesKey);
		ssfDos.write(encryptedData);
		ssfDos.close();
		} catch (IOException e) {
			System.out.println("An error occurred while writing ssf file");
			//e.printStackTrace();
		}
		System.out.println("SSF file successfully created");
		
	}
}
