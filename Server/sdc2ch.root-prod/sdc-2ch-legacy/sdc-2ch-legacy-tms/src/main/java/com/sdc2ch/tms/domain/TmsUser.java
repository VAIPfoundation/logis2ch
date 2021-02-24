package com.sdc2ch.tms.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.domain.IUserRole;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name="M_USER")
@Where(clause="isUse = 1 and Group_Cd != 'HQUSER'")
@Getter
@Setter
public class TmsUser implements IUser {

    @Id @Column(name="ROWID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="USERID", length = 15)
    private String username;
    
    @Column(name="PASSWORD", length = 50)
    private String password;
    
    @Column(name="GROUP_CD", length = 10)
    private String roleName;
    
    @Column(name="CENTER_CD", length = 20)
    private String factoryId;
    
    @Column(name="Mobile_No", length = 20)
    private String mobileNo;
    
    @Transient
    private IUserRole role;
    @Transient
    private IUserDetails userDetails;

    public List<IUserRole> getRoles() {
        return Arrays.asList(new IUserRole() {
			@Override
			public String getRolename() {
				return roleName;
			}
		});
    }

	@Override
	@Transient
	public String getMobileNo() {
		return mobileNo;
	}

	@Override
	public String getFctryCd() {
		return factoryId;
	}
}
