package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.BlogView;
import com.kdm360.bridalweb.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogViewRepository extends JpaRepository<BlogView, Long> {

    @Query("SELECT b.blogPostId, COUNT(b) AS viewCount FROM BlogView b GROUP BY b.blogPostId ORDER BY viewCount DESC")
    List<Object[]> findMostReadBlogs();

	Optional<BlogView> getByBlogPostIdAndUserIdAndViewDate(BlogPost blog, User user, int today);

	Optional<BlogView> getByBlogPostIdAndAndViewDate(BlogPost blog, int today);

	void deleteByBlogPostId(BlogPost blog);

	boolean existsByUserId(User user);
}
