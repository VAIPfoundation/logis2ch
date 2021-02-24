package com.sdc2ch.prcss.ds.t.chain.state.action;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IAutoDoorEvent;
import com.sdc2ch.prcss.ds.event.IBeconEnterEvent;
import com.sdc2ch.prcss.ds.event.IBeconEvent.BeconEventType;
import com.sdc2ch.prcss.ds.event.IBeconExitedEvent;
import com.sdc2ch.prcss.ds.event.IEmptyboxConfirmEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceArrivedEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEnterEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent.GeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceExitedEvent;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.event.INfcTagEvent;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.ArrivedState;
import com.sdc2ch.prcss.ds.t.chain.state.DepartState;
import com.sdc2ch.prcss.ds.t.chain.state.EnterState;
import com.sdc2ch.prcss.ds.t.chain.state.FinishState;
import com.sdc2ch.prcss.ds.t.chain.state.LoadingState;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;
import com.sdc2ch.prcss.ds.t.chain.state.TurnningState;
import com.sdc2ch.prcss.ds.t.chain.state.UnLoadingState;
import com.sdc2ch.tms.enums.FactoryType;

public class ActionEventBuilder {
	
	private ShippingChain chain;
	
	private ActionEventBuilder() {
	}
	
	public static ActionEventBuilder builder() { 
		return new ActionEventBuilder();
	}
	
	
	public ShippingChain build() {
		
		chain.getShippingStates().stream().forEach(s -> {
			
			if(s instanceof EnterState) {
				setEnterEventAction(s);
			}
			
			if(s instanceof ArrivedState) {
				setArrivedEventAction(s);
			}
			
			if(s instanceof UnLoadingState) {
				setUnloadingEventAction(s);
			}
			
			if(s instanceof LoadingState) {
				setLoadingEventAction(s);
			}
			
			if(s instanceof DepartState) {
				setDepartEventAction(s);
			}
			if(s instanceof TurnningState) {
				setTurnningEventAction(s);
			}
			if(s instanceof FinishState) {
				setFinishEventAction(s);
			}
		});
		return chain;
	}


	public ActionEventBuilder chain(ShippingChain chain) {
		this.chain = chain;
		return this;
	}
	
	
	
	private void setArrivedEventAction(ShippingStatus s) {
		
		
		if(chain.shipConfig.isFactory()) {
			
			
			
			ActionEvent action = new GeofenceAction(s, GeoFenceEvent.GEO_ARRIVE);
			action.setEventClass(IGeoFenceArrivedEvent.class);
			s.add(action);
			
			
			
			action = new BeaconAction(s, BeconEventType.BCON_ENTER);
			action.setEventClass(IBeconEnterEvent.class);
			s.add(action);
			
			
			
			if (chain.context.isFirstChain(chain)) {
				action = new NfcAction(s, ActionEventType.NFC_TAG_OFFIC);
				action.setEventClass(INfcTagEvent.class);
				s.add(action);
			}
			
			if(FactoryType.F4D1 == chain.shipConfig.fctryTy) {
				action = new AutoDoorAction(s);
				action.setEventClass(IAutoDoorEvent.class);
				s.add(action);
			}
		}else if(chain.shipConfig.isCustomerCenter()) {
			
			
			ActionEvent action = new GeofenceAction(s, GeoFenceEvent.GEO_ARRIVE);
			action.setEventClass(IGeoFenceArrivedEvent.class);
			s.add(action);
			
			



			
		}else if(chain.shipConfig.isWareHouse()) {
			
			ActionEvent action = new GeofenceAction(s, GeoFenceEvent.GEO_ARRIVE);
			action.setEventClass(IGeoFenceArrivedEvent.class);
			s.add(action);
		}
	}

	
	private void setUnloadingEventAction(ShippingStatus s) {
		
		if(chain.shipConfig.isFactory()) {
			
			ActionEvent action = new NfcAction(s, ActionEventType.NFC_TAG_LDNG);
			action.setEventClass(INfcTagEvent.class);
			s.add(action);
		}else if(chain.shipConfig.isCustomerCenter()) {
			
			ActionEvent action = new EmptyboxAction(s);
			action.setEventClass(IEmptyboxConfirmEvent.class);
			s.add(action);
		}else if(chain.shipConfig.isWareHouse()) {
			
		}
		
	}
	private void setLoadingEventAction(ShippingStatus s) {
		if(chain.shipConfig.isFactory()) {
			
			ActionEvent action = new NfcAction(s, ActionEventType.NFC_TAG_LDNG);
			action.setEventClass(INfcTagEvent.class);
			s.add(action);
		}else if(chain.shipConfig.isCustomerCenter()) {
			


		}else if(chain.shipConfig.isWareHouse()) {
			
		}
	}

	private void setDepartEventAction(ShippingStatus s) {
		
		
		ShippingStatus state = (ShippingStatus) s.findArriveState();
		

		if(chain.shipConfig.isFactory()) {
			
			ActionEvent action = new GeofenceExitedAction(s, GeoFenceEvent.GEO_EXIT);
			action.setEventClass(IGeoFenceExitedEvent.class);
			s.add(action);
			
			
			action = new BeaconExitAction(s, BeconEventType.BCON_EXITD);
			action.setEventClass(IBeconExitedEvent.class);
			s.add(action);
			




			
			
			
			if(FactoryType.F4D1 == chain.shipConfig.fctryTy) {
				action = new AutoDoorAction(s);
				action.setEventClass(IAutoDoorEvent.class);
				s.add(action);
			}
		}else if(chain.shipConfig.isCustomerCenter()) {
			
			ActionEvent action = new GeofenceExitedAction(s, GeoFenceEvent.GEO_EXIT);
			action.setEventClass(IGeoFenceExitedEvent.class);
			s.add(action);
		}else if(chain.shipConfig.isWareHouse()) {
			
			ActionEvent action = new GeofenceExitedAction(s, GeoFenceEvent.GEO_EXIT);
			action.setEventClass(IGeoFenceExitedEvent.class);
			s.add(action);
		}
	}
	
	private void setEnterEventAction(ShippingStatus s) {
		ActionEvent action = new GeofenceAction(s, GeoFenceEvent.GEO_ENTER);
		action.setEventClass(IGeoFenceEnterEvent.class);
		s.add(action);
	}
	
	
	private void setTurnningEventAction(ShippingStatus s) {
		














	}
	
	private void setFinishEventAction(ShippingStatus s) {
		ActionEvent action = new MobileEventAction(s, MobileEventActionType.FINISH_JOB);
		action.setEventClass(IMobileActionEvent.class);
		s.add(action);














	}
}
