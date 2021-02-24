package com.sdc2ch.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.system.ApplicationPid;


public class ApplicationPIdWriter {
	@SuppressWarnings("resource")
	static String loadPidFilePath(String key) {
		String pidFile="";
		InputStream inputExt = null;
		InputStream inputInt = null;
        try  {
        	try {
        		inputExt = new FileInputStream("config/application.properties");
        	}
        	catch(FileNotFoundException e) {
        	}
        	inputInt = ApplicationPIdWriter.class.getClassLoader().getResourceAsStream("application.properties");
        	

            Properties propExt = new Properties();
            Properties propInt = new Properties();

            
            propExt.load(inputExt);
            propInt.load(inputInt);

            
            pidFile=propExt.getProperty(key);
            if(pidFile == null) {
            	pidFile=propInt.getProperty(key);
            }
            System.out.println("pid file path="+pidFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
		return pidFile;
	}

	
	public static void writePid(String key) {
		String pidFilePath=loadPidFilePath(key);
		try {
			File file = new File(pidFilePath);
			new ApplicationPid().write(file);
		} catch (IOException e) {
			System.out.println(""+e);
		}

	}
}
