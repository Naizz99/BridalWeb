package com.kdm360.bridalweb.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.model.Message;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.dto.BlogDto;
import com.kdm360.bridalweb.dto.ServiceCategoryDto;
import com.kdm360.bridalweb.dto.VendorProfileViewDto;
import com.kdm360.bridalweb.dto.WeddingDto;
import com.kdm360.bridalweb.dto.WeddingPlanRequest;
import com.kdm360.bridalweb.dto.VendorContactDto;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.Email;
import com.kdm360.bridalweb.model.FAQ;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorContact;
import com.kdm360.bridalweb.model.VendorProfileView;
import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.model.WeddingPlan;
import com.kdm360.bridalweb.repository.EmailService;
import com.kdm360.bridalweb.service.BackgroundImageService;
import com.kdm360.bridalweb.service.BlogPostService;
import com.kdm360.bridalweb.service.EmailServiceImpl;
import com.kdm360.bridalweb.service.PartnerService;
import com.kdm360.bridalweb.service.MessageService;
import com.kdm360.bridalweb.service.FAQService;
import com.kdm360.bridalweb.service.GalleryService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.VendorCategoryService;
import com.kdm360.bridalweb.service.VendorContactService;
import com.kdm360.bridalweb.service.VendorProfileViewService;
import com.kdm360.bridalweb.service.VendorService;
import com.kdm360.bridalweb.service.WeddingGalleryService;
import com.kdm360.bridalweb.service.WeddingPlanService;
import com.kdm360.bridalweb.service.WeddingService;
import com.kdm360.bridalweb.service.WeddingTypeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorCategoryService vendorCategoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private GalleryService galleryService;
    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private PartnerService clientService;
    
    @Autowired
    private BackgroundImageService backgroundImageService;
    
    @Autowired
    private MessageService contactUsService;
    
    @Autowired
    private FAQService faqService;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Autowired
    private WeddingPlanService weddingPlanService;
    
    @Autowired
    private WeddingService weddingService;
    
    @Autowired
    private WeddingGalleryService weddingGalleryService;
    
    @Autowired
    private WeddingTypeService weddingTypeService;
    
    @Autowired
    private VendorProfileViewService vendorProfileViewService;
    
    @Autowired
    private VendorContactService vendorContactService;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private EmailServiceImpl emailService;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/test")
    public String test(Model model) {   	
    	model.addAttribute("title", "123");
        return "test";
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpSession session) throws ServletException {
    	User user = (User)session.getAttribute("user");
    	String url;
    	if(user.getRole().equals(User.ROLE.CUSTOMER)) {
    		url="index";
    	}else {
    		url="login";
    	}
        request.logout();
        
        return "redirect:/" + url; // Redirect to login page
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session, Authentication authentication) {
    	
    	if(authentication != null) {
    		User user = userService.findByUsername(authentication.getName()).orElse(null);
    	    
    	    if (user != null) {
    	    	session.setAttribute("user", user);
    	    }	
    	}
    	
    	List<ServiceCategoryDto> vendorCategoryList = new ArrayList<ServiceCategoryDto>();
    	for(ServiceCategory sc: serviceCategoryService.get(true)) {
    		ServiceCategoryDto scDto = new ServiceCategoryDto(
	    				sc.getCategoryId(),
	    				sc.getTypeName(),
	    				null,
	    				sc.getDescription(),
	    				sc.isActive(),
	    				sc.getImage(),
	    				vendorCategoryService.getCountByCategoryId(sc)
    				);
    		vendorCategoryList.add(scDto);
    	}
    	model.addAttribute("vendorCategoryList", vendorCategoryList);
    	
    	model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
    	model.addAttribute("activeVendors", vendorService.findByActiveUsers());
    	model.addAttribute("blogList", blogPostService.get(true));
    	model.addAttribute("gallery", galleryService.get(true));
    	
    	Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
    	model.addAttribute("clients", clientService.get());

    	model.addAttribute("backgroundImages", backgroundImageService.get(settings.get()));
    	
        return "index";
    }
    
    @GetMapping("/about-us")
    public String about(Model model) {
    	Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        return "about-us";
    }
    
