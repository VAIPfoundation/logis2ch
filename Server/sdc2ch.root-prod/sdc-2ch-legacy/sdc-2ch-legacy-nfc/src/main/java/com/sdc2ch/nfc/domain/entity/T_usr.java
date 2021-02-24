package com.sdc2ch.nfc.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name="T_usr")
@Immutable
public class T_usr {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USRUID", length = 11)
	private int usruid;
	@Column(name = "USRID")
	private String usrid;
	@Column(name = "NM")
	private String nm;
	@Column(name = "GEN")
	private String gen;
	@Column(name = "TIT")
	private String tit;
	@Column(name = "PH")
	private String ph;
	@Column(name = "EML")
	private String eml;
	@Column(name = "BTH")
	private Date bth;
	@Column(name = "PIN")
	private String pin;
	@Column(name = "LGNID")
	private String lgnid;
	@Column(name = "PW")
	private String pw;
	@Column(name = "STTDT")
	private Date sttdt;
	@Column(name = "EXPDT")
	private Date expdt;
	@Column(name = "SECLVL")
	private String seclvl;
	@Column(name = "DISDRT")
	private Integer disdrt;
	@Column(name = "DISCNT")
	private Integer discnt;
	@Column(name = "DISMSG")
	private String dismsg;
	@Column(name = "DSBDUSR")
	private String dsbdusr;
	@Column(name = "IHR")
	private String ihr;
	@Column(name = "USATHPHT")
	private String usathpht;
	@Column(name = "DEL")
	private String del;
	@Column(name = "DELDT")
	private String deldt;
	@Column(name = "DELUSRID")
	private String delusrid;
	@Column(name = "USRGRUID")
	private Integer usrgruid;
	@Column(name = "ISUSRGR")
	private String isusrgr;
	@Column(name = "LSTMOD")
	private Integer lstmod;
	@Column(name = "MODFLG")
	private String modflg;
	@Column(name = "PERMUID")
	private Integer permuid;
	@Column(name = "LSTMODDT")
	private Date lstmoddt;
	@Column(name = "UDTCNT")
	private int udtcnt;
	@Column(name = "PWSTA")
	private int pwsta;
	@Column(name = "PWTKN")
	private String pwtkn;
	@Column(name = "USRIDORD")
	private String usridord;
	









	public int getUsruid() {
		return usruid;
	}
	public void setUsruid(int usruid) {
		this.usruid = usruid;
	}
	public String getUsrid() {
		return usrid;
	}
	public void setUsrid(String usrid) {
		this.usrid = usrid;
	}
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	public String getGen() {
		return gen;
	}
	public void setGen(String gen) {
		this.gen = gen;
	}
	public String getTit() {
		return tit;
	}
	public void setTit(String tit) {
		this.tit = tit;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public String getEml() {
		return eml;
	}
	public void setEml(String eml) {
		this.eml = eml;
	}
	public Date getBth() {
		return bth;
	}
	public void setBth(Date bth) {
		this.bth = bth;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getLgnid() {
		return lgnid;
	}
	public void setLgnid(String lgnid) {
		this.lgnid = lgnid;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public Date getSttdt() {
		return sttdt;
	}
	public void setSttdt(Date sttdt) {
		this.sttdt = sttdt;
	}
	public Date getExpdt() {
		return expdt;
	}
	public void setExpdt(Date expdt) {
		this.expdt = expdt;
	}
	public String getSeclvl() {
		return seclvl;
	}
	public void setSeclvl(String seclvl) {
		this.seclvl = seclvl;
	}
	public Integer getDisdrt() {
		return disdrt;
	}
	public void setDisdrt(Integer disdrt) {
		this.disdrt = disdrt;
	}
	public Integer getDiscnt() {
		return discnt;
	}
	public void setDiscnt(Integer discnt) {
		this.discnt = discnt;
	}
	public String getDismsg() {
		return dismsg;
	}
	public void setDismsg(String dismsg) {
		this.dismsg = dismsg;
	}
	public String getDsbdusr() {
		return dsbdusr;
	}
	public void setDsbdusr(String dsbdusr) {
		this.dsbdusr = dsbdusr;
	}
	public String getIhr() {
		return ihr;
	}
	public void setIhr(String ihr) {
		this.ihr = ihr;
	}
	public String getUsathpht() {
		return usathpht;
	}
	public void setUsathpht(String usathpht) {
		this.usathpht = usathpht;
	}
	public String getDel() {
		return del;
	}
	public void setDel(String del) {
		this.del = del;
	}
	public String getDeldt() {
		return deldt;
	}
	public void setDeldt(String deldt) {
		this.deldt = deldt;
	}
	public String getDelusrid() {
		return delusrid;
	}
	public void setDelusrid(String delusrid) {
		this.delusrid = delusrid;
	}
	public Integer getUsrgruid() {
		return usrgruid;
	}
	public void setUsrgruid(Integer usrgruid) {
		this.usrgruid = usrgruid;
	}
	public String getIsusrgr() {
		return isusrgr;
	}
	public void setIsusrgr(String isusrgr) {
		this.isusrgr = isusrgr;
	}
	public Integer getLstmod() {
		return lstmod;
	}
	public void setLstmod(Integer lstmod) {
		this.lstmod = lstmod;
	}
	public String getModflg() {
		return modflg;
	}
	public void setModflg(String modflg) {
		this.modflg = modflg;
	}
	public Integer getPermuid() {
		return permuid;
	}
	public void setPermuid(Integer permuid) {
		this.permuid = permuid;
	}
	public Date getLstmoddt() {
		return lstmoddt;
	}
	public void setLstmoddt(Date lstmoddt) {
		this.lstmoddt = lstmoddt;
	}
	public int getUdtcnt() {
		return udtcnt;
	}
	public void setUdtcnt(int udtcnt) {
		this.udtcnt = udtcnt;
	}
	public int getPwsta() {
		return pwsta;
	}
	public void setPwsta(int pwsta) {
		this.pwsta = pwsta;
	}
	public String getPwtkn() {
		return pwtkn;
	}
	public void setPwtkn(String pwtkn) {
		this.pwtkn = pwtkn;
	}
	public String getUsridord() {
		return usridord;
	}
	public void setUsridord(String usridord) {
		this.usridord = usridord;
	}

}
