package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "T_ARTICLE")
public class ArticleEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	private String subject;
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "board_id", referencedColumnName = "id")
	@JsonBackReference
	private BoardEntity board;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BoardEntity getBoard() {
		return board;
	}

	public void setBoard(BoardEntity board) {
		this.board = board;
	}
}
