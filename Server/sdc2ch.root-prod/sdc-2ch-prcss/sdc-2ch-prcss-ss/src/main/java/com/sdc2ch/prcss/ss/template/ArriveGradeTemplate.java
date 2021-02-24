package com.sdc2ch.prcss.ss.template;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_POINT_HIST;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.DlvyState;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ArriveGradeTemplate extends GradePointTemplate {

	private static final BigDecimal FORCE_TIME = new BigDecimal(-20);

	@Override
	public List<T_ANALS_GRADE_POINT_HIST> execute() {

		List<TemplateVo> vos = super.searchTemplates(DlvyState.DELIVERY);

		AtomicInteger inc = new AtomicInteger();
		String stopCd = vos.get(0).getStopCd();
		List<Sortable> sVos = new ArrayList<>();
		for(TemplateVo v : vos) {
			Sortable sortable = new Sortable();
			sortable.setStopCd(v.getStopCd());
			sortable.setVo(v);
			if(stopCd.equals(v.getStopCd())) {
				sortable.setIndex(inc.get());
			}else {
				sortable.setIndex(inc.incrementAndGet());
				stopCd = v.getStopCd();
			}
			sVos.add(sortable);
		}
		Map<Sortable, List<Sortable>> mapped = sVos.stream().collect(Collectors.groupingBy(s -> s, LinkedHashMap::new, Collectors.toList()));

		List<Sortable> keys = mapped.keySet().stream().collect(Collectors.toList());
		List<T_ANALS_GRADE_POINT_HIST> hist = mapped.keySet()
				.stream()
				.sorted(Comparator.comparing(Sortable::getIndex)).map( k -> {
						List<Sortable> msts = mapped.get(k);
						T_ANALS_GRADE_POINT_HIST hst = convert(msts);
						Sortable beforeStop = findStop(k, keys);

						if(beforeStop != null) {
							hst.setFromDlvyLcCd(beforeStop.getStopCd());



						}else {
							hst.setFromDlvyLcCd(k.getVo().getFactryCd());
						}

						return hst;
					})
				.collect(Collectors.toList());
		return hist;

	}

	private Sortable findStop(Sortable k, List<Sortable> sVos) {
		int befo = k.getIndex() - 1;
		if(befo < 0){
			return null;
		}
		return sVos.get(befo);
	}

	private T_ANALS_GRADE_POINT_HIST convert(List<Sortable> msts) {


		T_ANALS_GRADE_POINT_HIST point = new T_ANALS_GRADE_POINT_HIST();

		for(Sortable s : msts) {

			TemplateVo vo = s.getVo();

			TmsPlanIO plan = vo.getPlan();

			point.setAclGroupId(vo.getGid());
			point.setCaralcTy(plan.getCaraclTy());
			point.setDlvyDe(vo.getDlvyDe());
			point.setDriverCd(plan.getDriverCd());
			point.setDriverNm(plan.getDriverNm());

			switch (vo.getEventNm()) {
			case CC_ENTER:
				point.setGeoEnterDt(vo.getEventDt());
				break;
			case CC_ARRIVE:
				point.setGeoArriveDt(vo.getEventDt());
				break;
			case CC_TAKEOVER:
				point.setUnLoadDt(vo.getEventDt());
				break;
			case CC_DEPART:
				point.setGeoExitDt(vo.getEventDt());
				break;
			default:
				break;
			}
			point.setChild(!vo.getStopCd().equals(plan.getBundledDlvyLc()));
			String time = svc.getPlanedArriveTime(plan);
			Date planDe = convertStringtoDate(plan.getDlvyDe(), time);
			point.setPlanArriveDt(planDe);
			point.setRouteNo(routeNo);
			point.setToDlvyLcCd(vo.getStopCd());
			point.setVhcleTy(nullCheck(plan.getCarWegit()));
			point.setVrn(plan.getVrn());
		}

		
		Date arrivedTime = point.getGeoArriveDt() == null ? point.getUnLoadDt() : point.getGeoArriveDt();

		GradeTy gradeTy = GradeTy.FF;
		if(arrivedTime != null) {
			LocalDateTime fromTime = convertToLocalDateTimeViaInstant(point.getPlanArriveDt());
			LocalDateTime toTime = convertToLocalDateTimeViaInstant(arrivedTime);
			BigDecimal minutes = new BigDecimal(ChronoUnit.MINUTES.between(fromTime, toTime));

			if(minutes.compareTo(BigDecimal.ZERO) == 1) {	
				gradeTy = GradeTy.C;
			}else if(minutes.compareTo(FORCE_TIME) == 1){	
				gradeTy = GradeTy.B;
			}else {											
				gradeTy = GradeTy.A;
			}

		}
		point.setGrad(gradeTy.name());

		final GradeTy _gradeTy = gradeTy;
		msts.stream().forEach(s -> s.getVo().setPointGrad(_gradeTy));

		return point;
	}

	private Float nullCheck(String str) {
		return StringUtils.isEmpty(str) ? null : new BigDecimal(str).floatValue();
	}

	private Date convertStringtoDate(String dlvyDe, String time) {
		try {
			return new SimpleDateFormat("yyyyMMddHH:mm").parse(dlvyDe + time);
		}catch (Exception e) {
			return null;
		}
	}

	@Getter
	@Setter
	@ToString
	class Sortable {
		private int index;
		private TemplateVo vo;
		private String stopCd;
		@Override
		public int hashCode() {
			return (index + stopCd).hashCode();
		}

		public String getKey() {
			return index + stopCd;
		}
		public boolean equals(Object o) {
			return hashCode() == o.hashCode();
		}
	}

}
