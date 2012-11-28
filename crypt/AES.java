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
	      // AES-Schluessel generieren
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
      // Cipher-Objekt erzeugen und initialisieren mit AES-Algorithmus und Parametern
      // SUN-Default ist ECB-Modus (damit kein IV übergeben werden muss) und PKCS5Padding
      // Für Default-Parameter genügt: Cipher.getInstance("AES")
      //          und es kann auf die Parameter (IV) verzichtet werden	
      Cipher cipher;
      byte[] encData = null; //TODO i don not like null
      //byte[] encRest = null; //TODO i don not like null
	try {
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

      // Initialisierung
		cipher.init(Cipher.ENCRYPT_MODE, skey);
      
      // die zu schützenden Daten
      byte[] plain = plainData.getBytes();
      System.out.println("Daten: "+new String(plain));

      // nun werden die Daten verschlüsselt
      //(update wird bei großen Datenmengen mehrfach aufgerufen werden!)
      //encData = cipher.update(plain);

      // mit doFinal abschließen (Rest inkl. Padding ..)
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
	    System.out.println("Verschlüsselte Daten: "+new String(encData));
	    // zeigt den Algorithmus des Schlüssels
	    System.out.println("Schlüsselalgorithmus: "+skey.getAlgorithm());
	    // zeigt das Format des Schlüssels
	    System.out.println("Schlüsselformat: "+skey.getFormat());

      return encData; //TODO +encRest
	}
	
	public String decrypt(byte[] encData){
		  // sollen die Daten wieder entschlüsselt werden, so muss zuerst
	      // aus der Bytefolge eine neue AES-Schlüsselspezifikation erzeugt werden
		// mit diesem Parameter wird nun die AES-Chiffre ein zweites Mal,
	      // nun aber im DECRYPT MODE initialisiert (inkl. AlgorithmParameters)
	      Cipher cipher;
	      byte[] decData = null; //TODO i don not like null
	      //byte[] decRest = null; //TODO i don not like null
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			//AlgorithmParameters ap = cipher.getParameters();
			cipher.init(Cipher.DECRYPT_MODE, skey);


			// und die Daten entschlüsselt
			//decData = cipher.update(encData);

	      // mit doFinal abschließen (Rest inkl. Padding ..)

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
	      // anzeigen der entschlüsselten Daten
	      System.out.println("Entschlüsselte Daten: "+ new String(decData));
		return new String(decData);

	}

//	public byte[] keyToByteArray(SecretKey skey){
//		return skey.getEncoded();
//	}
	
//	private byte[] concatByteArr(byte[] that, byte[] other){
//		byte[] result = new byte[that.length + other.length];
//		System.arraycopy(that, 0, result, 0, that.length);
//		System.arraycopy(other, 0, result, that.length, other.length);
//		return result;
//	}
}