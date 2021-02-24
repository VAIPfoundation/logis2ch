package com.sdc2ch.legacy.aiv.service;

import static com.sdc2ch.require.config.IApplicationDbConfig.cp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.aiv.io.Notification;
import com.sdc2ch.aiv.io.Notification.commandType;
import com.sdc2ch.aiv.service.IMobileNotificationService;
import com.sdc2ch.core.expression.InstanceOf;
import com.sdc2ch.legacy.aiv.push.AllocatedNotificationData;
import com.sdc2ch.legacy.aiv.push.NotificationData;
import com.sdc2ch.legacy.aiv.push.NotificationDataMessage;
import com.sdc2ch.legacy.aiv.push.NotificationResponse;
import com.sdc2ch.require.config.IApplicationDbConfig.AIV;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@PropertySource(value = cp + AIV.ps)
public class MobileNotificationServiceImpl implements IMobileNotificationService {
	
	@Autowired IMobileAppService appSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MobileNotificationServiceImpl.class);
	
	private static final String AUTH_NAME = "Authorization";
	private static final String AUTH_VALUE = "key=AAAAH4NBUX0:APA91bE2UwEEpK1q1sVDrrKI3su9oXn66ilQsmCLLaaCN2hJ6SPa0La-5S134fqsoBty3P4riPJ6g-YEx3BmHu1fTBB4C04JHOA6L9tcOX0GB1hqmFz8nash7pcQHcJrxtCTl650joWL";
	private static final String URL = "https:

	
	private HttpClient client = new HttpClient();
	
	@Value("${aiv.push.auth.key}")
	private String authName;
	@Value("${aiv.push.auth.value}")
	private String authValue;
	@Value("${aiv.push.auth.url}")
	private String url;
	
	@Autowired
	private void subscribe(I2ChEventManager manager) {
		manager.subscribe(IFirebaseNotificationEvent.class).filter(e -> {
			try {
				
				NotificationResponse res = InstanceOf.when(e).instanceOf(IFirebaseNotificationEvent.class).then(f -> {
					
					IFirebaseNotificationEvent event = (IFirebaseNotificationEvent)e;
					AllocatedNotificationData data = new AllocatedNotificationData();
					data.setCommand(commandType.SDC2CH_FCM_EVENT);
					data.setBody(f.getContents());
					data.setTitle(f.getMobileNo());
					data.setAllocatedGroupId((Long) event.getDatas());
					NotificationDataMessage message = new NotificationDataMessage();
					message.setPriority(f.getPriority());
					message.setTo(f.getAppKey());
					message.setData(data);
					return send(message);
					
				}).otherwise(null);
				log.info("send noti {}" , res);
				
				
				if(res != null && res.getSuccess() != 1) {
					appSvc.findAppInfoByUser(e.user()).ifPresent(a -> {
						a.setValidTkn(false);
						appSvc.save(a);
					});
				}
				
			}catch (Exception ex) {
				log.error("{}", ex);
			}
		});
	}
	
	public NotificationResponse send(Notification noti) {
		 String gson = create().toJson(noti);
		 logger.info("send \r\n{}", gson);
		 return client.send(gson);
	}
	
	private Gson create() {
		return new GsonBuilder()

			     .enableComplexMapKeySerialization()
			     .serializeNulls()
			     .setDateFormat(DateFormat.LONG)

			     .setPrettyPrinting()
			     .setVersion(1.0)
			     .create();
	}
	
	private class HttpClient {
		
		private String CONTENT_TYPE = "application/json";
		
		private NotificationResponse send(String contents) {
			logger.info("content \r\n{}", contents);
			HttpURLConnection conn = null;
			InputStream ins = null;
			NotificationResponse res = null;
			try {
				URL url = new URL(URL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(1000);
				conn.setDoOutput(true);
				conn.setReadTimeout(3000);
				conn.setRequestMethod("POST");
				conn.addRequestProperty("Content-Type", CONTENT_TYPE);
				conn.addRequestProperty("Accept",CONTENT_TYPE);
				conn.addRequestProperty(AUTH_NAME, AUTH_VALUE);
				
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(contents.getBytes("UTF-8"));
				outputStream.flush();
				outputStream.close();
				
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					logger.warn("{}", conn.getContent());
				}
				
				ins = conn.getInputStream();
				
				if(ins != null) {
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
					
					String s;
					
					while((s = reader.readLine()) != null) {
						
						res = create().fromJson(s, NotificationResponse.class);

					}
					
					reader.close();
					
				}

				if(ins != null)
					ins.close();

			} catch (IOException e) {
				logger.error("{}", e);
			}finally {
				if(conn != null)
					conn.disconnect();
			}
			
			return res;
		}
	}


	public void sendCommandData(String tokenId, commandType tp) throws Exception{









	}
	
	
	public void sendCommandData(NotificationData data, String tokenId, com.sdc2ch.aiv.io.Notification.commandType tp) throws Exception{

		if(tokenId != null && !"".equals(tokenId)) {
			
			NotificationDataMessage message = new NotificationDataMessage();
			
			data.setCommand(tp);
			message.setPriority(Priority.high);
			message.setTo(tokenId);
			message.setData(data);
			
			
			NotificationResponse res = send(message);
			
			if(res == null || res.getFailure() == 1) {
				
				
				
			}
			
			logger.info("{}", res);
			
		}
	}
	public void sendNotificationPush(Notification message) throws Exception{

		NotificationResponse res = send(message);
		
		if(res == null || res.getFailure() == 1) {
			
			
			
		}
		
		logger.info("{}", res);
	}

}
