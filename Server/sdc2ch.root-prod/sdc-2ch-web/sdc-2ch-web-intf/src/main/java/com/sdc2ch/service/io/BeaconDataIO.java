package com.sdc2ch.service.io;

import java.util.Date;

public interface BeaconDataIO {
	String getBconId();
	String getMdn();
	String getInoutType();
	Date getDataDt();
}
