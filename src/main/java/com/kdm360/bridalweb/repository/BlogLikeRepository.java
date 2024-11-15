package com.kdm360.bridalweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.BlogLike;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;

@Repository
public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {

	Optional<BlogLike> getByBlogPostIdAndUserId(BlogPost blog, User user);

	@Transactional
    @Modifying
    @Query("DELETE FROM BlogLike vc WHERE vc.userId = :userId")
	void deleteByUserId(User userId);

	void deleteByBlogPostId(BlogPost blog);

	boolean existsByUserId(User user);
	
}
