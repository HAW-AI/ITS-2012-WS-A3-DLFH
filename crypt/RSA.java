package crypt;

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

import util.Input;
import util.Output;

public class RSA {

	PublicKey pubKey;
	PrivateKey prvKey;

	private RSA(PublicKey pubKey, PrivateKey prvKey) {
		this.pubKey = pubKey;
		this.prvKey = prvKey;
	}

	public static RSA create(int keysize) {
		KeyPair keypair = generateKeyPair(keysize);
		return new RSA(keypair.getPublic(), keypair.getPrivate());
	}

	public static RSA create(String pubKeyPath, String prvKeyPath) {
		PublicKey pubKey = toPubKey(readKey(pubKeyPath));
		PrivateKey prvKey = toPrvKey(readKey(prvKeyPath));
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
			e.printStackTrace();
		}
		return keyPair;
	}

	private static byte[] readKey(String keyPath) {
		Input in = Input.create(keyPath);
		in.readByLength(in.readLength());
		byte[] key = in.readByLength(in.readLength());
		return key;
	}

	private static PublicKey toPubKey(byte[] key) {
		PublicKey pubKey = null;
		try {
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			pubKey = keyFactory.generatePublic(pubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			System.out.println("Key specifications are wrong");
			e.printStackTrace();
		}
		return pubKey;
	}

	private static PrivateKey toPrvKey(byte[] key) {
		PrivateKey prvKey = null;
		try {
			PKCS8EncodedKeySpec prvKeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			prvKey = keyFactory.generatePrivate(prvKeySpec);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			System.out.println("Key specifications are wrong");
			e.printStackTrace();
		}
		return prvKey;
	}

	public void writeKeysToFile(String pubKeyPath, String prvKeyPath,
			String ownerName) {
		writeKey(pubKey.getEncoded(), pubKeyPath, ownerName);
		writeKey(prvKey.getEncoded(), prvKeyPath, ownerName);
	}

	private void writeKey(byte[] key, String keyPath, String ownerName) {
		Output out = Output.create(keyPath);
		out.writeInt(ownerName.length());
		out.write(ownerName.getBytes());
		out.writeInt(key.length);
		out.write(key);
		out.close();
	}

	public byte[] signAesKey(SecretKey skey) {
		byte[] signedData = null;
		try {
			Signature signer = Signature.getInstance("SHA1withRSA");
			signer.initSign(prvKey);
			signer.update(skey.getEncoded());
			signedData = signer.sign();
		} catch (SignatureException e) {
			System.out.println("An error occurred while signing a secret key");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		}
		return signedData;
	}

	public boolean verifyAesKey(byte[] skey, byte[] signature) {
		boolean result = false;
		try {
			Signature signer = Signature.getInstance("SHA1withRSA");
			signer.initVerify(pubKey);
			signer.update(skey);
			result = signer.verify(signature);
		} catch (SignatureException e) {
			System.out
					.println("An error occurred while verifying a secret key");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm not found");
			e.printStackTrace();
		}
		return result;
	}

	public byte[] encryptAesKey(SecretKey skey) {
		Cipher cipher;
		byte[] encData = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] plain = skey.getEncoded();
			encData = cipher.doFinal(plain);
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

	public SecretKey decryptAesKey(byte[] encSkey) {
		Cipher cipher;
		SecretKey decSkey = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prvKey);
			byte[] decSkeyByte = cipher.doFinal(encSkey);
			decSkey = new SecretKeySpec(decSkeyByte, "AES");
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
		return decSkey;
	}
}
