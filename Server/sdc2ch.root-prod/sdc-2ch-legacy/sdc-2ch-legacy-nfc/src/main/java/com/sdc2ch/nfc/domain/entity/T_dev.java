package com.sdc2ch.nfc.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.querydsl.core.annotations.QueryProjection;

@Entity
@Table(name = "t_dev")
public class T_dev {
	
	
    @QueryProjection 
    public T_dev(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public T_dev() {
    }
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEVID", length=11)
	private int id;
	@Column(name="NM")
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
