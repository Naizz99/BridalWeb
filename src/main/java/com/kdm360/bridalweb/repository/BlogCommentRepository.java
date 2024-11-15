package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.BlogComment;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

	List<BlogComment> findByBlogPostId(BlogPost blog);

	@Transactional
    @Modifying
    @Query("DELETE FROM BlogComment vc WHERE vc.parentCommentId = :parentCommentId")
	void deleteByParentCommentId(BlogComment parentCommentId);

	void deleteByBlogPostId(BlogPost blog);

	boolean existsByUserId(User user);
}
