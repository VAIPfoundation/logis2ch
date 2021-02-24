package com.sdc2ch.prcss.eb.io;

public interface EmptyboxIO {

	public enum Cause {
		NO_USER,
		NONE_BOX,
		BAD_BOX
	}

	public enum ModifyCause {
		DIFF_QTY,
		LIMIT_SPACE
	}
	
	void setConfirm(boolean confirm);
	

	String getRegUser();
	String getRouteNo();
	String getDlvyDe();
	String getStopCd();
	Cause getCause();
	ModifyCause getModifyCause();
	Long getId();

}
