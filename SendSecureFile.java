import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

import crypt.AES;
import crypt.RSA;


public class SendSecureFile {
	public static void main(String[] args){
		if(args.length == 4){
			String prvKeyPath = args[0];
			String pubKeyPath = args[1];
			String plainDataPath = args[2];
			String outputDataPath = args[3];
			try {			
				File plainDataFile = new File(plainDataPath);
				FileInputStream plainDataFis = new FileInputStream(plainDataFile);
				DataInputStream plainDataDis = new DataInputStream(plainDataFis);

				StringBuffer dataBuffer = new StringBuffer();
				while(plainDataDis.available() != 0){
					dataBuffer.append((char)plainDataDis.read()); //TODO casting is ugly, readChar won't work...
				}
				System.out.println("Read data"+dataBuffer.toString());
				plainDataDis.close();
				
				RSA rsa = RSA.create(pubKeyPath, prvKeyPath);
				AES aes = AES.create(128);
				byte[] encryptedData = aes.encrypt(dataBuffer.toString());
				SecretKey aesKey = aes.getSkey();
				byte[] signedAesKey = rsa.signAesKey(aesKey);
				byte[] encryptedAesKey = rsa.encryptAesKey(aesKey);

				File ssfFile = new File(outputDataPath);
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
		} else {
			System.out.println("Please enter 4 argument: <privat key> <public key> <plain data> <output file>");
		}
	}
}
