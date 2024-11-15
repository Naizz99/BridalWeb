package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.BlogDto;
import com.kdm360.bridalweb.dto.VendorDto;
import com.kdm360.bridalweb.dto.VendorPackageDto;
import com.kdm360.bridalweb.model.BlogComment;
import com.kdm360.bridalweb.model.BlogLike;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.BlogView;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorCategory;
import com.kdm360.bridalweb.model.VendorPackage;
import com.kdm360.bridalweb.model.VendorProfileView;
import com.kdm360.bridalweb.service.BlogCommentService;
import com.kdm360.bridalweb.service.BlogLikeService;
import com.kdm360.bridalweb.service.BlogPostService;
import com.kdm360.bridalweb.service.BlogViewService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.VendorPackageService;
import com.kdm360.bridalweb.service.VendorService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blogs")
public class BlogController {

	@Autowired
    private BlogPostService blogService;
	
	@Autowired
    private BlogCommentService blogCommentService;
	
	@Autowired
    private BlogLikeService blogLikeService;
	
	@Autowired
    private BlogViewService blogViewService;
	
    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorPackageService vendorPackageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Value("${file.image.location}")
    private String imageLocation;
    
    @GetMapping
    public String listBlogPosts(Model model, Authentication authentication) {
    	    		    
        List<BlogPost> blogs = blogService.get();
        
        List<BlogPost> activeBlogs = blogService.get(true);
        
        BlogPost blog = new BlogPost();
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
        
        if(authentication != null) {
    		User user = userService.findByUsername(authentication.getName()).orElse(null);
    	    
    		if (user.getRole().equals(User.ROLE.CUSTOMER)) {
    			model.addAttribute("blogs", blogs);
    			model.addAttribute("activeBlogs", activeBlogs);
    			model.addAttribute("blog", blog);
            	return "blogs";
    	    }else if(user.getRole().equals(User.ROLE.VENDOR)){
    	    	blogs = blogService.findByUserId(user);
    	        model.addAttribute("blogs", blogs);
            	return "admin/blog-list";
            }else {
            	model.addAttribute("blogs", blogs);
            	model.addAttribute("activeBlogs", activeBlogs);
            	return "admin/blog-list";
            }
    	}else {
    		model.addAttribute("blogs", blogs);
			model.addAttribute("activeBlogs", activeBlogs);
    		return "blogs";
    	}	
    }

    @GetMapping("/new")
    public String showNewBlogForm(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	BlogPost blog = new BlogPost();
    	blog.setUserId(user);
    	
        model.addAttribute("blog", blog);
        
        return "admin/blog-form";
    }

