package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.BlogShare;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.BlogShareRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogShareService {

    private final BlogShareRepository repo;

    @Autowired
    public BlogShareService(BlogShareRepository repo) {
        this.repo = repo;
    }

    public List<BlogShare> getAllBlogShares() {
        return repo.findAll();
    }

    public Optional<BlogShare> getBlogShareById(Long id) {
        return repo.findById(id);
    }

    public BlogShare addBlogShare(BlogShare blogShare) {
        return repo.save(blogShare);
    }

    public BlogShare updateBlogShare(Long id, BlogShare updatedBlogShare) {
        Optional<BlogShare> existingBlogShare = repo.findById(id);
        if (existingBlogShare.isPresent()) {
            BlogShare blogShare = existingBlogShare.get();
            blogShare.setUserId(updatedBlogShare.getUserId());
            blogShare.setSharedDate(updatedBlogShare.getSharedDate());
            blogShare.setBlogPostId(updatedBlogShare.getBlogPostId());
            return repo.save(blogShare);
        } else {
            throw new RuntimeException("BlogShare not found with id " + id);
        }
    }

    public void deleteBlogShare(Long id) {
        repo.deleteById(id);
    }

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}
}
