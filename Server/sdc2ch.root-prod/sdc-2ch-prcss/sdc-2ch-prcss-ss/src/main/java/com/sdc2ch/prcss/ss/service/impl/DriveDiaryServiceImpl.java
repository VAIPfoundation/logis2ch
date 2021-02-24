package com.sdc2ch.prcss.ss.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ss.executor.EventExecuteFactory;
import com.sdc2ch.prcss.ss.executor.EventExecuteFactory.EventExecuteFactoryBuilder;
import com.sdc2ch.prcss.ss.repo.T_DriveDiaryRepository;
import com.sdc2ch.prcss.ss.repo.domain.T_DRIVE_DIARY;
import com.sdc2ch.prcss.ss.service.IDriveDiaryService;
import com.sdc2ch.prcss.ss.template.AbstractGradeTemplate.GradeTy;
import com.sdc2ch.prcss.ss.template.StateTemplate;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsShippingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DriveDiaryServiceImpl implements IDriveDiaryService {

	@Autowired ITmsShippingService shippingSvc;
	@Autowired T_DriveDiaryRepository repo;
	@Autowired GradeServiceImpl gradeSvc;
	@Autowired DrivingServiceImpl drvSvc;

	private I2ChEventConsumer<IShippingContextEvent> contextEvent;

	@Autowired
	private void regist(I2ChEventManager manager) {
		contextEvent = manager.subscribe(IShippingContextEvent.class);
		contextEvent.filter(e -> onReceive(e));
	}


	@Override
	public List<T_DRIVE_DIARY> findAllByAllocateGId(Long gid) {
		return repo.findAllByAclGroupId(gid);
	}

	@Override
	public List<T_DRIVE_DIARY> findAllByUserNameAndDlvyDe(String username, String dlvyDe) {
		return repo.findAllByDriverCdAndDlvyDe(username, dlvyDe);
	}

	@Override
	public List<T_DRIVE_DIARY> findAllByVrnAndDlvyDe(String vrn, String dlvyDe) {
		return repo.findAllByVrnAndDlvyDe(vrn, dlvyDe);
	}



	private void onReceive(I2ChEvent<IShippingContextEvent> e) {

		try {

			IShippingContextEvent event = (IShippingContextEvent) e;
			IShippingContext context = event.getContext();
			List<TmsPlanIO> shippings = context.getShippings();

			if(shippings != null) {

				StateTemplate template = new StateTemplate(shippingSvc);

				Stream<TemplateVo> vos = Stream.of();

				for(TmsPlanIO plan : shippings) {
					Stream<TemplateVo> stmp = template.getTemplate(plan);	
					if(stmp != null){
						vos = Stream.concat(vos, stmp);
					}
				}

				List<ShippingStateEvent> events = context.findByEventActions(EventAction.values());	







				BlockingQueue<ShippingStateEvent> queue = new LinkedBlockingQueue<>(events);
				List<TemplateVo> tmps = vos.collect(Collectors.toList());

				if(!tmps.isEmpty()) {

					while(!queue.isEmpty()) {

						ShippingStateEvent sse = queue.take();


						outer : for(int i = 0 ; i < tmps.size() ; i++) {
							TemplateVo vo = tmps.get(i);

							EventAction[] actions = vo.getEventNm().actions;

							if(actions != null) {


								

								if( vo.getRouteNo().equals(sse.getRouteNo())

										&& "TURN".equals(vo.getState().getStateCd())
										&& sse.getEventAct() == EventAction.USR_ENTER
										) {

									vo.add(sse);	
									setReadonly(tmps, i);	
									

										break outer;


								} else if(vo.getRouteNo().equals(sse.getRouteNo())
										&& Arrays.asList(actions).contains(sse.getEventAct())
										&& vo.getStopCd().equals(sse.getStopCd())

										) {



									vo.add(sse);	
									setReadonly(tmps, i);	
									
									
									if(EventNm.LDNG_ST != vo.getEventNm() && EventNm.FT_ENTER != vo.getEventNm()) {
										break outer;
									}

									
								}else {

								}
							}

						}

					}


					
					
					EventExecuteFactory factory = EventExecuteFactoryBuilder.builder().context(context).build();

					List<TemplateVo> results = tmps.stream().map(vo -> {
						vo.setGid(context.getGroupId());
						
						factory.decideEvent2(vo);
						return vo;

					}).collect(Collectors.toList());






					results = gradeSvc.analsGradeScope(results);	
					results = gradeSvc.analsPointScope(results);	
					results = save(results, event.user());			








				}


			}
		}catch (Exception ex) {
			log.error("{}", ex);
		}

	}


	private void setReadonly(List<TemplateVo> tmps, int idx) {
		if(idx > 0) {
			for(int i = 0 ; i < idx-1 ; i++) {
				tmps.get(i).setReadonly(true);
			}
		}
	}


	private List<TemplateVo> save(List<TemplateVo> results, IUser user) {

		AtomicInteger inc = new AtomicInteger();
		repo.saveAll(results.stream().map(r -> {
			T_DRIVE_DIARY diray  = convert(r, user, inc.getAndIncrement());
			Long id = repo.findByDlvyDeAndRouteNoAndDlvyLcCdAndStatusCdAndEventCd(diray.getDlvyDe(),
					diray.getRouteNo(), diray.getDlvyLcCd(), diray.getStatusCd(), diray.getEventCd()).orElse(diray).getId();
			diray.setId(id);
			return diray;
		}).collect(Collectors.toList()));
		return results;
	}

	private T_DRIVE_DIARY convert(TemplateVo r, IUser user, int orderNo) {
		T_DRIVE_DIARY diary = new T_DRIVE_DIARY();

		diary.setAclGroupId(r.getGid());
		diary.setDlvyDe(r.getDlvyDe());
		diary.setOrderNo(orderNo);
		diary.setDlvyLcCd(r.getStopCd());
		diary.setDlvyLcNm(r.getStopNm());
		diary.setDriverCd(user.getUsername());
		diary.setEventDt(r.getEventDt());
		diary.setEventNm(r.getEventNm().eventTyNm);
		diary.setEventCd(r.getEventNm().name());
		diary.setEventTy(r.getDataTy());
		diary.setFctryCd(r.getFactryCd());
		diary.setMobileNo(user.getMobileNo());
		diary.setRemark(r.getRm());
		diary.setRouteNo(r.getRouteNo());
		diary.setStatusCd(r.getState().getStateCd());
		diary.setStatusNm(r.getState().getStateNm());
		TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
		diary.setVrn(driver.getCar().getVrn());

		diary.setTrnsprtCmpny(driver.getCar().getTrnsprtCmpny());
		diary.setLdngTy(driver.getCar().getLdngTy());
		diary.setVhcleTy(driver.getCar().getWegith().toString());
		diary.setCaralcTy(r.getPlan().getCaraclTy());
		diary.setRtateRate(new BigDecimal(r.getPlan().getConfRtateRate()));
		diary.setDriverNm(driver.getCar().getDriverNm());

		diary.setGradeScope1(nullSafeEnum(r.getScopeGrad()));
		diary.setGrade(nullSafeEnum(r.getPointGrad()));









		return diary;
	}


	private String nullSafeEnum(GradeTy scopeGrad) {
		return scopeGrad == null ? null : scopeGrad.name();
	}
}
