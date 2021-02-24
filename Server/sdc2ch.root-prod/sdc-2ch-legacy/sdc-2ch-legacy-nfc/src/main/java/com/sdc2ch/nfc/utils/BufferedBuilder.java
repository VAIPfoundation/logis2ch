package com.sdc2ch.nfc.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BufferedBuilder {
	
	public static BufferedReader createReader(String path) {
		InputStream ins = getLoader().getResourceAsStream(path);
		return new BufferedReader(new InputStreamReader(ins));
	}
	
	private static ClassLoader getLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static InputStream getInputstream(String path) {
		return getLoader().getResourceAsStream(path);
	}
	
	public static FileOutputStream getOutPutStream(String path) {
		
		
		URL u = getLoader().getResource(path);
		
		try {
			FileOutputStream fos = new FileOutputStream(u.getFile());
			return fos;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		return null;
	}
}
