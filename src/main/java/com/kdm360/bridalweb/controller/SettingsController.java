package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.SettingsDto;
import com.kdm360.bridalweb.model.BackgroundImage;
import com.kdm360.bridalweb.model.BackgroundImage.TYPE;
import com.kdm360.bridalweb.model.Gallery;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.VendorGallery;
import com.kdm360.bridalweb.service.BackgroundImageService;
import com.kdm360.bridalweb.service.BlogCommentService;
import com.kdm360.bridalweb.service.BlogLikeService;
import com.kdm360.bridalweb.service.BlogPostService;
import com.kdm360.bridalweb.service.BlogShareService;
import com.kdm360.bridalweb.service.BlogViewService;
import com.kdm360.bridalweb.service.CustomerInquiryService;
import com.kdm360.bridalweb.service.FavoriteVendorService;
import com.kdm360.bridalweb.service.GalleryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserLoginService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.VendorContactService;
import com.kdm360.bridalweb.service.VendorProfileViewService;
import com.kdm360.bridalweb.service.VendorRatingService;
import com.kdm360.bridalweb.service.VendorService;
import com.kdm360.bridalweb.service.WeddingPlanService;
import com.kdm360.bridalweb.service.WeddingService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private BackgroundImageService backgroundImageService;
    
    @Autowired
    private GalleryService galleryService;
    
    @Value("${file.image.location}")
    private String imageLocation;
    
    @GetMapping
    public String settings(Authentication authentication, Model model) {
    	if(authentication == null) {
    		return "redirect:/login";
    	}
        
        model.addAttribute("setting",settingsService.get(1).get());
        model.addAttribute("landingContents",backgroundImageService.get(settingsService.get(1).get()));
        model.addAttribute("gallery", galleryService.get());
        return "admin/settings";
    }
    
    @PostMapping
    public String saveUser(SettingsDto settingDto, Model model, HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
    	System.out.println("save settings " + settingDto);
    	
    	if(authentication == null) {
    		return "redirect:/login";
    	}
    	
    	User loggedUser = (User)session.getAttribute("user");
    	
    	Settings setting = settingsService.get(settingDto.getId()).get();
    	setting.setSystemName(settingDto.getSystemName());
    	setting.setEmail1(settingDto.getEmail1());
    	setting.setEmail2(settingDto.getEmail2());
    	setting.setMobile1(settingDto.getMobile1());
    	setting.setMobile2(settingDto.getMobile2());
    	setting.setFooterDescription(settingDto.getFooterDescription());
    	setting.setAboutUs(settingDto.getAboutUs());
    	setting.setWhatsapp(settingDto.getWhatsapp());
    	setting.setFacebook(settingDto.getFacebook());
    	setting.setInstagram(settingDto.getInstagram());
    	setting.setTwitter(settingDto.getTwitter());
    	
    	
        if((settingDto.getSystemName().isEmpty()) || (settingDto.getEmail1().isEmpty()) || (settingDto.getMobile1().isEmpty()) || (settingDto.getFooterDescription().isEmpty()) || (settingDto.getAboutUs().isEmpty())) {
    		redirectAttributes.addFlashAttribute("error", "Required fields are missing.");
    		redirectAttributes.addFlashAttribute("setting", setting);
    		return "redirect:/settings";
    	}
                
        try {
            if (!settingDto.getSystemLogo().isEmpty()) {
                String logoPath = saveFile(settingDto.getSystemLogo(), "settings/", "logo.png");
                setting.setSystemLogo(logoPath);
            }
            
            if ((settingDto.getLandingContents() != null) && (!settingDto.getLandingContents().isEmpty())) {
                try {
                	for (MultipartFile image1 : settingDto.getLandingContents()) {
                        if (!image1.isEmpty()) {

                        	BackgroundImage bgImage = new BackgroundImage();
                        	bgImage.setSettingsId(setting);
                        	String contentType = image1.getContentType();
                            
                            if (contentType != null) {
                                if (contentType.startsWith("image")) {
                                    bgImage.setType(TYPE.IMAGE);
                                } else if (contentType.startsWith("video")) {
                                    bgImage.setType(TYPE.VIDEO);
                                } else {
                                    continue; 
                                }
                            }
                            backgroundImageService.save(bgImage);
                            
                        	String imagePath = saveFile(image1, "settings/", (bgImage.getType()==TYPE.IMAGE)? "bgContent"+bgImage.getImageId() + ".png" : "bgContent"+bgImage.getImageId() + ".mp4");
                            
                            bgImage.setPath(imagePath);
                            backgroundImageService.save(bgImage);
                        }
                    }
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            
            if ((settingDto.getGallery() != null) && (!settingDto.getGallery().isEmpty())) {
                try {
                	for (MultipartFile image1 : settingDto.getGallery()) {
                        if (!image1.isEmpty()) {

                        	Gallery image = new Gallery();
                        	galleryService.save(image);
                            
                        	String imagePath = saveFile(image1, "gallery/", "GALLERY"+image.getGalleryId() + ".png");
                            
                        	image.setImage(imagePath);
                        	galleryService.save(image);
                        }
                    }
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            
        } catch (IOException e) {
        	e.printStackTrace();
        	redirectAttributes.addFlashAttribute("error", "Error saving image: " + e.getMessage());
        }
        
        settingsService.save(setting);
        redirectAttributes.addFlashAttribute("message", "Settings updated successfully! ");
        return "redirect:/settings";
    }

    
    @GetMapping("/landingContent/delete/{id}")
    public String deleteLandingContent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		backgroundImageService.delete(id);
    	
		return "redirect:/settings";
    }
    
    @GetMapping("/galleryImage/active/{id}")
    public String actveGalleryImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	Gallery galleryImage = galleryService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
    	galleryImage.setActive(true);
		galleryService.save(galleryImage);
		return "redirect:/settings";
    }
    
    @GetMapping("/galleryImage/deactve/{id}")
    public String deactveGalleryImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	Gallery galleryImage = galleryService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
    	galleryImage.setActive(false);
		galleryService.save(galleryImage);
		return "redirect:/settings";
    }
    
    @GetMapping("/galleryImage/delete/{id}")
    public String deleteGalleryImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		galleryService.delete(id);
    	
		return "redirect:/settings";
    }
    
    /********************************/
    private String saveFile(MultipartFile file, String folderName, String name) throws IOException {        
        String fileName = name.toUpperCase();
        String uploadDir = imageLocation + folderName;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/" + folderName + fileName;
        
    } 
	
}
