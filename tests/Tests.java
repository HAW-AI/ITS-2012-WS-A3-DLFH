package tests;
import static org.junit.Assert.*;

import javax.crypto.SecretKey;

import org.junit.Test;

import ssf.AES;

public class Tests {

	@Test
	public void testAes128() {
		String plainData = "Test String";
		AES aes = AES.create(128);
		SecretKey skey = aes.generateKey();
		byte [] encData = aes.encrypt(plainData, skey);
		byte[] raw_key = aes.keyToByteArray(skey);
		String decData = aes.decrypt(encData, raw_key);
		assertTrue(decData.equals(plainData));
	}
}
