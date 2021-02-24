package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.ArticleEntity;

public interface ArticleRepo extends JpaRepository<ArticleEntity, Long>{

}
