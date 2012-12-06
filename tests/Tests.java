package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import crypt.AES;
import crypt.RSA;


public class Tests {

	@Test
	public void testAes128() {
		String plainData = "Test String";
		AES aes = AES.create(128);
		byte[] decData = aes.decrypt(aes.encrypt(plainData.getBytes()));
		assertTrue(plainData.equals(new String(decData)));
	}
	
	@Test
	public void testRSASign() {
		AES aes = AES.create(128);
		RSA rsa = RSA.create(1024);
		byte[] signatur = rsa.signAesKey(aes.getSkey());
		assertTrue(rsa.verifyAesKey(aes.getSkey().getEncoded(), signatur));
	}
}
