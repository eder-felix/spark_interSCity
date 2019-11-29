package br.leserc.sparkprocess;

public interface FileHandler {
	void saveFile(byte[] data);
	byte[] readFromFile();
}
