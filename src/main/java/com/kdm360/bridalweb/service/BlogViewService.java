package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.BlogView;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.BlogViewRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogViewService {

	@Autowired
    private BlogViewRepository repo;

    public List<BlogView> get() {
        return repo.findAll();
    }

    public Optional<BlogView> get(Long id) {
        return repo.findById(id);
    }

    public BlogView save(BlogView blogView) {
        return repo.save(blogView);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    public List<Object[]> findMostReadBlogs() {
        return repo.findMostReadBlogs();
    }

	public Optional<BlogView> getByBlogPostIdAndUserIdAndViewDate(BlogPost blog, User user, int today) {
		return repo.getByBlogPostIdAndUserIdAndViewDate(blog, user, today);
	}

	public Optional<BlogView> getByBlogPostIdAndViewDate(BlogPost blog, int today) {
		return repo.getByBlogPostIdAndAndViewDate(blog, today);
	}

	public void deleteByBlogPostId(BlogPost blog) {
		repo.deleteByBlogPostId(blog);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}
}
