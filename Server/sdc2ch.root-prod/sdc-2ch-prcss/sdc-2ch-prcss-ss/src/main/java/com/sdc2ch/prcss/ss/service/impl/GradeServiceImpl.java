package com.sdc2ch.prcss.ss.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.ss.repo.T_AnalsGradePointHistRepository;
import com.sdc2ch.prcss.ss.repo.T_AnalsGradeScopeHistRepository;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_POINT_HIST;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.prcss.ss.service.IGradeService;
import com.sdc2ch.prcss.ss.template.GradePointTemplate;
import com.sdc2ch.prcss.ss.template.GradeScopeTemplate;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce.Condition;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce.StdtimePointTy;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce.StdtimeScopeTy;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsShippingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GradeServiceImpl implements IGradeService {

	@Autowired List<IAnalsStdTimeSerivce> gradSvcs;
	@Autowired ITmsShippingService shippingSvc;
	@Autowired T_AnalsGradeScopeHistRepository repo;
	@Autowired T_AnalsGradePointHistRepository pointRepo;

	private Float nullCheck(String carWegit) {
		return StringUtils.isEmpty(carWegit) ? null : new BigDecimal(carWegit).floatValue();
	}

	public String getPlanedArriveTime(TmsPlanIO plan) {
		return shippingSvc.getPlanedArriveTime(plan);
	}

	public List<TemplateVo> analsGradeScope(List<TemplateVo> results) {

		if(results != null) {
			
			Map<String, List<TemplateVo>> mapped = results.stream().collect(Collectors.groupingBy(TemplateVo::getRouteNo));
			mapped.keySet().stream().forEach(routeNo -> {

				List<TemplateVo> templates = mapped.get(routeNo);

				try {

					repo.saveAll(	
						Stream.of(StdtimeScopeTy.values()).map(scop -> {
							TmsPlanIO plan = templates.get(0).getPlan();
							Condition condition = Condition.builder().caralcTy(plan.getCaraclTy())
									.fctryCd(plan.getFctryCd())
									.vehicleTy(nullCheck(plan.getCarWegit()))
									.routeNo(routeNo)
									.build();
							GradeScopeTemplate g = GradeScopeTemplate.builder()
								.scope(scop)
								.routeNo(routeNo)
								.stdGradles(gradSvcs.stream()
										.filter(s -> s.supported(scop)).findFirst().get().searchAnalsStdTimeDetail(condition))
								.templates(templates)
								.build();
							return g;
						}).map(t -> {
							T_ANALS_GRADE_SCOPE_HIST hist = t.execute();
							if(hist != null) {
								Long id = repo.findByAclGroupIdAndRouteNoAndStatusCd(hist.getAclGroupId(),
										hist.getRouteNo(), hist.getStatusCd())
										.orElse(hist).getId();
								hist.setId(id);
							}
							return hist;
						}).filter(t -> t != null).collect(Collectors.toList()));

				} catch (Exception e) {
					log.error("{}", e);
				}
			});
		}
		return results;
	}

	public List<TemplateVo> analsPointScope(List<TemplateVo> results) {

		if(results != null) {
			
			Map<String, List<TemplateVo>> mapped = results.stream().collect(Collectors.groupingBy(TemplateVo::getRouteNo));
			mapped.keySet().stream().forEach(routeNo -> {

				List<TemplateVo> templates = mapped.get(routeNo);

				try {

					Stream.of(StdtimePointTy.values()).map(point -> {
						return GradePointTemplate.builder()
						.scope(point)
						.routeNo(routeNo)
						.templates(templates)
						.svc(this)
						.build();

					}).map(t -> {

						List<T_ANALS_GRADE_POINT_HIST> hists = t.execute();
						for(T_ANALS_GRADE_POINT_HIST hist : hists) {
							if(hist != null) {
								Long id = pointRepo.findByAclGroupIdAndRouteNoAndToDlvyLcCd(hist.getAclGroupId(),
										hist.getRouteNo(), hist.getToDlvyLcCd())
										.orElse(hist).getId();
								hist.setId(id);
								pointRepo.save(hist);
							}
						}
						return hists;
					}).filter(t -> t != null).collect(Collectors.toList());

				}catch (Exception e) {
					log.error("{}", e);
				}
			});
		}

		return results;
	}
}
