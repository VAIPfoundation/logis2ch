package com.sdc2ch.prcss.ss.template;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.DlvyState;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.State;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce.StdtimeScopeTy;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;

public abstract class GradeScopeTemplate extends AbstractGradeTemplate {

	protected List<TemplateVo> templates;
	protected List<AnalsStdGradeIO> stdgrades;
	protected String routeNo;

	public abstract T_ANALS_GRADE_SCOPE_HIST execute();

	public static class GradeTemplateBuilder {

		private StdtimeScopeTy scope;
		private List<TemplateVo> templates;
		private List<AnalsStdGradeIO> stdgrades;
		private String routeNo;

		public GradeScopeTemplate build() {

			GradeScopeTemplate template = null;
			switch (scope) {
			case DLVY:
				template = new DeliveryGradeTemplate();
				break;
			case LDNG:
				template =  new LoadingGradeTemplate();
				break;
			default:
				template =  new DefaultTemplate();
				break;
			}
			template.templates = templates;
			template.stdgrades = stdgrades;
			template.routeNo = routeNo;
			return template;
		}

		public GradeTemplateBuilder scope(StdtimeScopeTy scop) {
			this.scope = scop;
			return this;
		}
		public GradeTemplateBuilder routeNo(String routeNo) {
			this.routeNo = routeNo;
			return this;
		}

		public GradeTemplateBuilder stdGradles(List<AnalsStdGradeIO> stdgrades) {

			if(stdgrades.isEmpty()) {
				System.out.println();
			}
			this.stdgrades = stdgrades;
			return this;
		}

		public GradeTemplateBuilder templates(List<TemplateVo> templates) {
			this.templates = templates;
			return this;
		}
	}

	public static GradeTemplateBuilder builder() {
		return new GradeTemplateBuilder();
	}


	public static class DefaultTemplate extends GradeScopeTemplate {

		@Override
		public T_ANALS_GRADE_SCOPE_HIST execute() {

			return null;
		}

	}

	protected List<TemplateVo> searchTemplates(State ... state) {
		List<State> states = Lists.newArrayList(state);
		return templates.stream()
				.filter(t -> states.stream()
						.anyMatch(_s -> _s.getEnName().equals(t.getState().getEnName())))
				.collect(Collectors.toList());
	}
	protected AnalsStdGradeIO getAnalsStdGradeIO() {
		TemplateVo vo = templates.get(0);
		return stdgrades.stream().filter(std -> filter(std, vo)).findFirst().orElse(stdgrades.stream().findFirst().get());
	}

	protected boolean filter(AnalsStdGradeIO std, TemplateVo vo) {
		String carWeight = vo.getPlan().getCarWegit();
		String caralcTy = vo.getPlan().getCaraclTy();
		return caralcTy.equals(std.getCaralcTy()) && new BigDecimal(carWeight).compareTo(new BigDecimal(std.getVhcleTy())) == 0;
	}


	protected GradeTy analsGrade (TemplateVo from, TemplateVo to, AnalsStdGradeIO stdTime) {


		if(from != null && from.getEventDt() != null && to != null && to.getEventDt() != null) {


			long fromTimeMi = from.getEventDt().getTime();
			long toTimeMi = to.getEventDt().getTime();



			if(toTimeMi - fromTimeMi < 1000 * 5){
				return GradeTy.FF;
			}
			BigDecimal adjust = stdTime.getAdjustTime() == null ? BigDecimal.ZERO : new BigDecimal(stdTime.getAdjustTime());
			BigDecimal std = stdTime.getStdTime() == null ? BigDecimal.ZERO : new BigDecimal(stdTime.getStdTime());

			LocalDateTime fromTime = convertToLocalDateTimeViaInstant(from.getEventDt());
			LocalDateTime toTime = convertToLocalDateTimeViaInstant(to.getEventDt());
			BigDecimal minutes = new BigDecimal(ChronoUnit.MINUTES.between(fromTime, toTime));

			switch (std.compareTo(minutes)) {
			case 1:
				return std.subtract(adjust).compareTo(minutes) > 0 ? GradeTy.A : GradeTy.B;
			case 0:
				return GradeTy.B;
			case -1:
				return GradeTy.C;
			default:
				break;
			}

		}
		return GradeTy.FF;
	}

	protected T_ANALS_GRADE_SCOPE_HIST save(Params param) {

		T_ANALS_GRADE_SCOPE_HIST hist = new T_ANALS_GRADE_SCOPE_HIST();
		hist.setAclGroupId(param.getFrom().getGid());
		hist.setAdjustTime(nullSafe(param.getStdTime().getAdjustTime()));
		hist.setCaralcTy(param.getPlan().getCaraclTy());
		hist.setDriverCd(param.getPlan().getDriverCd());
		hist.setDriverNm(param.getPlan().getDriverNm());
		hist.setFromDlvyLcCd(param.getFrom().getPlan().getStopCd());


		hist.setFromPlanDe(param.getFromPlanDe());
		hist.setFromPlanTime(param.getFromPlanTime());

		if(param.getFrom() != null) {
			hist.setFromRealDe(convertLocalTimeToDeString(param.getFrom().getEventDt()));
			hist.setFromRealTime(convertLocalTimeToTimeString(param.getFrom().getEventDt()));
		}
		hist.setGrad(param.gradeTy.name());


		hist.setRouteNo(routeNo);
		hist.setStdTime(nullSafe(param.getStdTime().getStdTime()));

		hist.setToPlanDe(param.getToPlanDe());
		hist.setToPlanTime(param.getToPlanTime());

		if(param.getTo() != null) {
			hist.setToDlvyLcCd(param.getTo().getPlan().getStopCd());
			hist.setToRealDe(convertLocalTimeToDeString(param.getTo().getEventDt()));
			hist.setToRealTime(convertLocalTimeToTimeString(param.getTo().getEventDt()));
		}


		hist.setVhcleTy(nullCheck(param.getPlan().getCarWegit()));
		hist.setVrn(param.getPlan().getVrn());
		hist.setDlvyDe(param.getPlan().getDlvyDe());
		hist.setStatusCd(param.getState().getStateCd());
		hist.setStatusNm(param.getState().getStateNm());

		System.out.println(hist);
		return hist;
	}

	private Float nullCheck(String str) {
		return StringUtils.isEmpty(str) ? null : new BigDecimal(str).floatValue();
	}

	private long nullSafe(Long obj) {
		return obj == null ? 0 : obj;
	}


	@Getter
	@Setter
	protected class Params {
		TmsPlanIO plan;
		AnalsStdGradeIO stdTime;
		TemplateVo from;
		TemplateVo to;
		GradeTy gradeTy;
		DlvyState state;
		String fromPlanTime;
		String fromPlanDe;
		String toPlanTime;
		String toPlanDe;
	}

}
