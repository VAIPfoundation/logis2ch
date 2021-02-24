package com.sdc2ch.prcss.test;

import java.io.File;
import java.util.stream.Stream;

public class Parsing {
	
	public static void main(String[] args) {
		
		File folder = new File("dtg/DTG");
		
		StringBuilder sb = new StringBuilder();
		Stream.of(folder.listFiles()).forEach(f -> {
			
			String fnm = f.getName().split("_")[1];
			fnm = fnm.substring(0, fnm.lastIndexOf("."));
			
			sb.append("\"").append(fnm).append("\"").append(",");
		});
		
		
		System.out.println(sb.toString());
	}

}
