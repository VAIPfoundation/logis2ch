package com.sdc2ch.mobile.enums;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.sdc2ch.mobile.res.start.Allocated;
import com.sdc2ch.mobile.res.start.DayOff;
import com.sdc2ch.mobile.res.start.EmptyBox;
import com.sdc2ch.mobile.res.start.EmptyDetail;
import com.sdc2ch.mobile.res.start.MenuDetails;
import com.sdc2ch.mobile.res.start.ReturnGoods;
import com.sdc2ch.mobile.res.start.StartDrive;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum MobilePGMEnums {
	
	
	PGM100("/find/car/allocated/details") {
		SimpleDateFormat fmt = new SimpleDateFormat("MM/dd HH:mm");
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			if(vo == null) {
				return Allocated.of(pgmId, null, null);
			}
			
			return Allocated.of(pgmId, vo.getAllocatedDt() == null? "" : fmt.format(vo.getAllocatedDt()), vo.getAllocatedGroupId());
		}
	},
	PGM200("/event/start/drive") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			
			
			if(vo == null) {
				return StartDrive.of(pgmId, null, null,null,null,null);
			}
			
			SimpleDateFormat pageFmt = new SimpleDateFormat("MM/dd HH:mm");
			SimpleDateFormat serverFmt = new SimpleDateFormat("yyyyMMddHH:mm");
			
			List<ShippingState> contains = Arrays.asList(ShippingState.READY, ShippingState.CANCEL, ShippingState.COMPLETE);
			
			try {
				String curTieme = null;
				if(!StringUtils.isEmpty(vo.getCurArrivedScheduledTime())) {
					String time = checkTime(vo.getCurArrivedScheduledTime());
					curTieme = pageFmt.format(serverFmt.parse(vo.getDlvyDe() + time));
				}
				String nextTime = null;
				if (!StringUtils.isEmpty(vo.getNextArrivedScheduledTime())) {
					
					String time = checkTime(vo.getNextArrivedScheduledTime());
					nextTime = pageFmt.format(serverFmt.parse(vo.getDlvyDe() + time));

				}
				StartDrive start = StartDrive.of(pgmId, vo.getState().name(), vo.getCurDlvyLoNm(), curTieme, vo.getNextDlvylcNm(),
						nextTime);
				
				if(!contains.contains(vo.getState()))
					start.setClassName("menuItem_Active");
				return start;

			} catch (Exception e) {
				log.error("{}", e);
			}
			return null;
		}

		private String checkTime(String time) {
			String _time = time;
			if(StringUtils.isEmpty(_time)) {
				_time = "00:00";
			}
			
			if(_time.length() != 5) {
				_time = "00:00";
			}
			
			_time = _time.replaceAll(";", ":");
			
			return _time;
		}
	},
	PGM300("/find/empty/box") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			if(vo == null)
				return EmptyBox.of(pgmId, 0, 0, 0);
			return EmptyBox.of(pgmId, vo.getEmptyboxTotal(), vo.getEmptyboxComplete(), vo.getSquareBoxQty());
		}
	},
	PGM400("/find/car/allocated") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return ReturnGoods.of(pgmId, "", "");
		}
	},
	PGM500("/event/end/drive") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return EmptyDetail.of(pgmId);
		}
	},
	PGM600("/insert/have/off") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return DayOff.of(pgmId, 0);
		}
	},
	PGM700("/insert/car/wash") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return EmptyDetail.of(pgmId);
		}
	},
	PGM800("/find/sanitation/check/index") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return EmptyDetail.of(pgmId);
		}
	},
	PGM900("/find/drive/statistics") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return EmptyDetail.of(pgmId);
		}
	},
	PGM1000("/find/drive/statistics") {
		@Override
		public MenuDetails getDetail(String pgmId, ShipStateVo2 vo) {
			return EmptyDetail.of(pgmId);
		}
	}
	;
	private String path;
	private MobilePGMEnums(String path) {
		this.path = path;
	}
	
	public abstract MenuDetails getDetail(String pgmId, ShipStateVo2 vo);
}
