package com.sdc2ch.web.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(value = "com.sdc2ch.*")
@EnableScheduling
@EnableTransactionManagement()
public class Seoulmilk2chApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(Seoulmilk2chApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Seoulmilk2chApplication.class, args)
		.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {

	        @Override
	        public void onApplicationEvent(ContextClosedEvent event) {
	        		logger.info("{}", event);
	        		
	        		Thread t = new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							







							
							System.out.println("====");
							System.out.println("====");
							System.out.println("====");
							System.out.println("====");
							System.out.println("====");
							System.out.println("====");
							System.out.println("====");
							
						}
					});
	        		t.setDaemon(false);
	        		t.run();
	        }
	    });
	}
}
