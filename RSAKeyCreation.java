import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;

import crypt.RSA;

public class RSAKeyCreation {
	public static void main(String[] args){
		if(args.length == 1 && !args[0].isEmpty()){
			System.out.println("Owner of keypair: "+args[0]);
			String OwnerName = args[0];
			System.out.println("Generating keys");
			KeyPair keys = RSA.generateKeyPair(1024);
			try {
				System.out.println("Writing public keyfile. Keyformat: "+keys.getPublic().getFormat());
				System.out.println(keys.getPublic());
				byte[] pubKey = keys.getPublic().getEncoded();
				File pubKeyFile = new File(OwnerName+".pub");
				FileOutputStream pubKeyFos = new FileOutputStream(pubKeyFile);
				DataOutputStream pubKeyDos = new DataOutputStream(pubKeyFos);
				pubKeyDos.writeInt(OwnerName.length());
				pubKeyDos.writeBytes(OwnerName);
				pubKeyDos.writeInt(pubKey.length);
				System.out.println("Key length: "+pubKey.length);
				pubKeyDos.write(pubKey);
				pubKeyDos.close();
	
				System.out.println("Writing private keyfile. Keyformat "+keys.getPrivate().getFormat());
				System.out.println(keys.getPrivate());
				byte[] prvKey = keys.getPrivate().getEncoded();
				File prvKeyFile = new File(OwnerName+".prv");
				FileOutputStream prvKeyFos = new FileOutputStream(prvKeyFile);
				DataOutputStream prvKeyDos = new DataOutputStream(prvKeyFos);
				prvKeyDos.writeInt(OwnerName.length());
				prvKeyDos.writeBytes(OwnerName);
				prvKeyDos.writeInt(prvKey.length);
				System.out.println("Key length: "+prvKey.length);
				prvKeyDos.write(prvKey);
				prvKeyDos.close();
				System.out.println("Keyfiles successfully created");
			} catch (FileNotFoundException e) {
				System.out.println("At least one of the key files could not be created");
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println("An error occurred while writing to one of the key files");
				//e.printStackTrace();
			}
		} else {
			System.out.println("Please enter an owner name as first argument");
		}
	}
}
