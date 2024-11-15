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

import com.kdm360.bridalweb.model.FAQ;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.service.FAQService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/faq")
public class FAQController {

	@Autowired
    private FAQService faqService;
	    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Autowired
    private SettingsService settingsService;
    
    @GetMapping
    public String listFAQ(Model model, Authentication authentication) {
    	    		    
        List<FAQ> questions = faqService.get();
        model.addAttribute("questions", questions);
        
        List<FAQ> activeQuestions = faqService.get(true);
        model.addAttribute("activeQuestions", activeQuestions);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
        if(authentication != null) {
    		User user = userService.findByUsername(authentication.getName()).orElse(null);
    	    
    		if (user.getRole().equals(User.ROLE.CUSTOMER)) {
            	return "faq";
    	    }else {
            	return "admin/faq-list";
            }
    	}else {
    		return "faq";
    	}	
    }

    @GetMapping("/new")
    public String showNewBlogForm(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	FAQ faq = new FAQ();
    	    	
        model.addAttribute("faq", faq);
        
        return "admin/faq-form";
    }

    @PostMapping
    public String saveBlogPost(FAQ faq, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("save faq " + faq);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	if((faq.getFaqId() != null) && (faqService.get(faq.getFaqId()).isPresent())) {

    	}else {
    		faq.setActive(true);
    		faq.setCreatedDate(LocalDateTime.now());
        }

    	faq.setUpdatedDate(LocalDateTime.now());
               
        if((faq.getQuestion().isEmpty()) || (faq.getAnswer().isEmpty())) {
    		model.addAttribute("error", "Question & Answer cannot be empty.");
    		
    		model.addAttribute("faq", faq);

    		return "admin/faq-form"; 
    		
    	}
                
        faqService.save(faq);
        
        return "redirect:/faq"; 
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogPostForm(@PathVariable("id") Long id, Model model) {
        FAQ faq = faqService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid blog Id:" + id));
        model.addAttribute("faq", faq);
      return "admin/faq-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlogPost(@PathVariable("id") Long id) {
    	faqService.delete(id);
        return "redirect:/faq";
    }
    
}
