package com.varsql.web.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.board.BoardEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface BoardRepository extends DefaultJpaRepository, JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity>  {
	
	public BoardEntity findByArticleId(long articleId);
}
