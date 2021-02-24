package com.sdc2ch.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import lombok.extern.slf4j.Slf4j;







@Slf4j
public class Smilk2ChProcess {

	public static boolean isStillAllive(String pidStr) {
	    
	    String command = null;
	    if (OsCheck.getOS()==OsCheck.OS.WINDOWS) {
	        log.debug("Check alive Windows mode. Pid: [{}]", pidStr);
	        command = "cmd /c tasklist /FI \"PID eq " + pidStr + "\"";            
	    } else if (OsCheck.getOS()==OsCheck.OS.LINUX) {
	        log.debug("Check alive Linux/Unix mode. Pid: [{}]", pidStr);
	        command = "ps -p " + pidStr;            
	    } else {
	        log.warn("Unsuported OS: Check alive for Pid: [{}] return false", pidStr);
	        return false;
	    }
	    return isProcessIdRunning(pidStr, command); 
	}
	
	private static boolean isProcessIdRunning(String pid, String command) {
	    log.debug("Command [{}]",command );
	    try {
	        Process pr = Runtime.getRuntime().exec(command);

	        
	        InputStreamReader isReader = new InputStreamReader(pr.getInputStream(),"MS949");
	        BufferedReader bReader = new BufferedReader(isReader);
	        String strLine = null;
	        while ((strLine= bReader.readLine()) != null) {
	        	log.info(strLine);;
	            if (strLine.contains(" " + pid + " ")) {
	            	log.info("PID: "+pid+" OK");
	                return true;
	            }
	        }
        	log.info("PID: "+pid+" NOT FOUND");

	        return false;
	    } catch (Exception ex) {
	        log.warn("Got exception using system command [{}-{}].", command, ex);
	        return true;
	    }
	}
	
	
	public static Process execProcess(String command) throws IOException {

		Process pr = Runtime.getRuntime().exec(command);
		return pr;
	}

	
	public static int killProcess(int pid) {
		
		String command = "";
		try {
		    if (OsCheck.getOS()==OsCheck.OS.WINDOWS) {
		        command = "taskkill /F /PID " + pid;   
		    } else if (OsCheck.getOS()==OsCheck.OS.LINUX) {
		        command = "kill -9 " + pid;            
		    } else {
		        log.warn("Unsuported OS: Check alive for Pid: [{}] return fail", pid);
		        return -1;
		    }
			
			Runtime.getRuntime().exec(command);
			log.info("killProcess: {}",command);
			return 1;
		} catch (IOException e) {
			
			log.error("killProcess:"+e.toString());
		}

		return -1;
	}
	

































}