//    @GetMapping("/faq")
//    public String faq(Model model) {
//    	List<FAQ> faqList = faqService.get();
//    	System.out.println("faqList : " + faqList);
//    	model.addAttribute("faqList", faqList);
//        return "faq";
//    }
//    
    @GetMapping("/new-vendor")
    public String newVendor(Model model) {
            	
    	User vendorUser = new User();
    	vendorUser.setRole(User.ROLE.VENDOR);
    	
    	Vendor vendor = new Vendor();
    	vendor.setRegistrationDate(LocalDate.now());
    	vendor.setUserId(vendorUser);    	
        model.addAttribute("vendor", vendor);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	    	
        return "vendor-form";
        
    }
    
    @GetMapping("/dashboard")
    public String adminPanel(Model model, HttpSession session, Authentication authentication) {
    	
    	if(authentication == null) {
    		return "redirect:/login";
    	}
    	
    	User user = userService.findByUsername(authentication.getName()).get();
    	session.setAttribute("user", user);
    	model.addAttribute("user", user);
    	
//    	int msgCout = messageService.getCountByIsRead(false);
//    	System.out.println("msgCout : " + msgCout);
    	
    	if(user.getRole().equals(User.ROLE.CUSTOMER)) {
    		model.addAttribute("weddingPlanList", weddingPlanService.get(user));
    		model.addAttribute("newWeddingPlan", new WeddingPlan());
    		
    		List<Wedding> weddingList = weddingService.get(user);
    		List<WeddingDto> formattedWeddingList = new ArrayList<WeddingDto>();            
            for(Wedding w : weddingList) {
            	WeddingDto wd = new WeddingDto();
            	wd.setWeddingId(w.getWeddingId());
            	wd.setName(w.getName());
            	wd.setLocation(w.getLocation());
            	wd.setDate(w.getDate());
            	wd.setBudget(w.getBudget());
            	wd.setWeddingTypeId(w.getWeddingTypeId().getTypeName());
            	wd.setTypeId(w.getWeddingTypeId().getWeddingTypeId());
            	wd.setUserId(w.getUserId());
            	wd.setCreatedBy(w.getCreatedBy());
            	wd.setCreatedDate(w.getCreatedDate());
        		if(!weddingGalleryService.getByWeddingId(w).isEmpty()) {
            		wd.setImage(weddingGalleryService.getByWeddingId(w).get(0).getImage());
            	}else {
            		wd.setImage("/images/null.jpg");
            	}
        		formattedWeddingList.add(wd);
            }
            model.addAttribute("weddingList", formattedWeddingList);
    		model.addAttribute("newWedding", new Wedding());
    		model.addAttribute("weddingTypes", weddingTypeService.get(true));
    		
    		model.addAttribute("categories", serviceCategoryService.get(true));
    		    		
    		model.addAttribute("newBlog", new BlogPost());
    		List<BlogPost> blogList = blogPostService.findByUserId(user);
    		List<BlogDto> formattedBlogList = blogList.stream().map(blog -> {
    			BlogDto dto = new BlogDto();
    		    dto.setBlogPostId(blog.getBlogPostId());
    		    dto.setTitle(blog.getTitle());
    		    dto.setContent(blog.getContent());
    		    dto.setPublishedDate(blog.getPublishedDate());
    		    dto.setUpdatedDate(blog.getUpdatedDate());
    		    dto.setImagePath(blog.getImage());
    		    dto.setActive(blog.isActive());
    		    dto.setUserId(blog.getUserId().getUserId());
    		    dto.setBlogStatus(blogPostService.findBlogPostStatsById(blog.getBlogPostId()));
    		    return dto;
    		}).collect(Collectors.toList());
    	    model.addAttribute("blogList", formattedBlogList);
    	    
    		List<VendorProfileView> searchedVendorList = vendorProfileViewService.findByUserIdOrderByViewDateDesc(user);
    		List<VendorProfileViewDto> formattedVendorList = searchedVendorList.stream().map(vendor -> {
    		    VendorProfileViewDto dto = new VendorProfileViewDto();
    		    dto.setViewId(vendor.getViewId());
    		    dto.setVendorId(vendor.getVendorId());
    		    dto.setUserId(vendor.getUserId());
    		    dto.setIpAddress(vendor.getIpAddress());
    		    dto.setViewCount(vendor.getViewCount());
    		    dto.setViewDate(vendor.getViewDate());
    		    LocalDate date = LocalDate.parse(vendor.getViewDate().toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    		    dto.setFormattedViewDate(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    		    
    		    return dto;
    		}).collect(Collectors.toList());
    	    model.addAttribute("searchedVendorList", formattedVendorList);
    	        	    
    	    List<VendorContact> contactedVendorList = vendorContactService.findByUserId(user);
    		List<VendorContactDto> formattedContactedVendorList = contactedVendorList.stream().map(vendor -> {
    			VendorContactDto dto = new VendorContactDto();
    		    dto.setContactId(vendor.getContactId());
    		    dto.setVendorId(vendor.getVendorId());
    		    dto.setUserId(vendor.getUserId());
    		    dto.setIpAddress(vendor.getIpAddress());
    		    dto.setContactDate(vendor.getContactDate());
    		    dto.setContactTime(vendor.getContactTime());
    		    dto.setFormattedContactDate(LocalDate.parse(vendor.getContactDate().toString(), DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    		    dto.setFormattedContactTime(LocalTime.parse(String.format("%06d", vendor.getContactTime()), DateTimeFormatter.ofPattern("HHmmss")).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    		    
    		    return dto;
    		}).collect(Collectors.toList());
    	    model.addAttribute("contactedVendorList", formattedContactedVendorList);
    	        		
    	    model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
            Optional<Settings> settings = settingsService.get(1);
        	model.addAttribute("settings", settings.get());
        	
    		return "customer-account";
    	}else{
    		return "admin/index";
    	}
    	
    }
    
    @GetMapping("/register")
    public String register(Model model) {
    	User user = new User();
    	user.setRole(User.ROLE.CUSTOMER);
    	user.setStatus(User.STATUS.ACTIVE);
 
    	model.addAttribute("user",user);
        return "admin/customer-registration";
    }
    
    @GetMapping("/forgotPassword")
    public String forgottenPassword() {
        return "admin/forgot-password";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin/user-profile";
    }
    
    @PostMapping("/uploadImage")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        String fileName = "PRD00" + System.currentTimeMillis() + ".png";
        String uploadDir = "/vendor/" + type + "/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok().body("{\"success\": true, \"fileName\": \"" + fileName + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Error saving file.\"}");
        }
    }
        
    @GetMapping("/sendOtp")
	public ResponseEntity<Object> sendOtp(@RequestParam String username) {
		System.out.println("send OTP " + username);
		
		User user = userService.findByUsername(username).get();
		
		if(!user.equals(null)) {
			
			String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));

			user.setTempOTP(otp);
			userService.save(user);

			Email email = new Email(
					user.getEmail(),
    				("One Time Password"),
    				("Dear User, \n\n Your OTP is : " + otp));
    		emailService.sendMail(email);

    		return new ResponseEntity<>("sent",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
}
