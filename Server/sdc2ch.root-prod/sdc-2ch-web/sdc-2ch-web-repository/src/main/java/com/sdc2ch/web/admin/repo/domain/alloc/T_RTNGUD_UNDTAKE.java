package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_RTNGUD_UNDTAKE")
@Getter
@Setter
public class T_RTNGUD_UNDTAKE extends T_ID {
	
	@Column(name = "RTNGUD_REQUST_DE", columnDefinition = YYYYMMDD_08)
	private String rtngudRequstDe;

	@Column(name = "VHCLE_ROW_ID")
	private Long vhcleRowId;

	@Column(name = "RTNGUD_GB")
	private String rtngudGb;

	@Column(name = "CNFIRM_YN")
	@ColumnDefault(value = "0")
	private boolean cnfirmYn;

	@Column(name = "CNFIRM_DT")
	private Date cnfirmDt;

}
