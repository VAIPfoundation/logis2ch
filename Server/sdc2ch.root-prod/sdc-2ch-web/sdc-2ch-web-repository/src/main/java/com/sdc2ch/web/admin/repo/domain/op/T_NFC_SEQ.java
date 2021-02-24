package com.sdc2ch.web.admin.repo.domain.op;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.service.io.NfcSeqIO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class T_NFC_SEQ extends T_ID implements NfcSeqIO {

	private int sequence;

	private String lastmonth;

	@Override
	@Transient
	public Date getLastUpdated() {
		return updateDt;
	}

}
