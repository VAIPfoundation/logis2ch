package com.sdc2ch.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.api.CallEventSession;
import com.sdc2ch.api.IVRCallEventEndpoint;
import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.command.Command;
import com.sdc2ch.api.command.GatherCommand.GatherType;
import com.sdc2ch.api.enums.DriverScenario;
import com.sdc2ch.api.util.ArsUtil;
import com.sdc2ch.ars.enums.CallType;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.ars.repo.domain.T_ARS_MENT;
import com.sdc2ch.ars.repo.domain.dao.T_ArsMentRepository;
import com.sdc2ch.repo.io.TmsDriverIO.FactoryTel;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DriverCallServiceImpl extends AbstractCallServiceImpl {

	private SenderType supported = SenderType.DRIVER;
	
	@Autowired
	private T_ArsMentRepository repo;
	
	
	@Autowired
	public void onLoad(T_ArsMentRepository repo) {
		Stream.of(DriverScenario.values())
		.map(s ->repo.findByMessageTy(s.name())
				.orElseGet(() ->repo.save(new T_ARS_MENT(s.name(), s.getMessage()))))
		.collect(Collectors.toMap(a -> DriverScenario.valueOf(a.getMessageTy()), Function.identity()));
	}
	
	@Override
	public boolean supported(SenderType type) {
		return supported == type;
	}
	
	@Override
	public List<Command> makeCommand(CallEventSession session) {
		IArsEvent arsEvt = session.getEvent();
		String tel = null;
		String message = null;

		if(arsEvt != null) {
			
			if(CallType.FACTORY == arsEvt.getCallType()) {
				return callFactory(arsEvt.getFctryCd());
			}
			
			if(CallType.CUSTOMER == arsEvt.getCallType()) {
				
				if(StringUtils.isEmpty(arsEvt)) {
					return hangupCall();
					
				}else {
					return callCustomer(arsEvt.getDlvyLcMobile(), arsEvt.getDlvyLcNm());
				}
			}
			return Arrays.asList(super.createSayCommand(message), super.createRouteCommand(tel, message));
		}else {
			
			
			return Arrays.asList(super.createGatherCommand(GatherType.dtmf, 1, 3, getMessage(DriverScenario.D_GATHER_CALL)));
		}
	}
	
	private List<Command> hangupCall() {
		return Arrays.asList(super.createSayCommand(getMessage(DriverScenario.D_NO_REGIST_CUSTOMER)), super.createHangUpCommand());
	}

	@Override
	public List<Command> welcome(CallEventSession session, EICNCallEvent event) {
		
		
		switch (event.getEventName()) {
		case HANGUP:
			return Arrays.asList(super.createHangUpCommand());
		case INBOUND_CALL:
			return receiveInboundCall(session, event);
		case GATHER:
			return gatherCall(session, event);
		default:
			break;
		}
		
		return null;
	}
	
	private List<Command> gatherCall(CallEventSession session, EICNCallEvent event) {
		
		Integer digits = event.getDtmf();

		if(digits == null) {
			String message = null;
			if(session.isExpired()) {
				message = getMessage(DriverScenario.D_GATHER_TIMEOUT_CALL) + getMessage(DriverScenario.D_GATHER_HANGUP_CALL);
				return Arrays.asList(super.createSayCommand(message), super.createHangUpCommand());
			}else {
				message = getMessage(DriverScenario.D_GATHER_TIMEOUT_CALL) + getMessage(DriverScenario.D_GATHER_CALL);
				return Arrays.asList(super.createGatherCommand(GatherType.dtmf, 1, 3, message));
			}

		}
		return digits == 1 ? callCustomer(session.getPlan().getMobileNo(), session.getPlan().getDriverNm()) : callFactory(session.getPlan().getFctryCd());
	}

	private List<Command> receiveInboundCall(CallEventSession session, EICNCallEvent event) {
		
		IArsEvent arsEvt = session.getEvent();
		String mdn = event.getPhoneNumber();
		String message = null;
		
		TmsPlanIO plan = null;
		
		
		if(arsEvt == null) {
			List<TmsPlanIO> plans = findTmPlansByCustomerMobileNoByErp(mdn);
			
			if(checkCall(plans)) {
				return Arrays.asList(super.createSayCommand(getMessage(DriverScenario.D_NO_REGIST)),
						super.createHangUpCommand());
			}
			
			
			plan = plans.get(0);
			message = String.format(getMessage(DriverScenario.D_WELCOME_CALL), ArsUtil.convertName(plan.getDlvyLoNm()));
			session.setPlan(plan);
		}else {
			message = getMessage(DriverScenario.D_WELCOME_CALL);
			message = String.format(message, ArsUtil.convertName(arsEvt.getDlvyLcNm()));
		}
		if(StringUtils.isEmpty(message))
			message = DriverScenario.D_WELCOME_CALL.getMessage().replaceAll("%", "");
		return Arrays.asList(
				super.createSayCommand(message),
				super.createPauseCommand(1),
				super.createRedirectCommand(makeUrl(mdn, session.getRequest().getPort(), session.getRequest().getCallSid())));
		
	}

	private String getMessage(DriverScenario cs) {
		return repo.findByMessageTy(cs.name()).get().getMessage();
	}
	
	private boolean checkCall(List<TmsPlanIO> plan) {
		for(int i=0;i<plan.size();i++) {
			log.info("## ARS ## DriverCallServiceImpl.checkCall => cMobileNo={}, driverNm={}, mobileNo={}", plan.get(i).getCMobileNo(), plan.get(i).getDriverNm(), plan.get(i).getMobileNo());
		}
		
		return plan == null || plan.isEmpty() || 
				plan.stream().filter(p -> !StringUtils.isEmpty( p.getCMobileNo()) 
						|| !StringUtils.isEmpty( p.getCMobileNo())).findFirst().orElse(null) == null;
	}

	private List<TmsPlanIO> findTmPlansByCustomerMobileNo(String mdn) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return planSvc.findTmPlansByCustomerMobileNo(fmt.format(new Date()), mdn);
	}
	
	private List<TmsPlanIO> findTmPlansByCustomerMobileNoByErp(String mdn) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		log.info("## ARS ## driverCallServiceImpl.findTmPlansByCustomerMobileNoByErp => date={}, mdn={}", fmt.format(new Date()), mdn);
		return planSvc.findTmPlansByCustomerMobileNoByErp(fmt.format(new Date()), mdn);
	}

	@Override
	protected SenderType supportedType() {
		return supported;
	}
	
	private List<Command> callFactory(String fctryCd) {
		
		if ( containsTime() ) {
			fctryCd = "1D1";
		}
		String tel = FactoryTel.findTel(fctryCd).replaceAll("-", "");
		String name = FactoryType.convert(fctryCd).getName() + "공장";
		String message = String.format(getMessage(DriverScenario.D_FCTRY_CALL), name);
		return Arrays.asList(super.createSayCommand(message), super.createRouteCommand(tel, message));
	}

	private List<Command> callCustomer(String customerMdn, String dlvyLcNm) {
		String tel = customerMdn.replaceAll("-", "");
		String name = dlvyLcNm;
		String message = String.format(getMessage(DriverScenario.D_DRIVER_CALL), name);
		return Arrays.asList(super.createSayCommand(message), super.createRouteCommand(tel, message));
	}

}
