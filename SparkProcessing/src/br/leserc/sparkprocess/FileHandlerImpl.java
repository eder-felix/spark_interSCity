package br.leserc.sparkprocess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandlerImpl implements FileHandler {
	
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	private String path;
	private File file;
	
	public FileHandlerImpl(String path) {
		this.path = path;
		file = new File(path);	//Creates a new File
		
	}
	@Override
	public void saveFile(byte[] data) {
		// TODO Auto-generated method stub
		try {
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(data);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileOutputStream != null) {
				
				//try to close it
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public byte[] readFromFile() {
		// TODO Auto-generated method stub
		byte[] bFile = new byte[(int) file.length()];
		
		try {
			
			
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bFile;
	}

}