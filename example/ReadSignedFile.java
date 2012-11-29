package example;

import java.security.*;
import java.security.spec.*;
import java.io.*;

/*
 * In diesem Beispiel wird eine Datei mit einer Nachricht, zugehöriger SHA1/DSA-Signatur
 * und öffentlichem Schlüssel im X.509-Format geöffnet und die Signatur mit Hilfe des
 * öffentlichen Schlüssels verifiziert.
 */
public class ReadSignedFile extends Object {

  // der Name der Datei, in die die signierte Nachricht gespeichert wird
  public String fileName;
 
  /**
   * Diese Methode gibt eine Fehlermeldung sowie eine Beschreibung
   * der Ausnahme aus. Danach wird das Programm beendet.
   *
   * @param msg eine Beschreibung für den Fehler
   * @param ex die Ausnahme, die den Fehler ausgelöst hat
   */
  private final static void Error(String msg, Exception ex) {
    System.out.println(msg);
    System.out.println(ex.getMessage());
    System.exit(0);
  }

  /**
   * Diese Methode liest eine Nachricht, deren Signatur und den
   * gehörigen öffentlichen Schlüssel zur Verifizierung der Signatur.
   * Dann wird die Signatur überprüft und die Nachricht zurückgelierfert.
   */
  public String readAndVerifyMessage() {

    byte[] message = null;
    byte[] signature = null;
    byte[] pubKeyEnc = null;

    try {
      // die Datei wird geöffnet und die Daten gelesen
      DataInputStream is = new DataInputStream(new FileInputStream(fileName));
      // die Länge der Nachricht
      int len = is.readInt();
      message = new byte[len];
      // die Nachricht
      is.read(message);
      // die Länge der Signatur
      len = is.readInt();
      signature = new byte[len];
      // die Signatur
      is.read(signature);
      // die Länge des öffentlichen Schlüssels
      len = is.readInt();
      pubKeyEnc = new byte[len];
      // der öffentliche Schlüssel
      is.read(pubKeyEnc);
      is.close();
    } catch (IOException ex) {
      Error("Fehler beim Lesen der signierten Nachricht!", ex);
    }

    // nun wird aus der Kodierung wieder ein public key erzeugt
    KeyFactory keyFac = null;
    try {
      keyFac = KeyFactory.getInstance("DSA");
    } catch (NoSuchAlgorithmException ex) {
      Error("Es existiert keine KeyFactory für DSA.", ex);
    }
    // aus dem Byte-Array können wir eine X.509-Schlüsselspezifikation erzeugen
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyEnc);
    // und in einen abgeschlossene, providerabhängigen Schlüssel konvertieren
    PublicKey pubKey = null;
    try {
      pubKey = keyFac.generatePublic(x509KeySpec);
    } catch (InvalidKeySpecException ex) {
      Error("Fehler beim Konvertieren des Schlüssels.", ex);
    }

    // nun wird die Signatur überprüft
    Signature dsa = null;
    try {
      dsa = Signature.getInstance("SHA1withDSA");
      dsa.initVerify(pubKey);
      dsa.update(message);
    } catch (NoSuchAlgorithmException ex) {
      Error("Keine Implementierung für SHA1withDSA!", ex);
    } catch (SignatureException ex) {
      Error("Fehler beim Überprüfen der Signatur!", ex);
    } catch (InvalidKeyException ex) {
      Error("Falscher Algorithmus?", ex);
    }

    try {
      boolean ok = dsa.verify(signature);
      if (ok)
        System.out.println("Signatur erfolgreich verifiziert!");
      else
        System.out.println("Signatur konnte nicht verifiziert werden!");
    } catch (SignatureException ex) {
      Error("Fehler beim Verifizieren der Signatur!", ex);
    }

    // als Ergebnis liefern wir die urpsprüngliche Nachricht
    return new String(message);
  }

  /**
   * Die main Methode.
   */
  public static void main (String args[]) {

    ReadSignedFile rsf = new ReadSignedFile();
	// Name der zu lesenden Signaturdatei = 1. Argument der Kommandozeile
	if (args.length < 1) {
	      System.out.println(
	        "Usage: java ReadSignedFile filename"
	      );
	      System.exit(0);
    }
  	rsf.fileName = args[0];

    // die Nachricht wird wieder gelesen und die Signatur überprüft
    String msg = rsf.readAndVerifyMessage();
    System.out.println("Signierte Nachricht: "+msg);
  }
}