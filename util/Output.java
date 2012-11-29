package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Output {

	DataOutputStream dos;

	private Output(DataOutputStream dos) {
		this.dos = dos;
	}

	public static Output create(String filePath) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(new File(filePath)));
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found");
			e.printStackTrace();
		}
		return new Output(dos);
	}

	public void writeInt(int value) {
		try {
			dos.writeInt(value);
		} catch (IOException e) {
			System.out.println("Could not write integer to file");
			e.printStackTrace();
		}
	}

	public void write(byte[] data) {
		try {
			dos.write(data);
		} catch (IOException e) {
			System.out.println("Could not read data from file");
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			dos.close();
		} catch (IOException e) {
			System.out.println("Could not close file");
			e.printStackTrace();
		}
	}
}
