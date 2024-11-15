package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.PartnerDto;
import com.kdm360.bridalweb.model.Partner;
import com.kdm360.bridalweb.service.PartnerService;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.BackgroundImage.TYPE;
import com.kdm360.bridalweb.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/partners")
public class PartnersController {

	@Autowired
    private PartnerService partnerService;
	    
	@Value("${file.image.location}")
    private String imageLocation;
    
    @GetMapping
    public String listPartners(Model model, Authentication authentication) {
    	    		    
        List<Partner> partners = partnerService.get();
        model.addAttribute("partners", partners);
        
        if(authentication == null) {
        	return "redirect:/login"; 
    	}else {
    		return "admin/partner-list";
    	}	
    }

    @GetMapping("/new")
    public String showPartnerForm(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	Partner partner = new Partner();
    	model.addAttribute("partner", partner);
        
        return "admin/partner-form";
    }

    @PostMapping
    public String savePartner(PartnerDto partnerDto, Model model, Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("save partner " + partnerDto);

    	User loggedUser = (User)session.getAttribute("user");
    	if(authentication == null) {
        	return "redirect:/login"; 
    	}
    	
    	Partner partner;
    	if((partnerDto.getId() != null) && (partnerService.get(partnerDto.getId()).isPresent())) {
            partner = partnerService.get(partnerDto.getId()).get();
    	}else {
    		partner = new Partner();
    	}
    	partner.setName(partnerDto.getName());
    	partner.setWebSite(partnerDto.getWebSite());
    	
        if((partnerDto.getImage().isEmpty())) {
    		model.addAttribute("error", "Image cannot be empty.");
    		model.addAttribute("partner", partner);

    		return "admin/partner-form"; 
    	}
        partnerService.save(partner);
        
        if (!partnerDto.getImage().isEmpty()) {
            String imagePath;
			try {
				imagePath = saveFile(partnerDto.getImage(), "partners/", "IMAGE"+partner.getId() + ".png");
				partner.setImage(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        partnerService.save(partner);
        
        return "redirect:/partners"; 
    }

    @GetMapping("/edit/{id}")
    public String showEditPartnerForm(@PathVariable("id") Long id, Model model) {
    	Partner partner = partnerService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("partner", partner);
      return "admin/partner-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePartner(@PathVariable("id") Long id) {
    	partnerService.delete(id);
        return "redirect:/partners";
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
