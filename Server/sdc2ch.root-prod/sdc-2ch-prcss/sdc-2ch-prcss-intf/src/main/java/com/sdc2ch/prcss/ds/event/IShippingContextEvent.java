package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.require.event.I2ChInternalEvent;

public interface IShippingContextEvent extends I2ChInternalEvent<IShippingContextEvent> {
	
	public enum EventAction {
		MGR_ALLOCATED("배차확정이벤트"),
		MGR_CANCEL   ("배차취소이벤트"),
		GEO_ARRIVED  ("지오펜스도착이벤트"),
		GEO_ENTER    ("지오펜스진입이벤트"),
		GEO_EXITED   ("지오펜스진출이벤트"),
		USR_ST       ("사용자운행시작이벤트"),
		USR_FIN      ("사용자운행종료이벤트"),
		USR_CONFIRM  ("사용자배차확인이벤트"),
		USR_EB       ("사용자공상자입력이벤트"),
		NFC_TAG_LDNG ("NFC상차태깅이벤트"),
		NFC_TAG_OFFIC("NFC출근태깅이벤트"),
		BCN_ENTER    ("비콘진입이벤트"),
		BCN_EXITED   ("비콘진출이벤트"),
		SYS_FIN      ("시스템강제운행종료이벤트"),
		USR_ENTER    ("사용자공장도착이벤트"),
		USR_EXITED   ("사용지공장출발이벤트"),
		UNKNOWN      (null),
		;
		public String message;
		EventAction(String msg){
			message = msg;
		}
	}
	
	public enum EventBy {
		NFC,
		BEACON,
		MOBILE_WEB,
		GPS, SYSTEM, ADMIN;
	}
	
	public enum EventNm {
		FT_ENTER("통문(입)", EventAction.GEO_ENTER, EventAction.GEO_ARRIVED, EventAction.BCN_ENTER, EventAction.NFC_TAG_OFFIC, EventAction.USR_ENTER),
		LDNG_ST("상차시작", EventAction.NFC_TAG_LDNG),
		LDNG_ED("상차종료", EventAction.NFC_TAG_LDNG),
		FT_EXIT("통문(출)", EventAction.BCN_EXITED, EventAction.USR_EXITED, EventAction.GEO_EXITED),
		CC_ENTER("배송지(진입)", EventAction.GEO_ENTER),
		CC_ARRIVE("배송지도착", EventAction.GEO_ARRIVED),
		CC_TAKEOVER("상자회수(배송지)", EventAction.USR_EB),
		CC_DEPART("배송지출발", EventAction.GEO_EXITED),
		FT_RECOVER("상자회수(공장)"),
		REPORT("종료보고", EventAction.USR_ST, EventAction.SYS_FIN, EventAction.USR_FIN);
		public String eventTyNm;
		public EventAction[] actions;
		EventNm(String name, EventAction ...actions ) {
			this.eventTyNm = name;
			this.actions = actions;
		}
	}
	
	public IShippingContext getContext();
}
