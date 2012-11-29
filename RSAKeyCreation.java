import crypt.RSA;

public class RSAKeyCreation {
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("Please enter an owner name as first argument");
			System.exit(0);
		}
		System.out.println("Owner of keypair: "+args[0]);
		String ownerName = args[0];
		RSA rsa = RSA.create(1024);
		rsa.writeKeysToFile("", "", ownerName);
	}
}
