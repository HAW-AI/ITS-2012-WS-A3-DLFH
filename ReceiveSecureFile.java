import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

import crypt.AES;
import crypt.RSA;


public class ReceiveSecureFile {
	public static void main(String[] args){
		if(args.length == 4){
			String prvKeyPath = args[0];
			String pubKeyPath = args[1];
			String encDataPath = args[2];
			String outputDataPath = args[3];
			try {							
				File ssfFile = new File(encDataPath);
				FileInputStream ssfFis = new FileInputStream(ssfFile);
				DataInputStream ssfDis = new DataInputStream(ssfFis);
				int skeyLength = ssfDis.readInt();
				byte[] skeyBuffer = new byte[skeyLength];
				ssfDis.read(skeyBuffer);
				
				int sigLength = ssfDis.readInt();
				byte[] sigBuffer = new byte[sigLength];
				ssfDis.read(sigBuffer);
				
				byte[] encDataBuffer = new byte[ssfDis.available()]; //TODO not reliable?
				ssfDis.read(encDataBuffer);
				ssfDis.close();

				RSA rsa = RSA.create(pubKeyPath, prvKeyPath);
				SecretKey aesKey = rsa.decryptAesKey(skeyBuffer);
				AES aes = AES.create(aesKey);
				byte[] decData = aes.decrypt(encDataBuffer);
				
				File dataFile = new File(outputDataPath);
				FileOutputStream dataFis = new FileOutputStream(dataFile);
				DataOutputStream dataDis = new DataOutputStream(dataFis);
				dataDis.write(decData);
				dataDis.close();
				System.out.println(new String(decData));
				
				if(rsa.verifyAesKey(aesKey.getEncoded(), sigBuffer)){
					System.out.println("Verification successfull");
				} else {
					System.out.println("Verification failed");
				}
			} catch (IOException e) {
				System.out.println("An error occurred while writing ssf file");
				e.printStackTrace();
			}
		} else {
			System.out.println("Please enter 4 argument: <privat key> <public key> <plain data> <output file>");
		}
	}
}
