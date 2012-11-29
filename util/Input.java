package util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Input {

	DataInputStream dis;

	private Input(DataInputStream dis) {
		this.dis = dis;
	}

	public static Input create(String FilePath) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(new File(FilePath)));
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found");
			e.printStackTrace();
		}
		return new Input(dis);
	}

	public int readLength() {
		int length = -1;
		try {
			length = dis.readInt();
		} catch (IOException e) {
			System.out.println("Could not read length from file");
			e.printStackTrace();
		}
		return length;
	}

	public byte[] readByLength(int length) {
		byte[] buffer = new byte[length];
		try {
			dis.read(buffer);
		} catch (IOException e) {
			System.out.println("Could not read data from file");
			e.printStackTrace();
		}
		return buffer;
	}

	public byte[] readRemaining() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while (dis.available() != 0) {
				baos.write(dis.readByte());
			}
		} catch (IOException e) {
			System.out.println("Could not read data from file");
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public void close() {
		try {
			dis.close();
		} catch (IOException e) {
			System.out.println("Could not close file");
			e.printStackTrace();
		}
	}
}
