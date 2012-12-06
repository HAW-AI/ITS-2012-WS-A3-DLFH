import javax.crypto.SecretKey;

import util.Input;
import util.Output;

import crypt.AES;
import crypt.RSA;

public class ReceiveSecureFile {
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Please enter 4 argument: <privat key> <public key> <plain data> <output file>");
			System.exit(0);
		}
		String prvKeyPath = args[0];
		String pubKeyPath = args[1];
		String encDataPath = args[2];
		String outputDataPath = args[3];

		Input in = Input.create(encDataPath);
		byte[] encSkey = in.readByLength(in.readLength());
		byte[] signature = in.readByLength(in.readLength());
		byte[] endData = in.readRemaining();
		in.close();

		RSA rsa = RSA.create(pubKeyPath, prvKeyPath);
		SecretKey aesKey = rsa.decryptAesKey(encSkey);
		AES aes = AES.create(aesKey);
		byte[] decData = aes.decrypt(endData);

		Output out = Output.create(outputDataPath);
		out.write(decData);
		out.close();
		System.out.println("Read Data: " + new String(decData));

		if (rsa.verifyAesKey(aesKey.getEncoded(), signature)) {
			System.out.println("Verification successfull");
		} else {
			System.out.println("Verification failed");
		}
	}
}
