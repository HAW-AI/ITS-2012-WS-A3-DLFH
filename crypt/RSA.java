package crypt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSA {
	
	PublicKey pubKey;
	PrivateKey prvKey;

	private RSA(PublicKey pubKey, PrivateKey prvKey){
		this.pubKey = pubKey;
		this.prvKey = prvKey;
	}
	
	public static RSA create(int keysize){
		KeyPair keypair = generateKeyPair(keysize);
		return new RSA(keypair.getPublic(), keypair.getPrivate());
	}
	
	public static RSA create(String pubKeyPath, String prvKeyPath){
		PublicKey pubKey = readPubKeyFile(pubKeyPath);
		PrivateKey prvKey = readPrvKeyFile(prvKeyPath);
		return new RSA(pubKey, prvKey);
	}
	
	  public static KeyPair generateKeyPair(int keysize) {
		  	KeyPair keyPair = null;
		    try {
		    	System.out.println("Generating keys");
		      KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		      gen.initialize(keysize);
		      keyPair = gen.generateKeyPair();
		    } catch (NoSuchAlgorithmException e) {
				System.out.println("Algorithm not found");
		    	//e.printStackTrace();
		    }
			return keyPair;
		  }
	  
	  private static PublicKey readPubKeyFile(String pubKeyPath){
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
	  
	  private static PrivateKey readPrvKeyFile(String prvKeyPath){
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
	  
	  public void writeKeysToFile(String pubKeyPath, String prvKeyPath, String ownerName){
		  writePublicKey(pubKeyPath, ownerName);
		  writePrivateKey(prvKeyPath, ownerName);
	  }
	  
	  private void writePublicKey(String pubKeyPath, String ownerName){
		try {
			System.out.println("Writing public keyfile. Keyformat: "+this.pubKey.getFormat());
			System.out.println(this.pubKey);
			byte[] pubKey = this.pubKey.getEncoded();
			File pubKeyFile = new File(pubKeyPath+ownerName+".pub");
			FileOutputStream pubKeyFos = new FileOutputStream(pubKeyFile);
			DataOutputStream pubKeyDos = new DataOutputStream(pubKeyFos);
			pubKeyDos.writeInt(ownerName.length());
			pubKeyDos.writeBytes(ownerName);
			pubKeyDos.writeInt(pubKey.length);
			System.out.println("Key length: "+pubKey.length);
			pubKeyDos.write(pubKey);
			pubKeyDos.close();
			System.out.println("Keyfiles successfully created");
		} catch (FileNotFoundException e) {
			System.out.println("Public key file could not be created");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occurred while writing public key file");
			//e.printStackTrace();
		}
	  }
	  
	 private void writePrivateKey(String prvKeyPath, String ownerName){
		 try {
		 System.out.println("Writing private keyfile. Keyformat "+this.prvKey.getFormat());
			System.out.println(this.prvKey);
			byte[] prvKey = this.prvKey.getEncoded();
			File prvKeyFile = new File(prvKeyPath+ownerName+".prv");
			FileOutputStream prvKeyFos = new FileOutputStream(prvKeyFile);
			DataOutputStream prvKeyDos = new DataOutputStream(prvKeyFos);
			prvKeyDos.writeInt(ownerName.length());
			prvKeyDos.writeBytes(ownerName);
			prvKeyDos.writeInt(prvKey.length);
			System.out.println("Key length: "+prvKey.length);
			prvKeyDos.write(prvKey);
			prvKeyDos.close();
			System.out.println("Keyfile successfully created");
		} catch (FileNotFoundException e) {
			System.out.println("Private key file could not be created");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occurred while writing private key file");
			//e.printStackTrace();
		}
	 }
	  
	 public byte[] signAesKey(SecretKey skey){
		    // als erstes erzeugen wir die Signatur
		    Signature AesSig = null;
		    byte[] signature = null;
		    try {
		      AesSig = Signature.getInstance("SHA1withRSA");
		    } catch (NoSuchAlgorithmException ex) {
		    	System.out.println("Algorithm not found");
		    }

		    try {
		      // zum Signieren benötigen wir den geheimen Schlüssel
		    	AesSig.initSign(prvKey);
		    } catch (InvalidKeyException ex) {
		      System.out.println("Key is invalid");
		    }

		    try {
		      AesSig.update(skey.getEncoded());
		      signature = AesSig.sign();
		    } catch (SignatureException ex) {
		      System.out.println("An error occurred while signing a secret key ");
		    }
		    return signature;
	 }
	 
	public boolean verifyAesKey(byte[] skey ,byte[] signature) {
	    boolean result = false;
		try {
		Signature AesSig = Signature.getInstance("SHA1withRSA");
		AesSig.initVerify(pubKey);
		AesSig.update(skey);
		result = AesSig.verify(signature);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	 
	 public byte[] encryptAesKey(SecretKey skey){
	    Cipher cipher;
	    byte[] encData = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] plain = skey.getEncoded();
			encData = cipher.doFinal(plain);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return encData;
	 }

	public SecretKey decryptAesKey(byte[] encSkey) {
		Cipher cipher;
	      SecretKey decSkey = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prvKey);
			byte[] decSkeyByte = cipher.doFinal(encSkey);
			decSkey = new SecretKeySpec(decSkeyByte, "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decSkey;
	}
}
