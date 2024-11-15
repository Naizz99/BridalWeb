package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.dto.BlogStatsDto;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

	List<BlogPost> getByActive(boolean active);

	List<BlogPost> findByUserId(User user);
	
	@Query("SELECT new com.kdm360.bridalweb.dto.BlogStatsDto(" +
	           "bp.blogPostId, " +
	           "bp.title, " +
	           "(SELECT COUNT(bl) FROM BlogLike bl WHERE bl.blogPostId = bp), " +
	           "(SELECT COUNT(bc) FROM BlogComment bc WHERE bc.blogPostId = bp), " +
	           "(SELECT COUNT(bs) FROM BlogShare bs WHERE bs.blogPostId = bp), " +
	           "(SELECT COUNT(bv) FROM BlogView bv WHERE bv.blogPostId = bp)" +
	           ") " +
	           "FROM BlogPost bp WHERE bp.active = true")
	    List<BlogStatsDto> findBlogPostStats();
	

	    @Query("SELECT new com.kdm360.bridalweb.dto.BlogStatsDto(" +
	           "bp.blogPostId, " +
	           "bp.title, " +
	           "(SELECT COUNT(bl) FROM BlogLike bl WHERE bl.blogPostId = bp), " +
	           "(SELECT COUNT(bc) FROM BlogComment bc WHERE bc.blogPostId = bp), " +
	           "(SELECT COUNT(bs) FROM BlogShare bs WHERE bs.blogPostId = bp), " +
	           "(SELECT COUNT(bv) FROM BlogView bv WHERE bv.blogPostId = bp)" +
	           ") " +
	           "FROM BlogPost bp WHERE bp.blogPostId = :blogPostId AND bp.active = true")
	    BlogStatsDto findBlogPostStatsById(@Param("blogPostId") Long blogPostId);

		boolean existsByUserId(User user);
}
