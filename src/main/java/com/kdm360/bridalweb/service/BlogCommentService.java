package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.BlogComment;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.BlogCommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogCommentService {

	@Autowired
    private BlogCommentRepository repo;

    public List<BlogComment> get() {
        return repo.findAll();
    }
    
    public List<BlogComment> get(BlogPost blog) {
        return repo.findByBlogPostId(blog);
    }

    public Optional<BlogComment> get(Long id) {
        return repo.findById(id);
    }

    public BlogComment save(BlogComment blogComment) {
        return repo.save(blogComment);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    @Transactional
	public void deleteByParentCommentId(BlogComment comment) {
    	repo.deleteByParentCommentId(comment);
	}

	public void deleteByBlogPostId(BlogPost blog) {
		repo.deleteByBlogPostId(blog);
	}
	
	public boolean existsByUserId(User user) {
        return repo.existsByUserId(user);
    }
}
