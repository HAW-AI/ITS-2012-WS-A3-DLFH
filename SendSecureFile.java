import crypt.RSA;


public class SendSecureFile {
	public static void main(String[] args){
		RSA rsa = RSA.create("blub.pub", "blub.prv");
	}
}
