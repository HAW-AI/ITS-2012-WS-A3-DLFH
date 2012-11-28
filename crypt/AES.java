package crypt;
import java.security.*;

import javax.crypto.*;

public class AES {
	SecretKey skey;
	
	private AES (SecretKey skey){
		this.skey = skey;
	}

	public static AES create(int keysize){
		SecretKey skey = generateKey(keysize);
		return new AES(skey);
	}

	public static AES create(SecretKey skey){
		return new AES(skey);
	}
	
	public SecretKey getSkey(){
		return this.skey;
	}
	
	private static SecretKey generateKey(int keysize){
		KeyGenerator kg;
	    SecretKey skey = null;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(keysize);
			skey = kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skey;
	}
	
	public byte[] encrypt(String plainData){	
		Cipher cipher;
	    byte[] encData = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			byte[] plain = plainData.getBytes();
			System.out.println("Daten: "+new String(plain));
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
		System.out.println("Encrypted Data: "+ new String(encData));
		return encData;
	}
	
	public String decrypt(byte[] encData){
	    Cipher cipher;
	    byte[] decData = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			decData = cipher.doFinal(encData);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return new String(decData);
	}
}