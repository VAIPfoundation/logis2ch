package com.sdc2ch.nfc.io;

import java.util.Date;

public interface NfcEventIO {
	int getId();
	int getDevdt();
	int getDevuid();
	String getUsrid();
	Integer getEvt();
	Date getSrvdt();

}
