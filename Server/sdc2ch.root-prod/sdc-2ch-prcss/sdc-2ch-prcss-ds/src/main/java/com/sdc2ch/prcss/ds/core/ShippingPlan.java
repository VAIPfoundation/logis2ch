package com.sdc2ch.prcss.ds.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.util.ProcessUtils;
import com.sdc2ch.tms.enums.DeliveryType;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.SeasonType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.enums.TransportType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

 


@Builder
@Getter

@ToString(of = {"dlvyLcId", "vrn", "dlvyDe", "routeNo", "plannedATime", "plannedDTime", "routeReIdx", "dlvyLoReIdx", "timeZoneNm"})

public class ShippingPlan implements ShippingPlanIO {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public final  Long tmsPlanRowId;
	
	public final String routeNo;
	
	public final String dlvyDe;
	
	public final String driverCd;
	
	public final String vrn;
	
	
	public final String dlvyLcId;
	
	
	public final String dlvyLcNm;
	
	
	public final int dlvyLcSeq;
	
	
	public final String plannedATime;
	
	
	public final String plannedDTime;
	
	public final FactoryType fctryTy;
	
	public final ShippingType shppTy;
	
	public final SeasonType seasonTy;
	
	public final DeliveryType deliveryTy;
	
	public final TransportType transportTy;
	
	
	public final boolean isEmpty;
	
	
	public final double lat;
	public final double lng;
	
	public final String carWegit;
	public final String dockNo;
	
	
	
	private final String bundledDlvyLc;
	
	@Setter @Getter
	private int routeReIdx;
	@Setter @Getter
	private int dlvyLoReIdx;
	
	private int radius;
	
	@Setter @Getter
	private String ldngSt;
	@Setter @Getter
	private String ldngEd;
	
	@Setter @Getter
	private LocalDateTime scheDlvyStDt;
	@Setter @Getter
	private LocalDateTime scheDlvyEdDt;
	
	@Setter @Getter
	private String confRtateRate;
	@Setter @Getter
	private String confTollCost;
	@Setter @Getter
	private String confDistance;
	@Setter @Getter
	private String carOil;
	
	@Setter @Getter
	private String addr;
	
	@Setter @Getter
	private String timeZoneNm;

	
	public boolean isFactory() {
		return Arrays.asList(FactoryType.values())
				.stream().anyMatch(f -> f.getCode().equals(dlvyLcId));
	}
	
	public boolean isWareHouse() {
		return !isFactory() && !isCustomerCenter();
	}
	
	public boolean isCustomerCenter() {
		return ShippingType.DELEVERY == shppTy && ProcessUtils.isNumeric(dlvyLcId);
	}
	
	
	public boolean isFirst() {
		return dlvyLcSeq == 0;
	}
	
	
	public LocalDateTime getArriveTime() {
		
		String time = plannedATime == null ? plannedDTime : plannedATime;
		try {
			
			if(!StringUtils.isEmpty(time)) {
				LocalDate localDate = LocalDate.parse(dlvyDe, formatter);
				LocalTime localTime = LocalTime.parse(time);
				return LocalDateTime.of(localDate, localTime);
			}
			
		}catch (Exception e) {
			if(!StringUtils.isEmpty(time)) {
				LocalDate localDate = LocalDate.parse(dlvyDe, formatter);
				
				time = time.replaceAll(":", "").replaceAll(";", "");
				
				if(time.length() == 4) {
					LocalTime localTime = LocalTime.parse(time.substring(0, 2) + ":" + time.substring(2, 4));
					return LocalDateTime.of(localDate, localTime);
				}
			}
		}
		return null;
	}
	@Override
	public int getUniqueSequence() {
		return hashCode();
	}
	
	public FactoryType getMyFacrtyTy() {
		return Stream
				.of(FactoryType.values())
				.filter(f -> f.getCode().equals(dlvyLcId))
				.findFirst()
				.orElse(FactoryType.FFFF);
	}
}
