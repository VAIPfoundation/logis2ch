package com.sdc2ch.service.grade;

import java.util.List;

import com.sdc2ch.service.grade.io.AnalsStdGradeIO;

import lombok.Builder;
import lombok.Getter;


public interface IAnalsStdTimeSerivce {
	
	public enum StdtimeScopeTy {
		
		LDNG,
		DLVY,
		
	}
	public enum StdtimePointTy {
		ARRV,
	}
	
	@Builder
	@Getter
	public static class Condition {
		String fctryCd;
		String routeNo;
		Float vehicleTy;
		String caralcTy;
	}
	
	boolean supported(StdtimeScopeTy ty);
	List<AnalsStdGradeIO> searchAnalsStdTimeDetail(Condition param);

}
