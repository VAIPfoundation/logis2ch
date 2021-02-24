package com.sdc2ch.service.common.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.prcss.msngr.IMessengerService;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.domain.IUserRole;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.service.admin.IAlarmSetupService;
import com.sdc2ch.service.admin.IAlarmSttusService;
import com.sdc2ch.service.common.IAlarmService;
import com.sdc2ch.service.common.model.alarm.ChkNoCnfirmAlarmDto;
import com.sdc2ch.service.common.model.alarm.ChkNoEtyBoxDto;
import com.sdc2ch.service.common.model.alarm.ChkNoLoadingDto;
import com.sdc2ch.service.common.model.alarm.ChkNoPassageDto;
import com.sdc2ch.service.common.model.alarm.ChkOffGpsDto;
import com.sdc2ch.service.common.model.alarm.ChkResultDto;
import com.sdc2ch.service.common.model.alarm.ChkTotalAlarmDto;
import com.sdc2ch.service.common.model.alarm.IChkAlarmDto;
import com.sdc2ch.service.event.model.AppPushEvent;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.web.admin.repo.dao.T_AlarmSttusRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_SETUP;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmSetupType;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmType;
import com.sdc2ch.web.admin.repo.domain.alloc.type.TrnsmisType;
import com.sdc2ch.web.admin.repo.dto.AlarmChkDto;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlarmServiceImpl implements IAlarmService {

	@Autowired
	private IAlarmSttusService alarmSttusSvc; 
	@Autowired
	private T_AlarmSttusRepository alarmSttusRepo;

	@Autowired
	private IAlarmSetupService alarmSetupSvc; 
	@Autowired
	private IMessengerService messengerSvc; 
	@Autowired
	private IMobileAppService appSvc; 

	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;


	@Value("${alarm.result.path:C:\\Apache24\\htdocs}")
	private String targetPath;

	@Value("${app.baseUrl:http:
	private String baseUrl;

	private final String TARGET_FORDER = "alarmResult";

	
	public void sendSms() {

	}

	@Autowired
	public void init(I2ChEventManager manager) {
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
	}


	
	@Override
	public void chkTotalAlarmAndSendMsg(String dlvyDe) {
		List<FactoryType> fctryTyList = Arrays.asList(FactoryType.values());

		for(FactoryType fctryTy : fctryTyList) {
			
			if(fctryTy == FactoryType.FFFF) {
				break;
			}
			ChkTotalAlarmDto result = new ChkTotalAlarmDto();
			List<IChkAlarmDto> dtoList = new ArrayList<IChkAlarmDto>();

			dtoList.add(chkCnfirmCaralcDtls(fctryTy.getCode() ,dlvyDe)); 
			dtoList.add(chkNoPassage(fctryTy.getCode(), dlvyDe)); 
			dtoList.add(chkNoLoading(fctryTy.getCode(), dlvyDe)); 
			dtoList.add(chkNoEtyBox(fctryTy.getCode(), dlvyDe)); 
			dtoList.add(chkOffGps(fctryTy.getCode(), dlvyDe)); 

			result.setFactoryTy(fctryTy);
			result.setList(dtoList);
			if(result.getChkCnt() > 0 ) {
				

				String fctryCd = result.getFactoryTy().getCode();
				
				String fileName = fctryCd+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".html";



				
				makeHtmlResult(fileName, result.getContents());

				if(validSendMsg(LocalDateTime.now(), result.getFactoryTy().getCode())){
					
					String targetUser = alarmSetupSvc.searchAlarmSetupByAll(result.getFactoryTy().getCode(), null, null)
							.stream().filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && result.getFactoryTy().getCode().equals(o.getFctryCd()))
							.findFirst()
							.map(v-> v.getValue())
							.orElse(AlarmSetupType.ALARM_TARGET_USER.getDefaultValue());
					
					String url = String.join("/", baseUrl, fileName);
					StringBuffer sbMsg = new StringBuffer();
					sbMsg.append("상세내용 확인 URL <br/>");
					sbMsg.append("<A href='"+url+"'> ");
					sbMsg.append(url);
					sbMsg.append("</A>");
					sendMsngrMsg("aivision12", "2CH 시스템", targetUser, result.getTitle(), sbMsg.toString());
				}
			}
		}

	}

	
	private boolean makeHtmlResult(String fileName, String text) {
		Path path = Paths.get(targetPath, TARGET_FORDER, fileName);
		BufferedWriter fw = null;
		try {
			if (Files.notExists(path.getParent())) {
				Files.createDirectories(path.getParent());
			}
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile(), true), "UTF-8"));
			fw.write(text);
			fw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {

				}
			}
		}

	}


	@Override
	public ChkNoPassageDto chkNoPassage(String fctryCd, String dlvyDe) {
		List<T_ALARM_SETUP> alarmSetupList = alarmSetupSvc.searchAlarmSetupByAll(fctryCd, null, null);
		
		ChkNoPassageDto result = new ChkNoPassageDto();
		result.setAlarmTy(AlarmType.NO_PASSAGE);
		
		Integer rangeValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.FCT_ENTER_LDNG_RANGE
						&& fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.FCT_ENTER_LDNG_RANGE.getDefaultValue()));
		String targetUser = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> v.getValue()).orElse("aivision13");

		
		List<AlarmChkDto> DelayPassages = alarmSttusRepo.findNoPassageByDlvyDeAndFctryCdAndState(dlvyDe, fctryCd, rangeValue);

		
		List<T_ALARM_STTUS> alarmSttusList = DelayPassages.stream()
				.map(delayDto -> cvtAlarmSttus(AlarmType.NO_PASSAGE, delayDto, targetUser, TrnsmisType.MSNGR_MSG))
				.filter(entity -> entity !=null)
				.collect(Collectors.toList());
		alarmSttusSvc.save(alarmSttusList);

		
		List<ChkResultDto> resultList = DelayPassages.stream()
		.map(delayDto -> cvtChkResultDto(AlarmType.NO_PASSAGE, delayDto))
		.filter(dto -> dto!=null)
		.collect(Collectors.toList());

		result.setResultDto(resultList);
		return result;
	}

	@Override
	public ChkNoEtyBoxDto chkNoEtyBox(String fctryCd, String dlvyDe) {
		List<T_ALARM_SETUP> alarmSetupList = alarmSetupSvc.searchAlarmSetupByAll(fctryCd, null, null);

		
		Integer rangeValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ETY_BOX_NOCNFIRM_RANGE && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.ETY_BOX_NOCNFIRM_RANGE.getDefaultValue()));
		
		Integer cycleValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ETY_BOX_NOCNFIRM_CYCLE && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.ETY_BOX_NOCNFIRM_CYCLE.getDefaultValue()));

		String targetUser = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> v.getValue()).orElse(AlarmSetupType.ALARM_TARGET_USER.getDefaultValue());



		
		ChkNoEtyBoxDto result = new ChkNoEtyBoxDto();
		result.setAlarmTy(AlarmType.ETY_BOX_DCSN_NO_OPRTN);

		
		List<AlarmChkDto> delayEmptyBoxs = alarmSttusRepo.findDelayEmptyBoxByDlvyDeAndFctryCd(dlvyDe, fctryCd, rangeValue, cycleValue);








		
		
		
		if (false) {

			delayEmptyBoxs.stream().filter(dto -> dto != null).forEach(dto -> {
				String msg = String.format(AlarmType.ETY_BOX_DCSN_NO_OPRTN.getPushMsg(), dto.getDlvyDe(), dto.getRouteNo(), dto.getStopNm());
				sendPush(AlarmType.ETY_BOX_DCSN_NO_OPRTN, dto, -1L, msg);
			});


		}

		
		List<T_ALARM_STTUS> alarmSttusList = delayEmptyBoxs.stream()
				.map(dto -> cvtAlarmSttus(AlarmType.ETY_BOX_DCSN_NO_OPRTN, dto, targetUser, TrnsmisType.MSNGR_MSG))
				.filter(entity -> entity !=null)
				.collect(Collectors.toList());
		alarmSttusSvc.save(alarmSttusList);

		List<ChkResultDto> resultList = delayEmptyBoxs.stream()
		.map(dto -> cvtChkResultDto(AlarmType.ETY_BOX_DCSN_NO_OPRTN, dto))
		.filter(dto -> dto!=null)
		.collect(Collectors.toList());
		result.setResultDto(resultList);
		return result;
	}

	private void sendPush(AlarmType alarmTy, AlarmChkDto dto, Long groupId, String msg) {
		IUser user = new IUser() {
			@Override
			public String getUsername() {
				
				return dto.getDriverCd();
			}

			@Override
			public IUserDetails getUserDetails() {
				
				return null;
			}

			@Override
			public List<IUserRole> getRoles() {
				
				return null;
			}

			@Override
			public String getPassword() {
				
				return null;
			}

			@Override
			public String getMobileNo() {
				
				return dto.getMobileNo();
			}

			@Override
			public String getFctryCd() {
				
				return dto.getFctryCd();
			}
		};

		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);
		if(appInfo != null) {
			eventPush.fireEvent(
					AppPushEvent.builder()
					.appKey(appInfo.getAppTkn())
					.contents(msg)
					.datas(-1L)
					.priority(Priority.high)
					.mobileNo(user.getMobileNo())
					.user(user)
					.build());
		}
		alarmSttusSvc.save(cvtAlarmSttus(AlarmType.ETY_BOX_DCSN_NO_OPRTN, dto, user.getUsername(), TrnsmisType.PUSH));
	}


	
	@Override
	public ChkNoLoadingDto chkNoLoading(String fctryCd, String dlvyDe) {


		List<T_ALARM_SETUP> alarmSetupList = alarmSetupSvc.searchAlarmSetupByAll(fctryCd, null, null);
		
		ChkNoLoadingDto result = new ChkNoLoadingDto();
		result.setAlarmTy(AlarmType.NO_LOADING);


		Integer rangeValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.NO_LDNG_RANGE
						&& fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.NO_LDNG_RANGE.getDefaultValue()));

		String targetUser = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> v.getValue()).orElse(AlarmSetupType.ALARM_TARGET_USER.getDefaultValue());

		
		List<AlarmChkDto> DelayPassages = alarmSttusRepo.findNoLoadingByDlvyDeAndFctryCdAndState(dlvyDe, fctryCd, rangeValue);

		
		List<T_ALARM_STTUS> alarmSttusList = DelayPassages.stream()
				.map(delayDto -> cvtAlarmSttus(AlarmType.NO_LOADING, delayDto, targetUser, TrnsmisType.MSNGR_MSG))
				.filter(entity -> entity !=null)
				.collect(Collectors.toList());
		alarmSttusSvc.save(alarmSttusList);

		
		List<ChkResultDto> resultList = DelayPassages.stream()
		.map(delayDto -> cvtChkResultDto(AlarmType.NO_LOADING, delayDto))
		.filter(dto -> dto!=null)
		.collect(Collectors.toList());

		result.setResultDto(resultList);
		return result;

	}

	
	@Override
	public ChkNoCnfirmAlarmDto chkCnfirmCaralcDtls(String fctryCd, String dlvyDe) {
		
		List<T_ALARM_SETUP> alarmSetupList = alarmSetupSvc.searchAlarmSetupByAll(fctryCd, null, null);
		
		ChkNoCnfirmAlarmDto result = new ChkNoCnfirmAlarmDto();
		result.setAlarmTy(AlarmType.NO_CNFRM_CARALC_DTLS);

		Integer rangeValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.CARALC_NOCNFIRM_RANGE
						&& fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.CARALC_NOCNFIRM_RANGE.getDefaultValue()));

		Integer cycleValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.CARALC_NOCNFIRM_CYCLE
						&& fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.CARALC_NOCNFIRM_CYCLE.getDefaultValue()));

		String targetUser = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> v.getValue()).orElse(AlarmSetupType.ALARM_TARGET_USER.getDefaultValue());

		
		List<AlarmChkDto> noConfirms = alarmSttusRepo.findAlcNoConfirmByDlvyDeAndFctryCd(dlvyDe, fctryCd, rangeValue, cycleValue);


		
		List<T_ALARM_STTUS> alarmSttusList = noConfirms.stream()
				.map(delayDto -> cvtAlarmSttus(AlarmType.NO_CNFRM_CARALC_DTLS, delayDto, targetUser, TrnsmisType.MSNGR_MSG))
				.filter(entity -> entity !=null)
				.collect(Collectors.toList());
		alarmSttusSvc.save(alarmSttusList);

		
		List<ChkResultDto> resultList = noConfirms.stream()
		.map(delayDto -> cvtChkResultDto(AlarmType.NO_CNFRM_CARALC_DTLS, delayDto))
		.filter(dto -> dto!=null)
		.collect(Collectors.toList());

		result.setResultDto(resultList);
		return result;
	}

	
	@Override
	public ChkOffGpsDto chkOffGps(String fctryCd, String dlvyDe) {
		List<T_ALARM_SETUP> alarmSetupList = alarmSetupSvc.searchAlarmSetupByAll(fctryCd, null, null);

		
		Integer rangeValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.GPS_OFF_RANGE && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.GPS_OFF_RANGE.getDefaultValue()));
		
		Integer cycleValue = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.GPS_OFF_CYCLE && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> Integer.valueOf(v.getValue())).orElse(Integer.valueOf(AlarmSetupType.GPS_OFF_CYCLE.getDefaultValue()));
		
		String targetUser = alarmSetupList.stream()
				.filter(o -> o.getSetupTy() == AlarmSetupType.ALARM_TARGET_USER && fctryCd.equals(o.getFctryCd()))
				.findFirst().map(v -> v.getValue()).orElse(AlarmSetupType.ALARM_TARGET_USER.getDefaultValue());


		
		ChkOffGpsDto result = new ChkOffGpsDto();
		result.setAlarmTy(AlarmType.GPS_OFF);

		
		List<AlarmChkDto> offGpsList = alarmSttusRepo.findOffGpsByDlvyDeAndFctryCd(dlvyDe, fctryCd, rangeValue, cycleValue);

		List<T_ALARM_STTUS> alarmSttusList = offGpsList.stream()
				.map(dto -> cvtAlarmSttus(AlarmType.GPS_OFF, dto, targetUser, TrnsmisType.MSNGR_MSG))
				.filter(entity -> entity != null)
				.collect(Collectors.toList());

		alarmSttusSvc.save(alarmSttusList);

		List<ChkResultDto> resultList = offGpsList.stream()
		.map(dto -> cvtChkResultDto(AlarmType.GPS_OFF, dto))
		.filter(dto -> dto!=null)
		.collect(Collectors.toList());
		result.setResultDto(resultList);

		return result;
	}


	@Override
	public void sendMsngrMsg(String trnmisUserId, String trnsmisUserNm, String rcverUserId, String title, String body) {
		messengerSvc.sendMessengerMsg(trnmisUserId, trnsmisUserNm, rcverUserId, title, body);
	}

	
	private boolean validSendMsg(LocalDateTime now,String fctryCd) {
		if (now.getHour() >= 7 && now.getHour() < 19) {
			return true;
		} else if ((now.getHour() <7 && now.getHour() >=19) && "1D1".equals(fctryCd)) {	
			return true;
		}
		return false;
	}

	
	private ChkResultDto cvtChkResultDto(AlarmType alarmTy, AlarmChkDto dto) {
		if(dto == null) {
			return null;
		}

		ChkResultDto result = new ChkResultDto();
		BeanUtils.copyProperties(dto, result);
		result.setChkDt(dto.getChkDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		result.setChkBaseDt(dto.getChkBaseDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		result.setAlarmTy(alarmTy);
		return result;
	}

	
	private T_ALARM_STTUS cvtAlarmSttus(AlarmType alarmTy, AlarmChkDto dto, String rcverUser, TrnsmisType trnTy) {
		if(dto == null) {
			return null;
		}
		T_ALARM_STTUS alarmSttus = new T_ALARM_STTUS();
		alarmSttus.setAlarmTy(alarmTy);
		alarmSttus.setFctryCd(dto.getFctryCd());
		alarmSttus.setDlvyDe(dto.getDlvyDe());
		alarmSttus.setVrn(dto.getVrn());
		alarmSttus.setCaralcTy(dto.getCaralcTy());
		alarmSttus.setRouteNo(dto.getRouteNo());
		alarmSttus.setMobileNo(dto.getMobileNo());
		alarmSttus.setStopCd(dto.getStopCd());
		alarmSttus.setTrnsmisTy(trnTy);
		alarmSttus.setTrnsmisDt(Date.from(dto.getChkDt().atZone(ZoneId.systemDefault()).toInstant()));
		alarmSttus.setRcverUser(rcverUser);
		alarmSttus.setTrnsmisUser("system");


		return alarmSttus;
	}

}
