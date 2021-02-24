package com.sdc2ch.web.util;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class ExceptionAspect {
	@AfterThrowing(pointcut = "execution(* com.sdc2ch..*ServiceImpl.*(..))", throwing = "ex")
	public void logAfterThrowingAllMethods(Throwable ex) throws Throwable {
		log.error("{}", ex);
		throw ex;
	}

}
