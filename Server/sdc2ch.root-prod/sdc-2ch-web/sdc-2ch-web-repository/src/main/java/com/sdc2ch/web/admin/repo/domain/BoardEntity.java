package com.sdc2ch.web.admin.repo.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "T_BOARD")
public class BoardEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	@Column(name = "BOARD_DESC")
	private String desc;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "board" , fetch = FetchType.EAGER)
	private List<ArticleEntity> articles;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "BoardEntity [id=" + id + ", title=" + title + ", desc=" + desc + "]";
	}
	public List<ArticleEntity> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticleEntity> articles) {
		this.articles = articles;
	}
	
	
}
