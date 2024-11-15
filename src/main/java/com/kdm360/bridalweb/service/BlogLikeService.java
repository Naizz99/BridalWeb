package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.BlogLike;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.BlogLikeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogLikeService {

	@Autowired
    private BlogLikeRepository repo;

    public List<BlogLike> get() {
        return repo.findAll();
    }

    public Optional<BlogLike> get(Long id) {
        return repo.findById(id);
    }

    public BlogLike save(BlogLike blogLike) {
        return repo.save(blogLike);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public Optional<BlogLike> getByBlogPostIdAndUserId(BlogPost blog, User user) {
		return repo.getByBlogPostIdAndUserId(blog, user);
	}

	@Transactional
	public void deleteByUserId(User loggedUser) {
		repo.deleteByUserId(loggedUser);
	}

	public void deleteByBlogPostId(BlogPost blog) {
		repo.deleteByBlogPostId(blog);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}
}
