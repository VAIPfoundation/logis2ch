package com.sdc2ch.web.admin.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.admin.board.BoardService;
import com.sdc2ch.web.admin.repo.domain.BoardEntity;

@RestController
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired BoardService boardService;

	@GetMapping("/board/findAll")
	public List<BoardEntity> findAll() {
		
		logger.info("{}", "findAll()");
		return boardService.findAll();
	}
	
	@GetMapping("/board/findById")
	public BoardEntity findById1(@RequestParam Long id) {
		
		logger.info("{} :> {}", "findById1()", id);
		return boardService.findById(id);
	}
	
	@GetMapping("/board/findById/{id}/{name}")
	public BoardEntity findById2(@PathVariable(name = "id") Long id, @PathVariable(name = "name") String name) {
		
		logger.info("{} :> {},{}", "findById1()", id, name);
		return boardService.findById(id);
	}
	
	@PostMapping("/board/insert")
	public BoardEntity insert(@RequestBody BoardEntity board) {
		logger.info("insert {} ", board);
		return boardService.insert(board);
	}
	





	
	
	
	@PostMapping("/board/udate")
	public BoardEntity udate(@RequestBody BoardEntity board) {
		logger.info("udate {} ", board);
		return boardService.update(board);
	}
	
	@GetMapping("/board/delete")
	public void delete(@RequestParam Long id) {
		logger.info("delete {} ", id);
		boardService.delete(id);
	}
}
