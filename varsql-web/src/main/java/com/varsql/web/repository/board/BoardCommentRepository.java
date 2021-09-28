package com.varsql.web.repository.board;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.board.BoardCommentEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface BoardCommentRepository extends DefaultJpaRepository, JpaRepository<BoardCommentEntity, Long>, JpaSpecificationExecutor<BoardCommentEntity>  {
	
	public List<BoardCommentEntity> findByArticleId(long articleId, Sort sort);
	
	public BoardCommentEntity findByCommentId(String commentId);
	
	public BoardCommentEntity findByArticleIdAndCommentId(long articleId, long CommentId);

	public void deleteByArticleId(String articleId);

	@Transactional
	@Modifying
	@Query("delete from BoardCommentEntity a where a.articleId = :id")
	public void deleteByArticleIdQuery(@Param("id") long articleId);
	
	@Query(value="select max(a.grpSeq)+1 from BoardCommentEntity a where a.grpCommentId = :grpCommentId")
	public long findByGrpSeqMaxQuery(@Param("grpCommentId") long grpCommentId);
	
	@Transactional
	@Modifying
	@Query(value="update BoardCommentEntity a set  a.grpSeq = a.grpSeq+1 "
			+ "where a.articleId = :articleId "
			+ "and a.grpCommentId = :grpCommentId "
			+ "and a.commentId != :commentId "
			+ "and a.grpSeq > :grpSeq")
	public void updateGrpSeqQuery(@Param("articleId") long articleId, @Param("grpCommentId") long grpCommentId, @Param("commentId") long commentId, @Param("grpSeq") long grpSeq);
}
