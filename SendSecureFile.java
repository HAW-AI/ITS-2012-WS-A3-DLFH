import crypt.RSA;


public class SendSecureFile {
	public static void main(String[] args){
		RSA.readPubKeyFile("blub.pub");
		RSA.readPrvKeyFile("blub.prv");
	}
}
