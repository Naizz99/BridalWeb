package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.dto.BlogStatsDto;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.BlogPostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

	@Autowired
    private BlogPostRepository repo;

    public List<BlogPost> get() {
        return repo.findAll();
    }

    public List<BlogPost> get(boolean active) {
        return repo.getByActive(active);
    }
    
    public Optional<BlogPost> get(Long id) {
        return repo.findById(id);
    }

    public BlogPost save(BlogPost blogPost) {
        return repo.save(blogPost);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public List<BlogPost> findByUserId(User user) {
		return repo.findByUserId(user);
	}

	public BlogStatsDto findBlogPostStatsById(Long blogPostId) {
		return repo.findBlogPostStatsById(blogPostId);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}
}
