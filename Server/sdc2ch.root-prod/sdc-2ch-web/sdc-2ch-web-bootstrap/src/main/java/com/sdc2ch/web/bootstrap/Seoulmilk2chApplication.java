package com.sdc2ch.web.bootstrap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sdc2ch.core.utils.ApplicationPIdWriter;
import com.sdc2ch.core.utils.JustOneApp;
import com.sdc2ch.service.admin.ISysActInfoService;
import com.sdc2ch.web.admin.repo.domain.sys.T_SYS_ACT_INFO_HIST;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(value = "com.sdc2ch.*")
@EnableScheduling
@EnableTransactionManagement()
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class Seoulmilk2chApplication implements CommandLineRunner  {
	private static final Logger logger = LoggerFactory.getLogger(Seoulmilk2chApplication.class);

	@Autowired
	private ISysActInfoService sysActInfoSvc;

	@Autowired
	private Environment env;
	




	public static void main(String[] args) throws Exception {
		
		
		JustOneApp ua = new JustOneApp("Seoulmilk2chApplication");

		if (ua.isAppActive()) {
			System.out.println("==========================================================\n");
			System.out.println("     Already Seoulmilk2chApplication is running.\n");
			System.out.println("==========================================================");
			System.exit(1);
		}
		
		ApplicationPIdWriter.writePid("application.pid.2chweb");

		
		ConfigurableApplicationContext context = SpringApplication.run(Seoulmilk2chApplication.class, args);
		
		context.addApplicationListener(new ApplicationListener<ContextStartedEvent>() {

	        @Override
	        public void onApplicationEvent(ContextStartedEvent event) {
				log.info("========================================");
				log.info("=                                      =");
				log.info("=   STARTING 2CH WEB PROCESS NOW...    =");
				log.info("=                                      =");
				log.info("========================================");
	        }
	    });
		
		context.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {

	        @Override
	        public void onApplicationEvent(ContextClosedEvent event) {
	        		logger.info("onApplicationEvent END {}", event);
	        		Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							








							log.info("====");
							log.info("====");
							log.info("====");
							log.info("====");
							log.info("====");
							log.info("====");
							log.info("====");
						}
					});
	        		t.setDaemon(false);
	        		t.run();
	        }
	    });
	}



	public static void BAK_main(String[] args) {
		SpringApplication.run(Seoulmilk2chApplication.class, args)

		.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {

	        @Override
	        public void onApplicationEvent(ContextClosedEvent event) {
	        		logger.info("onApplicationEvent END {}", event);
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
	
	@Override
	public void run(String... args) throws Exception {
		T_SYS_ACT_INFO_HIST sai = sysActInfoSvc.save(sysActInfoSvc.makeAppActInfo(true));

		logger.info("{}",sai);
	}

	@PreDestroy
	private void destory() throws Exception {
		logger.info("{}",sysActInfoSvc.save(sysActInfoSvc.makeAppActInfo(false)));
	}



	static class ApplicationListenerImpl implements ApplicationListener<ContextClosedEvent> {

		private ISysActInfoService sysActInfoSvc;
		ApplicationListenerImpl(ISysActInfoService sysActInfoSvc){
			this.sysActInfoSvc = sysActInfoSvc;
		}
		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			

			System.out.println(sysActInfoSvc);

		}

	}

}
