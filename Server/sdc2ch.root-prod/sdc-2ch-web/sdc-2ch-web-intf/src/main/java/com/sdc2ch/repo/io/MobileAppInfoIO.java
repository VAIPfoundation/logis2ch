package com.sdc2ch.repo.io;

import java.util.List;

public interface MobileAppInfoIO {
	void setValidTkn(boolean valid);
	String getAppTkn();
	boolean isValidTkn();
	List<TosIO> getTosses();
	MobileApkInfoIO getApk();
}
