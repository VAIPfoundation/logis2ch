package com.sdc2ch.web.admin.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ID_LNG_20;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sdc2ch.require.domain.IMenu;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "T_MENU")
@Getter
@Setter
public class T_MENU extends T_ID implements IMenu {


	@Column(name = "MENU_TITLE", unique=true, columnDefinition = ANY_ID_LNG_20)
	private String title;
	@Column(name = "MENU_PATH")
	private String path;
	@Column(name = "MENU_ICON")
	private String iconClassName;
	@Column(name = "MENU_PGM_ID", unique=true, columnDefinition = ANY_ID_LNG_20)
	private String pgmId;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	private List<T_MENU> items = new ArrayList<>();

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "PARENT_ROW_ID", referencedColumnName = "ROW_ID")
	private T_MENU parent;
	
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "T_MENU_T_ROLE_MAP")
	@JsonBackReference
	private Set<T_ROLE> roles = new HashSet<>();
	
	

	@Transient
    public Stream<T_MENU> flattened() {
        return Stream.concat(
                Stream.of(this),
                items.stream().flatMap(T_MENU::flattened));
    }
}
