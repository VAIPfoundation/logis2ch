package com.sdc2ch.admin.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.web.admin.repo.dao.BoardRepo;
import com.sdc2ch.web.admin.repo.domain.BoardEntity;

@Service
public class BoardService {
	
	
	@Autowired BoardRepo repo;
	
	public List<BoardEntity> findAll() {
		return repo.findAll();
	}

	public BoardEntity findById(Long id) {
		return repo.findById(id).orElse(null);
	}
	public BoardEntity insert(BoardEntity board) {
		return repo.save(board);
	};
	
	public BoardEntity update(BoardEntity board) {
		return repo.save(board);
	};
	
	public void delete(Long id) {
		repo.deleteById(id);
	};
}
