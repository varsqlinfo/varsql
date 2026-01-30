package com.varsql.web.app.manager.service;

import org.springframework.stereotype.Service;

import com.varsql.web.app.user.service.BoardService;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.repository.board.BoardCommentRepository;
import com.varsql.web.repository.board.BoardFileRepository;
import com.varsql.web.repository.board.BoardRepository;

@Service
public class BoardMgmtService extends BoardService{

	public BoardMgmtService(BoardRepository boardEntityRepository, BoardCommentRepository boardCommentEntityRepository,
			BoardFileRepository boardFileEntityRepository, FileUploadService fileUploadService) {
		super(boardEntityRepository, boardCommentEntityRepository, boardFileEntityRepository, fileUploadService);
		// TODO Auto-generated constructor stub
	}


}
