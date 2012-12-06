package crypt;

import java.security.*;

import javax.crypto.*;

public class AES {
	SecretKey skey;

	private AES(SecretKey skey) {
		this.skey = skey;
	}

	public static AES create(int keysize) {
		SecretKey skey = generateKey(keysize);
		return new AES(skey);
	}

	public static AES create(SecretKey skey) {
		return new AES(skey);
	}

	public SecretKey getSkey() {
		return this.skey;
	}

	private static SecretKey generateKey(int keysize) {
		KeyGenerator kg;
		SecretKey skey = null;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(keysize);
			skey = kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		}
		return skey;
	}

	public byte[] encrypt(byte[] plainData) {
		Cipher cipher;
		byte[] encData = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			encData = cipher.doFinal(plainData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.out.println("Padding not possible");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			System.out.println("Block size is invalid");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			System.out.println("Padding of data is invalid");
			e.printStackTrace();
		}
		return encData;
	}

	public byte[] decrypt(byte[] encData) {
		Cipher cipher;
		byte[] decData = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			decData = cipher.doFinal(encData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.out.println("Padding not possible");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			System.out.println("Block size is invalid");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			System.out.println("Padding of data is invalid");
			e.printStackTrace();
		}
		return decData;
	}
}