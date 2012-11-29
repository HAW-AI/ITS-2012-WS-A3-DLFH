import javax.crypto.SecretKey;

import util.Input;
import util.Output;

import crypt.AES;
import crypt.RSA;


public class SendSecureFile {
	public static void main(String[] args){
		if(args.length < 4){
			System.out.println("Please enter 4 argument: <privat key> <public key> <plain data> <output file>");
			System.exit(0);
		}
		String prvKeyPath = args[0];
		String pubKeyPath = args[1];
		String plainDataPath = args[2];
		String outputDataPath = args[3];		
		Input in = Input.create(plainDataPath);
		byte[] data = in.readRemaining();
		in.close();
		
		RSA rsa = RSA.create(pubKeyPath, prvKeyPath);
		AES aes = AES.create(128);
		byte[] encryptedData = aes.encrypt(data);
		SecretKey aesKey = aes.getSkey();
		byte[] signedAesKey = rsa.signAesKey(aesKey);
		byte[] encryptedAesKey = rsa.encryptAesKey(aesKey);

		Output out = Output.create(outputDataPath);
		out.writeInt(encryptedAesKey.length);
		out.write(encryptedAesKey);
		out.writeInt(signedAesKey.length);
		out.write(signedAesKey);
		out.write(encryptedData);
		out.close();
		System.out.println("SSF file successfully created");
	}
}
