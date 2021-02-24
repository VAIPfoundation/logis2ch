package com.sdc2ch.agent.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
class LoggingAspectUtil {

	
	private List<String> ignoreClasses = Arrays.asList(
			"com.sdc2ch.nfc.service.impl.NfcEventServiceImpl",
			"com.sdc2ch.tms.service.impl.TmsShippingServiceImpl",
			"com.sdc2ch.agent.service.impl.C2chProcessStatServiceImpl",
			""
			);

	@AfterReturning("execution(* com.sdc2ch..*ServiceImpl.*(..))")
	public void logServiceAccess(JoinPoint joinPoint) {
		Class<?> target = joinPoint.getTarget().getClass();
		if (!ignoreClasses.contains(target.getName()))
			log.info("{}", Arrays.asList(joinPoint.getArgs()).stream().map(o -> o == null ? "null" : o.toString()).collect(
					Collectors.joining(",", target.getName() + "." + joinPoint.getSignature().getName().concat("("), ")")));
	}
}
