package com.sdc2ch.web.admin.repo.domain.alloc;

import java.util.Date;

import com.sdc2ch.repo.io.RouteIO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class T_CARALC_CNFIRM2 implements RouteIO{

	private String routeNo;
	private String fctryCd;
	private String vrn;
	private String dlvyDe;
	private String mobileNo;
	private String ldngExpcTime;
	private String adjustTime;
	private String trnsmisUser;
	private Date trnsmisDt;
	private String cnfirmUser;
	private Date cnfirmDt;
	private String caralcTy;
	private String driverCd;

	@Override
	public String toString() {
		return "T_CARALC_CNFIRM2 [routeNo=" + routeNo + ", fctryCd=" + fctryCd + ", vrn=" + vrn + ", dlvyDe=" + dlvyDe
				+ ", ldngExpcTime=" + ldngExpcTime + ", mobileNo=" + mobileNo + "]";
	}
	@Override
	public Long getId() {
		return null;
	}
}
