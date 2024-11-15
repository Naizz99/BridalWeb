//package com.kdm360.bridalweb.controller;
//
//import org.springframework.web.bind.annotation.*;
//import com.kdm360.bridalweb.model.BlogView;
//import com.kdm360.bridalweb.service.BlogViewService;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/blog-views")
//public class BlogViewController {
//
//    private final BlogViewService blogViewService;
//
//    public BlogViewController(BlogViewService blogViewService) {
//        this.blogViewService = blogViewService;
//    }
//
//    @GetMapping
//    public List<BlogView> getAllBlogViews() {
//        return blogViewService.getAllBlogViews();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<BlogView> getBlogViewById(@PathVariable Long id) {
//        return blogViewService.getBlogViewById(id);
//    }
//
//    @PostMapping
//    public BlogView addBlogView(@RequestBody BlogView blogView) {
//        return blogViewService.addBlogView(blogView);
//    }
//
//    @PutMapping("/{id}")
//    public BlogView updateBlogView(@PathVariable Long id, @RequestBody BlogView blogView) {
//        return blogViewService.updateBlogView(id, blogView);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteBlogView(@PathVariable Long id) {
//        blogViewService.deleteBlogView(id);
//    }
//}
