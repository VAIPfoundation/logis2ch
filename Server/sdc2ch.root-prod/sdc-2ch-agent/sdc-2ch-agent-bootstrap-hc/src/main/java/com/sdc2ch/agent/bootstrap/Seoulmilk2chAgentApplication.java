package com.sdc2ch.agent.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sdc2ch.core.utils.ApplicationPIdWriter;
import com.sdc2ch.core.utils.JustOneApp;

@ComponentScan(value = "com.sdc2ch.*")
@EnableScheduling
@SpringBootApplication
public class Seoulmilk2chAgentApplication {

	public static void main(String[] args) throws Exception {

		
		JustOneApp ua = new JustOneApp("Seoulmilk2chAgentApplication");

		if (ua.isAppActive()) {
			System.out.println("==========================================================\n");
			System.out.println("     Already Seoulmilk2chAgentApplication is running.\n");
			System.out.println("==========================================================");
			System.exit(1);
		}

		ApplicationPIdWriter.writePid("application.pid.2chweb.agent");
		 
		SpringApplication.run(Seoulmilk2chAgentApplication.class, args);
	}
	
	
}
