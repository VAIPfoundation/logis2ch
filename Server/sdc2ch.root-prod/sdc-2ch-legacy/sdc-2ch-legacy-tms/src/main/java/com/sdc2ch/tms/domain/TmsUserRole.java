

package com.sdc2ch.tms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.require.domain.IUserRole;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "M_GROUP")
@Getter
@Setter
public class TmsUserRole implements IUserRole {
	
	@Id @Column(name = "ROWID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "GROUP_NM", length = 20)
    private String rolename;

}

