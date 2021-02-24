package com.sdc2ch.web.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sdc2ch.service.common.IAlarmService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AlarmSchedularComponent {

	@Autowired
	IAlarmService alarmSvc;

	@Scheduled(cron="0 0/15 * * * *")
	public void run() {
		long s = System.currentTimeMillis();
		log.info("Alarm Check Schedular Start");
		
		alarmSvc.chkTotalAlarmAndSendMsg(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		log.info("Alarm Check Schedular End : working  {}ms", System.currentTimeMillis() - s);
	}
}
