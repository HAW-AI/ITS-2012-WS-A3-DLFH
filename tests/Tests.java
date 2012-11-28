package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import crypt.AES;


public class Tests {

	@Test
	public void testAes128() {
		String plainData = "Test String";
		AES aes = AES.create(128);
		String decData = aes.decrypt(aes.encrypt(plainData));
		assertTrue(decData.equals(plainData));
	}
}
