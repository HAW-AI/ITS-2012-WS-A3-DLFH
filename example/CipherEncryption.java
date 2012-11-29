package example;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Dieses Beispiel zeigt die Verwendung der Klasse Cipher zum
 * Verschlüsseln von beliebigen Daten.
 */
public class CipherEncryption {

  /**
   * Die main Methode.
   */
  public static void main(String[] argv) {


    try {
      // AES-Schlüssel generieren
      KeyGenerator kg = KeyGenerator.getInstance("AES");
      kg.init(128);		// Schlüssellänge
      SecretKey skey = kg.generateKey();

      // Cipher-Objekt erzeugen und initialisieren mit AES-Algorithmus und Parametern
      // SUN-Default ist ECB-Modus (damit kein IV übergeben werden muss) und PKCS5Padding
      // Für Default-Parameter genügt: Cipher.getInstance("AES")
      //          und es kann auf die Parameter (IV) verzichtet werden
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

      // Initialisierung
      cipher.init(Cipher.ENCRYPT_MODE, skey);

      // Der Initialisierungsvektor IV muss dem Empfänger als Parameter
      // mit übergeben werden (falls nicht Betrieb im ECB-Modus)
      //System.out.println("Cipher Parameter: "+ cipher.getParameters().toString());
      //AlgorithmParameters ap = cipher.getParameters();

      // die zu schützenden Daten
      byte[] plain = "Das ist nur ein Test!".getBytes();
      System.out.println("Daten: "+new String(plain));

      // nun werden die Daten verschlüsselt
      //(update wird bei großen Datenmengen mehrfach aufgerufen werden!)
      byte[] encData = cipher.update(plain);

      // mit doFinal abschließen (Rest inkl. Padding ..)
      byte[] encRest = cipher.doFinal();

      // und angezeigt
      System.out.println("Verschlüsselte Daten: "+new String(encData) + new String(encRest));
      // zeigt den Algorithmus des Schlüssels
      System.out.println("Schlüsselalgorithmus: "+skey.getAlgorithm());
      // zeigt das Format des Schlüssels
      System.out.println("Schlüsselformat: "+skey.getFormat());

      // nun wird der kodierte Schlüssel als Bytefolge gespeichert
      byte[] raw_key = skey.getEncoded();

	  // hier findet die Übertragung statt ...

      // sollen die Daten wieder entschlüsselt werden, so muss zuerst
      // aus der Bytefolge eine neue AES-Schlüsselspezifikation erzeugt werden
      SecretKeySpec skspec = new SecretKeySpec(raw_key, "AES");

      // mit diesem Parameter wird nun die AES-Chiffre ein zweites Mal,
      // nun aber im DECRYPT MODE initialisiert (inkl. AlgorithmParameters)
      //cipher.init(Cipher.DECRYPT_MODE, skspec, ap);
      cipher.init(Cipher.DECRYPT_MODE, skspec);

      // und die Daten entschlüsselt
	  byte[] decData = cipher.update(encData);

      // mit doFinal abschließen (Rest inkl. Padding ..)
      byte[] decRest = cipher.doFinal(encRest);

      // anzeigen der entschlüsselten Daten
      System.out.println("Entschlüsselte Daten: "+ new String(decData) + new String(decRest));

    } catch (Exception ex) {
      // ein Fehler???
      System.out.println("Error: "+ex.getMessage());
      ex.printStackTrace();
    }
  }
}