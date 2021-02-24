package com.sdc2ch.prcss.ss.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.DlvyState;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;
import com.sdc2ch.tms.io.TmsPlanIO;


public class DeliveryGradeTemplate extends GradeScopeTemplate {

	@SuppressWarnings("null")
	@Override
	public T_ANALS_GRADE_SCOPE_HIST execute() {

		AnalsStdGradeIO stdTime = getAnalsStdGradeIO();
		List<TemplateVo> vos = super.searchTemplates(DlvyState.EXIT, DlvyState.DELIVERY, DlvyState.TURN);


		TemplateVo start = vos.stream().filter(v -> v.getEventNm() == EventNm.FT_EXIT).findFirst().orElseGet(null);

		



















		
		
		TemplateVo endPlan = vos.stream()
				.filter(v -> v.getPlan().getScheDlvyEdDe() != null && v.getPlan().getScheDlvyEdTime() != null)
				.max(Comparator.comparing(v -> (v.getPlan().getScheDlvyEdDe() + v.getPlan().getScheDlvyEdTime()))).orElse(null);
		
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHH:mm");
		String endPlanStr = endPlan.getPlan().getScheDlvyEdDe() + endPlan.getPlan().getScheDlvyEdTime();	
		Date endPlanDt = null;
		try {
			endPlanDt = transFormat.parse(endPlanStr);
			endPlanDt.setTime(endPlanDt.getTime() + 4*60*60*1000);
		} catch (ParseException e) {
			
			e.printStackTrace();
			endPlanDt.setTime(start.getEventDt().getTime() + 24*60*60*1000);
		}
		final Date endPlanDtFin = endPlanDt;	
		
		
		TemplateVo end = vos.stream()
				.filter(v -> v.getEventNm() == EventNm.FT_ENTER && v.getEventDt() != null && v.getEventDt().compareTo(endPlanDtFin) < 0)
				.max(Comparator.comparing(TemplateVo::getEventDt))
				.orElse(null);
		

		
		Collections.reverse(vos);

		
		
		Map<String, List<TemplateVo>> stopGroups =
				vos.stream()
					.filter(v -> v.getStopCd() != "1D1" && v.getStopCd() != "2D1" && v.getStopCd() != "3D1" && v.getStopCd() != "4D1" && v.getStopCd() != "5D1")
					.collect(Collectors.groupingBy(v -> v.getStopCd()));
		
		
		
		List<String> stopCdList = new ArrayList<>();
		for( TemplateVo vo : vos ) {
			String _stopCd = vo.getStopCd();
			if ( "1D1".equals(_stopCd) || "2D1".equals(_stopCd) || "3D1".equals(_stopCd) || "4D1".equals(_stopCd) || "5D1".equals(_stopCd) ) {
				
			} else if ( stopCdList.indexOf(_stopCd) == -1 ) {
				stopCdList.add(_stopCd);
			}
		}



		
		
		for( String stopCd : stopCdList ){
			List<TemplateVo> svos = stopGroups.get(stopCd);
			if ( end == null ) {
				end = svos.stream()
						.filter(v -> v.getEventNm() == EventNm.CC_TAKEOVER && v.getEventDt() != null && v.getEventDt().compareTo(endPlanDtFin) < 0)
						.sorted()
						.findFirst().orElse(null);
			}
			if ( end == null ) {
				end = svos.stream()
						.filter(v -> v.getEventNm() == EventNm.CC_ARRIVE && v.getEventDt() != null && v.getEventDt().compareTo(endPlanDtFin) < 0)
						.findFirst().orElse(null);
			}
			if ( end == null ) {
				end = svos.stream()
						.filter(v -> v.getEventNm() == EventNm.CC_DEPART && v.getEventDt() != null && v.getEventDt().compareTo(endPlanDtFin) < 0)
						.findFirst().orElse(null);
			}
			if ( end == null ) {
				end = svos.stream()
						.filter(v -> v.getEventNm() == EventNm.CC_ENTER && v.getEventDt() != null && v.getEventDt().compareTo(endPlanDtFin) < 0)
						.findFirst().orElse(null);
			}
			if ( end != null ) {
				break;
			}
		};
		
		
		
		
		Collections.reverse(vos);
		
		
		if ( end == null ) {
			end = findLast(vos);
		}
		
		




		
		
		GradeTy gradeTy = analsGrade(start, end, stdTime);

		vos.stream().forEach(v -> v.setScopeGrad(gradeTy));

		TmsPlanIO plan = start.getPlan();

		Params param = new Params();
		param.setFrom(start);
		param.setFromPlanDe(plan.getScheDlvyStDe());

		param.setFromPlanTime(plan.getScheDlvyStTime());	
		param.setToPlanDe(plan.getScheDlvyEdDe());
		if ( end != null && end.getPlan() != null ) {
			param.setToPlanTime(end.getPlan().getArrivePlanTime());
		}
		param.setGradeTy(gradeTy);
		param.setPlan(plan);
		param.setState(DlvyState.DELIVERY);
		param.setStdTime(stdTime);
		param.setTo(end);

 		return save(param);
	}


	private TemplateVo findLast(List<TemplateVo> vos) {

		return vos.stream().filter(v -> v.getEventDt() != null).max(Comparator.comparing(TemplateVo::getEventDt)).orElse(null);
	}


	public static void main(String[] args) {
		List<Integer> ints = new ArrayList<>(2);
		ints.add(1);
		ints.add(2);
		ints.add(3);

		System.out.println(ints);
		
		String date = "20200213";
		String time = "12:59";
		
		String from = date + time;
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHH:mm");
		try {
			Date to = transFormat.parse(from);
			System.out.println(to.getTime());
			to.setTime(to.getTime() +  4*60*60*1000);
			System.out.println(to.getTime());
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

			
	}

}