    @PostMapping
    public String saveBlogPost(BlogDto blogDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveBlogPost " + blogDto);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	BlogPost blog;

    	if((blogDto.getBlogPostId() != null) && (blogService.get(blogDto.getBlogPostId()).isPresent())) {
    		blog = blogService.get(blogDto.getBlogPostId()).get();
    		blog.setActive(blogDto.isActive());
    	}else {
        	blog = new BlogPost();
        	blog.setActive(true);
        	blog.setPublishedDate(LocalDateTime.now());
        	blog.setUserId(loggedUser);
        }

    	
    	blog.setTitle(blogDto.getTitle());
    	blog.setContent(blogDto.getContent());
    	blog.setUpdatedDate(LocalDateTime.now());

    	if((blogDto.getTitle().isEmpty()) || (blogDto.getContent().isEmpty())) {
    		model.addAttribute("error", "Required fields are missing.");
    		
    		model.addAttribute("blog", blog);

    		return "admin/blog-form"; 
    		
    	}
        blogService.save(blog);
        
        try {
            if (!blogDto.getImage().isEmpty()) {
                String logoPath = saveFile(blogDto.getImage(), blog.getUserId().getUserId(), blog.getBlogPostId(), "BLOG");
                blog.setImage(logoPath);
            }
            blogService.save(blog);	
            redirectAttributes.addFlashAttribute("message", "Blog Post saved successfully!");

        } catch (IOException e) {
        	e.printStackTrace();
        	redirectAttributes.addFlashAttribute("error", "Error saving blog: " + e.getMessage());
        }

        if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
        	return "redirect:/dashboard"; 
        }else {
        	return "redirect:/blogs"; 
        }        
    }

    @GetMapping("/{id}")
    public String showBlogPost(@PathVariable("id") Long id, Model model, Authentication authentication,  HttpSession session) {
        
    	BlogPost blog = blogService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blog", blog);
        
        BlogView view;
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        if(authentication != null) {
        	User user = (User)session.getAttribute("user");
        	if((user.getRole().equals(User.ROLE.CUSTOMER)) && (!blog.getUserId().equals(user))) {
        		view = blogViewService.getByBlogPostIdAndUserIdAndViewDate(blog, user, today).orElse(null);
                
            	if(view == null) {
            		view = new BlogView();
                    view.setBlogPostId(blog);
                    view.setUserId(user);
                    view.setViewDate(today);
                    view.setViewCount(1);
            	}else {
            		view.setViewCount(view.getViewCount() + 1);
            	}
            	blogViewService.save(view);      
        	}
        }else {
        	view = blogViewService.getByBlogPostIdAndUserIdAndViewDate(blog, null, today).orElse(null);
            
        	if(view == null) {
        		view = new BlogView();
                view.setBlogPostId(blog);
                view.setUserId(null);
                view.setViewDate(today);
                view.setViewCount(1);
        	}else {
        		view.setViewCount(view.getViewCount() + 1);
        	}
        	blogViewService.save(view);
        }   
        
        List<BlogComment> commentList = blogCommentService.get(blog);
        model.addAttribute("commentList", commentList);
        
        List<BlogComment> subCommentList = new ArrayList<BlogComment>();
        for(BlogComment bc : commentList) {
        	if(bc.getParentCommentId() != null) {
        		subCommentList.add(bc);
        	}
        }
        model.addAttribute("subCommentList", subCommentList);
        
        BlogComment newComment = new BlogComment();
        newComment.setBlogPostId(blog);
        model.addAttribute("newComment", newComment);
        
        if(authentication != null) {
        	User loggedUser = (User)session.getAttribute("user");
        	if(blogLikeService.getByBlogPostIdAndUserId(blog,loggedUser).isPresent()) {
        		model.addAttribute("liked", true);
        	}else {
        		model.addAttribute("liked", false);
        	}
        }else {
        	model.addAttribute("liked", false);
        }
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
        return "blog";
        
    }
    
    @GetMapping("/edit/{id}")
    public String showEditBlogPostForm(@PathVariable("id") Long id, Model model) {
        BlogPost blog = blogService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("blog", blog);
      return "admin/blog-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlogPost(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    	
    	long userId = blogService.get(id).get().getUserId().getUserId();
    	BlogPost blog = blogService.get(id).get();
    	    	
    	blogCommentService.deleteByBlogPostId(blog);
    	blogLikeService.deleteByBlogPostId(blog);
    	blogViewService.deleteByBlogPostId(blog);
        blogService.delete(id);

        redirectAttributes.addFlashAttribute("error", "Blog has been deleted with id: " + id);
        User loggedUser = (User)session.getAttribute("user");
        if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
        	return "redirect:/dashboard"; 
        }else {
        	return "redirect:/blogs"; 
        }    
        
    }
    
    /*******************************/
    @PostMapping("/comment")
    public String saveBlogComment(BlogComment blogComment, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("saveBlogComment " + blogComment);

    	User loggedUser = (User)session.getAttribute("user");
    	
        if((blogComment.getContent().isEmpty()) || (blogComment.getBlogPostId() == null)) {
        	redirectAttributes.addFlashAttribute("blog", blogComment.getBlogPostId());
    		redirectAttributes.addFlashAttribute("error", "Required data are missing.");
    		return "redirect:/blogs/" + blogComment.getBlogPostId().getBlogPostId(); 
    	}
        blogComment.setUserId(loggedUser);
        blogComment.setPostedDate(LocalDateTime.now());
        blogCommentService.save(blogComment);
        
        redirectAttributes.addFlashAttribute("message", "Blog Post saved successfully!");

        return "redirect:/blogs/" + blogComment.getBlogPostId().getBlogPostId(); 
    }
    
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, HttpSession session, Authentication authentication) {
    	System.out.println("deleteComment " + id);
    	BlogComment comment = blogCommentService.get(id).get();
    	
    	if(authentication != null) {
    		User loggedUser = (User)session.getAttribute("user");
    		if((loggedUser.getRole().equals(User.ROLE.ADMIN)) || (comment.getUserId().equals(loggedUser)) || (comment.getBlogPostId().getUserId().equals(loggedUser))) {
    			blogCommentService.deleteByParentCommentId(comment);
    			blogCommentService.delete(id);
    		}
    		
    	}
    	return "redirect:/blogs/" + comment.getBlogPostId().getBlogPostId(); 
    }
    
    /*******************************/
    @GetMapping("/liked/{id}")
    public String blogLiked(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Authentication authentication,  HttpSession session) {
    	System.out.println("Blog Liked");
        BlogPost blog = blogService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        redirectAttributes.addFlashAttribute("blog", blog);
        
        BlogComment comment = new BlogComment();
        comment.setBlogPostId(blog);
        model.addAttribute("comment", comment);
        
        if(authentication != null) {
        	User loggedUser = (User)session.getAttribute("user");
        	System.out.println(id);
        	System.out.println(loggedUser);
        	System.out.println(blogLikeService.getByBlogPostIdAndUserId(blog,loggedUser).isPresent());
        	if(blogLikeService.getByBlogPostIdAndUserId(blog,loggedUser).isPresent()) {
        		blogLikeService.deleteByUserId(loggedUser);
        		redirectAttributes.addFlashAttribute("liked", false);
        	}else {
        		BlogLike like = new BlogLike();
        		like.setBlogPostId(blog);
        		like.setLikedDate(LocalDateTime.now());
        		like.setUserId(loggedUser);
        		blogLikeService.save(like);        		
        		redirectAttributes.addFlashAttribute("liked", true);
        	}
        }else {
        	redirectAttributes.addFlashAttribute("liked", false);
        }
        
      return "redirect:/blogs/" + blog.getBlogPostId();
    }
    
    /********************************/
    private String saveFile(MultipartFile file, long userId, long blogId, String type) throws IOException {        
        String fileName = "USER" + userId + type.toUpperCase() + blogId + ".png";
        String uploadDir = imageLocation + "blog/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/blog/" + fileName;
        
    }
}
