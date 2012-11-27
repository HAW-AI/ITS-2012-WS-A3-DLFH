package crypt;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
	
	  public static KeyPair generateKeyPair(int keysize) {
		  	KeyPair keyPair = null;
		    try {
		      KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		      gen.initialize(keysize);
		      keyPair = gen.generateKeyPair();
		    } catch (NoSuchAlgorithmException e) {
				System.out.println("Algorithm not found");
		    	//e.printStackTrace();
		    }
			return keyPair;
		  }
	  
	  public static PublicKey readPubKeyFile(String pubKeyPath){
		  	PublicKey pubKey = null;
			try {
				File pubKeyFile = new File(pubKeyPath);
				FileInputStream pubKeyFis = new FileInputStream(pubKeyFile);
				DataInputStream pubKeyDis = new DataInputStream(pubKeyFis);

				int OwnerLength = pubKeyDis.readInt();
				byte[] OwnerBuffer = new byte[OwnerLength];
				pubKeyDis.read(OwnerBuffer);
				String Owner = new String(OwnerBuffer);
				System.out.println("Key length: "+OwnerLength);
				System.out.println(Owner);
				
				int KeyLength = pubKeyDis.readInt();
				byte[] KeyBuffer = new byte[KeyLength];
				pubKeyDis.readFully(KeyBuffer);
				System.out.println("Key length: "+KeyBuffer.length);
				pubKeyDis.close();
				
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(KeyBuffer);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				pubKey = keyFactory.generatePublic(pubKeySpec);
				System.out.println("Pubic key read:"+pubKey.toString());
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Algorithm not found");
				//e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				System.out.println("Key specifications are wrong");
				//e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println("Public key file could not be found");
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println("An error occurred while reading public key file");
				//e.printStackTrace();
			}
			return pubKey;
	  }
	  
	  public static PrivateKey readPrvKeyFile(String prvKeyPath){
		  PrivateKey prvKey = null;
		  try {
				File prvKeyFile = new File(prvKeyPath);
				FileInputStream prvKeyFis = new FileInputStream(prvKeyFile);
				DataInputStream prvKeyDis = new DataInputStream(prvKeyFis);

				int OwnerLength = prvKeyDis.readInt();
				byte[] OwnerBuffer = new byte[OwnerLength];
				prvKeyDis.read(OwnerBuffer);
				String Owner = new String(OwnerBuffer);
				System.out.println(Owner);
				
				int KeyLength = prvKeyDis.readInt();
				byte[] KeyBuffer = new byte[KeyLength];
				prvKeyDis.read(KeyBuffer);
				System.out.println("Key length: "+KeyBuffer.length);
				prvKeyDis.close();
				
				PKCS8EncodedKeySpec prvKeySpec = new PKCS8EncodedKeySpec(KeyBuffer);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				prvKey = keyFactory.generatePrivate(prvKeySpec);
				System.out.println("Pubic key read:"+prvKey.toString());
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Algorithm not found");
				//e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				System.out.println("Key specifications are wrong");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println("Public key file could not be found");
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println("An error occurred while reading public key file");
				//e.printStackTrace();
			}
			return prvKey;
	  }
}
