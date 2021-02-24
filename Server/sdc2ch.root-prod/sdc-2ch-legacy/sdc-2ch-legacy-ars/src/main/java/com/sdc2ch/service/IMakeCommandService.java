package com.sdc2ch.service;

import com.sdc2ch.api.callevent.EventName;

public interface IMakeCommandService {
	boolean supported(EventName eventName);

}
