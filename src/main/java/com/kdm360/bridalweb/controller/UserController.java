package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.PasswordDto;
import com.kdm360.bridalweb.dto.UserDto;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.service.BlogCommentService;
import com.kdm360.bridalweb.service.BlogLikeService;
import com.kdm360.bridalweb.service.BlogPostService;
import com.kdm360.bridalweb.service.BlogShareService;
import com.kdm360.bridalweb.service.BlogViewService;
import com.kdm360.bridalweb.service.CustomerInquiryService;
import com.kdm360.bridalweb.service.FavoriteVendorService;
import com.kdm360.bridalweb.service.UserLoginService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.VendorContactService;
import com.kdm360.bridalweb.service.VendorProfileViewService;
import com.kdm360.bridalweb.service.VendorRatingService;
import com.kdm360.bridalweb.service.VendorService;
import com.kdm360.bridalweb.service.WeddingPlanService;
import com.kdm360.bridalweb.service.WeddingService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
	    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BlogCommentService blogCommentService;
    
    @Autowired
    private BlogLikeService blogLikeService;
    
    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private BlogShareService blogShareService;
    
    @Autowired
    private BlogViewService blogViewService;
    
    @Autowired
    private CustomerInquiryService customerInquiryService;
    
    @Autowired
    private FavoriteVendorService favoriteVendorService;
    
    @Autowired
    private UserLoginService userLoginService;
    
    @Autowired
    private VendorService vendorService;    
    
    @Autowired
    private VendorContactService vendorContactService;    
    
    @Autowired
    private VendorProfileViewService vendorProfileViewService;    
    
    @Autowired
    private VendorRatingService vendorRatingService;    
    
    @Autowired
    private WeddingService weddingService;  
    
    @Autowired
    private WeddingPlanService weddingPlanService; 
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/admins")
    public String listAdmins(Model model, Authentication authentication) {
    	    		    
        List<User> admins = userService.get(User.ROLE.ADMIN);
        model.addAttribute("admins", admins);
        
        return "admin/admin-list";	
    }
    
    @GetMapping("/admins/new")
    public String newAdmin(Model model, HttpSession session, Authentication authentication) {
    	User loggedUser = (User)session.getAttribute("user");
    	
        User admin = new User();
        admin.setRole(User.ROLE.ADMIN);
        admin.setGender(User.GENDER.MALE);
        admin.setCreateBy(loggedUser.getUsername());
        admin.setCreateDate(LocalDate.now());
        admin.setStatus(User.STATUS.ACTIVE);
        admin.setUpdateBy(loggedUser.getUsername());
        admin.setUpdateDate(LocalDate.now());
        model.addAttribute("admin", admin);
        
        return "admin/admin-form";	
    }
        
    @GetMapping("/customers")
    public String listCustomers(Model model, Authentication authentication) {
    	    		    
        List<User> customers = userService.get(User.ROLE.CUSTOMER);
        model.addAttribute("customers", customers);
        
        return "admin/customer-list";	
    }
    
    @GetMapping("/customers/new")
    public String newCustomer(Model model, HttpSession session, Authentication authentication) {
    	User loggedUser = (User)session.getAttribute("user");
    	
        User customer = new User();
        customer.setRole(User.ROLE.CUSTOMER);
        customer.setGender(User.GENDER.MALE);
        customer.setCreateBy(loggedUser.getUsername());
        customer.setCreateDate(LocalDate.now());
        customer.setStatus(User.STATUS.ACTIVE);
        customer.setUpdateBy(loggedUser.getUsername());
        customer.setUpdateDate(LocalDate.now());
        model.addAttribute("customer", customer);
        
        return "admin/customer-form";	
    }
    
    @PostMapping
    public String saveUser(User user, Model model, HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
    	System.out.println("saveUser " + user);

    	String loggedUserName = user.getUsername();
    	
    	if(authentication != null) {
    		User loggedUser = (User)session.getAttribute("user");
    		loggedUserName = loggedUser.getUsername();
    	}
    	if((user.getUserId() != null) && (userService.get(user.getUserId()).isPresent())) {
    		user.setUsername(userService.get(user.getUserId()).get().getUsername());
    	}else {
    		user.setCreateBy(loggedUserName);
    		user.setCreateDate(LocalDate.now());
    		user.setStatus(User.STATUS.ACTIVE);
        }

    	if (user.getPassword() != null && !(user.getPassword()).trim().isEmpty()) {
		    user.setPassword(passwordEncoder.encode(user.getPassword()));
		}else {
			user.setPassword(userService.get(user.getUserId()).get().getPassword());
		}
    	
    	user.setUpdateBy(loggedUserName);
    	user.setUpdateDate(LocalDate.now());
               
        if((user.getFirstName().isEmpty()) || (user.getUsername().isEmpty()) || (user.getEmail().isEmpty()) || (user.getMobile().isEmpty())) {
    		redirectAttributes.addFlashAttribute("error", "Required fields are missing.");
    		redirectAttributes.addFlashAttribute("newUser", user);
    		if(user.getRole().equals(User.ROLE.ADMIN)) {
    			return "redirect:/users/admins"; 
    		}else if(user.getRole().equals(User.ROLE.VENDOR)) {
    			return "redirect:/users/vendors"; 
    		}else if(user.getRole().equals(User.ROLE.CUSTOMER)) {
    			if(authentication != null) {
    				User loggedUser = (User)session.getAttribute("user");
    	        	if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
    	            	return "redirect:/dashboard";
    	            }else {
    	            	return "redirect:/users/customers";
	            	}
    			}else if(authentication == null) {
    				return "redirect:/register";
    			}
    		}
    	}
                
        userService.save(user);
        if(authentication == null) {
        	redirectAttributes.addFlashAttribute("registered", "Account created successfully! ");
        	return "redirect:/login";
        }else {
        	User loggedUser = (User)session.getAttribute("user");
        	
        	redirectAttributes.addFlashAttribute("message", "Record updated successfully! ");
        	if(user.getRole().equals(User.ROLE.ADMIN)) {
    			return "redirect:/users/admins"; 
    		}else if(user.getRole().equals(User.ROLE.VENDOR)) {
    			return "redirect:/users/vendors"; 
    		}else if(user.getRole().equals(User.ROLE.CUSTOMER)) {
    			loggedUser = (User)session.getAttribute("user");
	        	if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
	        		redirectAttributes.addFlashAttribute("message", "Profile updated! ");
	            	return "redirect:/dashboard";
	            }else {
	            	return "redirect:/users/customers";
            	}
    		}
        }
		return "redirect:/users/customers";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
    	User user = userService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
    	if(user.getRole().equals(User.ROLE.ADMIN)) {
    		model.addAttribute("admin", user);
    		return "admin/admin-form";
    	}else if(user.getRole().equals(User.ROLE.CUSTOMER)) {
    		model.addAttribute("customer", user);
    		return "admin/customer-form";
    	}else {
    		return null;
    	}
    }
    
    @GetMapping("/delete/{id}")
    public String deleteBlogPost(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	
    	User user = userService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
    	if((!blogCommentService.existsByUserId(user)) 
    			&& (!blogLikeService.existsByUserId(user))
    			&& (!blogPostService.existsByUserId(user))
    			&& (!blogShareService.existsByUserId(user))
    			&& (!blogViewService.existsByUserId(user))
    			&& (!customerInquiryService.existsByUserId(user))
    			&& (!favoriteVendorService.existsByUserId(user))
    			&& (!vendorService.existsByUserId(user))
    			&& (!vendorContactService.existsByUserId(user))
    			&& (!vendorProfileViewService.existsByUserId(user))
    			&& (!vendorRatingService.existsByUserId(user))
    			&& (!weddingService.existsByUserId(user))
    			&& (!weddingPlanService.existsByUserId(user))) {
    		userService.delete(id);
    		try {
        		userLoginService.deleteByUser(user);
        	}catch(Exception ex) {
        		
        	}
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Record cannot be deleted due to relational data! ");
    	}
    	
    	if(user.getRole().equals(User.ROLE.ADMIN)) {
			return "redirect:/users/admins"; 
		}else if(user.getRole().equals(User.ROLE.VENDOR)) {
			return "redirect:/users/vendors"; 
		}else{
			return "redirect:/users/customers";
		}
    }
    
    @GetMapping("/validateUsername")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
    	System.out.println("username : " + username);
        try {
        	User user = userService.findByUsername(username).get();
        	System.out.println("user : " + user);
        	return new ResponseEntity<>("false", HttpStatus.OK);
        }catch(Exception e) {
        	return new ResponseEntity<>("true", HttpStatus.OK);
        }
    }
    
    @PostMapping("/updateProfile")
    public String updateProfile(UserDto userDto, Model model, HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
    	System.out.println("update profile " + userDto);

    	if(authentication == null) {
    		return "redirect:/login";
    	}
    	
    	User loggedUser = (User)session.getAttribute("user");
    	
    	if(loggedUser.getUserId() != userDto.getUserId()) {
    		redirectAttributes.addFlashAttribute("error", "Invalid Profile.");
    		return "redirect:/profile";
    	}
    	
    	loggedUser.setFirstName(userDto.getFirstName());
    	loggedUser.setLastName(userDto.getLastName());
    	loggedUser.setEmail(userDto.getEmail());
    	loggedUser.setMobile(userDto.getMobile());
    	loggedUser.setGender(userDto.getGender());
    	loggedUser.setUsername(userDto.getUsername());
    	
        if((userDto.getFirstName().isEmpty()) || (userDto.getUsername().isEmpty()) || (userDto.getEmail().isEmpty()) || (userDto.getMobile().isEmpty())) {
    		redirectAttributes.addFlashAttribute("error", "Required fields are missing.");
    		redirectAttributes.addFlashAttribute("user", loggedUser);
    		return "redirect:/profile";
    	}
            
        loggedUser.setUpdateBy(loggedUser.getUsername());
        loggedUser.setUpdateDate(LocalDate.now());
        userService.save(loggedUser);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully! ");
        return "redirect:/profile";
    }
    
    @PostMapping("/updatePassword")
    public String updatePassword(PasswordDto passwordDto, Model model, HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
    	System.out.println("update password " + passwordDto);

    	if(authentication != null) {
    		User loggedUser = (User)session.getAttribute("user");
        	
        	if(loggedUser.getUserId() != passwordDto.getUserId()) {
        		redirectAttributes.addFlashAttribute("error", "Invalid Profile.");
        		return "redirect:/profile";
        	}
        	
        	if(!passwordEncoder.matches(passwordDto.getCurrentPassword(), loggedUser.getPassword())) {
        		redirectAttributes.addFlashAttribute("error", "Invalid current password. Please try again.");
        		return "redirect:/profile";
        	}
        	
        	if(!passwordDto.getNewPassword().equals(passwordDto.getNewPassword2())) {
        		redirectAttributes.addFlashAttribute("error", "The new password you entered doesn’t match the re-typed password.");
        		return "redirect:/profile";
        	}
        	
        	loggedUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        	loggedUser.setUpdateBy(loggedUser.getUsername());
            loggedUser.setUpdateDate(LocalDate.now());
            userService.save(loggedUser);
            redirectAttributes.addFlashAttribute("message", "Password updated successfully! ");
            return "redirect:/profile";
    	}else {
    		if((passwordDto.getUsername() == null) || (!userService.findByUsername(passwordDto.getUsername()).isPresent())) {
        		redirectAttributes.addFlashAttribute("error", "Invalid Username.");
        		return "redirect:/forgotPassword";
        	}
    		
    		User user = userService.findByUsername(passwordDto.getUsername()).get();
    		
    		if(!passwordDto.getOtp().equals(user.getTempOTP())) {
        		redirectAttributes.addFlashAttribute("error", "The OTP you entered is incorrect. Please try again.");
        		return "redirect:/forgotPassword";
        	}
    		
    		if(!passwordDto.getNewPassword().equals(passwordDto.getNewPassword2())) {
        		redirectAttributes.addFlashAttribute("error", "The new password you entered doesn’t match the re-typed password.");
        		return "redirect:/forgotPassword";
        	}
    		
    		user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
    		user.setUpdateBy(user.getUsername());
        	user.setUpdateDate(LocalDate.now());
        	user.setTempOTP(null);
            userService.save(user);
            redirectAttributes.addFlashAttribute("message", "Password reset successfully! ");
    		return "redirect:/forgotPassword";
    	}
    	
    }
}
