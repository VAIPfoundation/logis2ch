package com.sdc2ch.prcss.ss.template;

import java.util.Comparator;
import java.util.List;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.DlvyState;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;
import com.sdc2ch.tms.io.TmsPlanIO;

public class LoadingGradeTemplate extends GradeScopeTemplate {


	@Override
	public T_ANALS_GRADE_SCOPE_HIST execute() {
		AnalsStdGradeIO stdTime = getAnalsStdGradeIO();
		List<TemplateVo> vos = super.searchTemplates(DlvyState.LDNG);
		TemplateVo orgSt = vos.stream().filter(v -> v.getEventNm() == EventNm.LDNG_ST).findFirst().orElseGet(null);
		TemplateVo orgEd = vos.stream().filter(v -> v.getEventNm() == EventNm.LDNG_ED).max(Comparator.comparing(TemplateVo::getEventDt)).orElse(null);

		TemplateVo start = orgSt;
		TemplateVo end = orgEd;



















		GradeTy gradeTy = analsGrade(start, end, stdTime);
		vos.stream().forEach(v -> v.setScopeGrad(gradeTy));



		TmsPlanIO plan = orgSt.getPlan();

		Params param = new Params();
		param.setFrom(start);
		param.setFromPlanDe(plan.getLdngStDe());
		param.setFromPlanTime(plan.getLdngSt());
		param.setToPlanDe(plan.getLdngEdDe());
		param.setToPlanTime(plan.getLdngEd());
		param.setGradeTy(gradeTy);
		param.setPlan(plan);
		param.setState(DlvyState.LDNG);
		param.setStdTime(stdTime);
		param.setTo(end);
 		return save(param);
	}

}
