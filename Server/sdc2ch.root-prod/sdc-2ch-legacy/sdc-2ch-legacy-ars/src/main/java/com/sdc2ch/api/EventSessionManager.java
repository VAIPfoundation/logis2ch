package com.sdc2ch.api;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.cache.Cache;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventSessionManager {


	private Cache<String, CallEventSession> sessions;

	@Autowired
	private void onLoad(I2ChEventManager manager) {
		I2ChEventConsumer<IArsEvent> sub = manager.subscribe(IArsEvent.class);
		try {
			sub.filter(e -> accept(e));
		}catch (Exception e) {
			log.error("{}", e);
		}

		sessions = CacheMenager.<String, CallEventSession>builder().expiredTime(30)
				.timeUnit(TimeUnit.SECONDS).maxSize(100).build().expiredCache();
	}

	private void accept(I2ChEvent<IArsEvent> e) throws InterruptedException {
		IArsEvent ars = (IArsEvent) e;

		log.info("## ARS ## Call Type>>>{}", ars.getCallType());
		CallEventSession _session = null;
		switch (ars.getCallType()) {
		case CUSTOMER:
			_session = createSession(ars.getDriverMobile());
			_session.setEvent(ars);
			break;
		case DRIVER:
			if(!ars.getCallers().isEmpty()) {
				ars.getCallers().forEach(s -> {
					CallEventSession __session = createSession(s);
					__session.setEvent(ars);
				});
			}
			break;
		case FACTORY:
			if(!ars.getCallers().isEmpty()) {
				ars.getCallers().forEach(s -> {
					CallEventSession __session = createSession(s);
					__session.setEvent(ars);
				});
			}
			_session = createSession(ars.getDriverMobile());
			_session.setEvent(ars);
			break;

		default:
			break;
		}

	}

	public CallEventSession getSession(String mdn) {
		try {
			return mdn == null ? null : sessions.getIfPresent(mdn);
		}catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public CallEventSession createSession(String mdn) {

		CallEventSession session = null;

		try {
			session = sessions.getIfPresent(mdn);
			if(session == null) {
				session = build();
			}
		} catch (Exception e) {
			session = build();
		}

		if(StringUtils.isEmpty(mdn)) {
			mdn = "nulldafeDefaultSession";
		}
		sessions.put(mdn, session);
		return session;

	}


	private CallEventSession build() {
		CallEventSession session = new CallEventSession();
		return session;
	}

	public void removeSession(String mdn) {
		sessions.invalidate(mdn);
	}

}
