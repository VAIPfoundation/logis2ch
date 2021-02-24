package com.sdc2ch.prcss.ss.template;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;

public abstract class AbstractTemplate {
	
	public interface State {
		String getStateNm();
		String getStateCd();
		String getEnName();
		EventNm[] getEventNm();
	}
	public enum DlvyState implements State {
		ENTER("통문(입)", "PASSAGE", EventNm.FT_ENTER),
		LDNG("상차", "LDNG", EventNm.LDNG_ST, EventNm.LDNG_ED),
		EXIT("통문(출)", "PASSAGE", EventNm.FT_EXIT),
		DELIVERY("배송", "DELIVERY", EventNm.CC_ENTER, EventNm.CC_ARRIVE, EventNm.CC_TAKEOVER, EventNm.CC_DEPART),
		TURN("회차", "TURN", EventNm.FT_ENTER, EventNm.FT_RECOVER),
		REPORT("보고", "REPORT", EventNm.REPORT)
		;
		public String stateNm;
		public String stateCd;
		public EventNm[] eventTys;
		DlvyState(String name, String stateCd, EventNm ... ty){
			this.stateNm = name;
			this.stateCd = stateCd;
			this.eventTys = ty;
		}
		@Override
		public String getStateNm() {
			return stateNm;
		}
		@Override
		public EventNm[] getEventNm() {
			return eventTys;
		}
		@Override
		public String toString() {
			return this.name();
		}
		@Override
		public String getStateCd() {
			return stateCd;
		}
		@Override
		public String getEnName() {
			return this.name();
		}
	}
	public enum TransState implements State{
		ENTER("통문(입)", EventNm.FT_ENTER),
		LDNG("상차", EventNm.LDNG_ST, EventNm.LDNG_ED),
		EXIT("통문(출)", EventNm.FT_EXIT),
		REPORT("보고", EventNm.REPORT)
		;
		public String stateNm;
		public EventNm[] eventTys;
		TransState(String name, EventNm ... ty){
			this.stateNm = name;
			this.eventTys = ty;
		}
		@Override
		public String getStateNm() {
			return stateNm;
		}
		@Override
		public EventNm[] getEventNm() {
			return eventTys;
		}
		@Override
		public String toString() {
			return this.name();
		}
		@Override
		public String getStateCd() {
			return this.name();
		}
		@Override
		public String getEnName() {
			return this.name();
		}
	}

}
