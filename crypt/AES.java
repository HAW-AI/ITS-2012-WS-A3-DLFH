package crypt;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Dieses Beispiel zeigt die Verwendung der Klasse Cipher zum
 * Verschl�sseln von beliebigen Daten.
 */
public class AES {
	int keyLength = 128;
	
	private AES (int keyLength){
		this.keyLength = keyLength;
	}

	public static AES create(int keyLength){
		return new AES(keyLength);
	}
	
	public byte[] encrypt(String plainData, SecretKey skey){
      // Cipher-Objekt erzeugen und initialisieren mit AES-Algorithmus und Parametern
      // SUN-Default ist ECB-Modus (damit kein IV �bergeben werden muss) und PKCS5Padding
      // F�r Default-Parameter gen�gt: Cipher.getInstance("AES")
      //          und es kann auf die Parameter (IV) verzichtet werden	
      Cipher cipher;
      byte[] encData = null; //TODO i don not like null
      //byte[] encRest = null; //TODO i don not like null
	try {
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

      // Initialisierung
		cipher.init(Cipher.ENCRYPT_MODE, skey);
      
      // die zu sch�tzenden Daten
      byte[] plain = plainData.getBytes();
      System.out.println("Daten: "+new String(plain));

      // nun werden die Daten verschl�sselt
      //(update wird bei gro�en Datenmengen mehrfach aufgerufen werden!)
      //encData = cipher.update(plain);

      // mit doFinal abschlie�en (Rest inkl. Padding ..)
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
	
	    // und angezeigt
	    System.out.println("Verschl�sselte Daten: "+new String(encData));
	    // zeigt den Algorithmus des Schl�ssels
	    System.out.println("Schl�sselalgorithmus: "+skey.getAlgorithm());
	    // zeigt das Format des Schl�ssels
	    System.out.println("Schl�sselformat: "+skey.getFormat());

      return encData; //TODO +encRest
	}
	
	public String decrypt(byte[] encData, byte[] raw_key){
		  // sollen die Daten wieder entschl�sselt werden, so muss zuerst
	      // aus der Bytefolge eine neue AES-Schl�sselspezifikation erzeugt werden
	      SecretKeySpec skspec = new SecretKeySpec(raw_key, "AES");
		// mit diesem Parameter wird nun die AES-Chiffre ein zweites Mal,
	      // nun aber im DECRYPT MODE initialisiert (inkl. AlgorithmParameters)
	      Cipher cipher;
	      byte[] decData = null; //TODO i don not like null
	      //byte[] decRest = null; //TODO i don not like null
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			//AlgorithmParameters ap = cipher.getParameters();
			cipher.init(Cipher.DECRYPT_MODE, skspec);


			// und die Daten entschl�sselt
			//decData = cipher.update(encData);

	      // mit doFinal abschlie�en (Rest inkl. Padding ..)

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
	      // anzeigen der entschl�sselten Daten
	      System.out.println("Entschl�sselte Daten: "+ new String(decData));
		return new String(decData);

	}
	
	public SecretKey generateKey(){
	      // AES-Schl�ssel generieren
	      KeyGenerator kg;
	      SecretKey skey = null;
		try {
		  kg = KeyGenerator.getInstance("AES");
	      kg.init(this.keyLength);
	      skey = kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skey;
	}

	public byte[] keyToByteArray(SecretKey skey){
		return skey.getEncoded();
	}
	
//	private byte[] concatByteArr(byte[] that, byte[] other){
//		byte[] result = new byte[that.length + other.length];
//		System.arraycopy(that, 0, result, 0, that.length);
//		System.arraycopy(other, 0, result, that.length, other.length);
//		return result;
//	}
}