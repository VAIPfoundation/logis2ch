package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.BoardEntity;

public interface BoardRepo extends JpaRepository<BoardEntity, Long>{

}
