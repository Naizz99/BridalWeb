package com.kdm360.bridalweb.controller;

import org.springframework.web.bind.annotation.*;
import com.kdm360.bridalweb.model.BlogShare;
import com.kdm360.bridalweb.service.BlogShareService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-shares")
public class BlogShareController {

    private final BlogShareService blogShareService;

    public BlogShareController(BlogShareService blogShareService) {
        this.blogShareService = blogShareService;
    }

    @GetMapping
    public List<BlogShare> getAllBlogShares() {
        return blogShareService.getAllBlogShares();
    }

    @GetMapping("/{id}")
    public Optional<BlogShare> getBlogShareById(@PathVariable Long id) {
        return blogShareService.getBlogShareById(id);
    }

    @PostMapping
    public BlogShare addBlogShare(@RequestBody BlogShare blogShare) {
        return blogShareService.addBlogShare(blogShare);
    }

    @PutMapping("/{id}")
    public BlogShare updateBlogShare(@PathVariable Long id, @RequestBody BlogShare blogShare) {
        return blogShareService.updateBlogShare(id, blogShare);
    }

    @DeleteMapping("/{id}")
    public void deleteBlogShare(@PathVariable Long id) {
        blogShareService.deleteBlogShare(id);
    }
}
