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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdm360.bridalweb.dto.CustomerWeddingDto;
import com.kdm360.bridalweb.dto.WeddingDto;
import com.kdm360.bridalweb.dto.WeddingGalleryDto;
import com.kdm360.bridalweb.dto.WeddingPlanRequest;
import com.kdm360.bridalweb.dto.WeddingTypeDto;
import com.kdm360.bridalweb.model.BlogPost;
import com.kdm360.bridalweb.model.BudgetBreakdown;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorGallery;
import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.model.WeddingGallery;
import com.kdm360.bridalweb.model.WeddingPlan;
import com.kdm360.bridalweb.model.WeddingType;
import com.kdm360.bridalweb.service.BudgetBreakdownService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.WeddingGalleryService;
import com.kdm360.bridalweb.service.WeddingPlanService;
import com.kdm360.bridalweb.service.WeddingService;
import com.kdm360.bridalweb.service.WeddingTypeService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/weddings")
public class WeddingController {

    @Autowired
    private WeddingService weddingService;

    @Autowired
    private WeddingTypeService weddingTypeService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WeddingGalleryService weddingGalleryService;
    
    @Autowired
    private WeddingPlanService weddingPlanService;
    
    @Autowired
    private BudgetBreakdownService budgetBreakdownService;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${file.image.location}")
    private String imageLocation;
    
    @GetMapping
    public String listWeddings(Model model) {
        List<Wedding> weddings = weddingService.get();
        List<WeddingDto> weddingList = new ArrayList<WeddingDto>();
        
        for(Wedding w : weddings) {
        	WeddingDto wd = new WeddingDto();
        	wd.setWeddingId(w.getWeddingId());
        	wd.setName(w.getName());
        	wd.setLocation(w.getLocation());
        	wd.setDate(w.getDate());
        	wd.setBudget(w.getBudget());
        	wd.setWeddingTypeId(w.getWeddingTypeId().getTypeName());
        	wd.setCreatedBy(w.getCreatedBy());
        	wd.setCreatedDate(w.getCreatedDate());
    		if(!weddingGalleryService.getByWeddingId(w).isEmpty()) {
        		wd.setImage(weddingGalleryService.getByWeddingId(w).get(0).getImage());
        	}else {
        		wd.setImage("/images/null.jpg");
        	}
        		
        	
        	weddingList.add(wd);
        }
        model.addAttribute("weddingList", weddingList);
        return "admin/wedding-list";
    }
    
    @GetMapping("/view")
    public String weddings(Model model, Authentication authentication) {
        List<Wedding> weddings = weddingService.get();
        List<WeddingDto> weddingList = new ArrayList<WeddingDto>();
        
        for(Wedding w : weddings) {
        	WeddingDto wd = new WeddingDto();
        	wd.setWeddingId(w.getWeddingId());
        	wd.setName(w.getName());
        	wd.setLocation(w.getLocation());
        	wd.setDate(w.getDate());
        	wd.setBudget(w.getBudget());
        	wd.setWeddingTypeId(w.getWeddingTypeId().getTypeName());
        	wd.setCreatedBy(w.getCreatedBy());
        	wd.setCreatedDate(w.getCreatedDate());
    		if(!weddingGalleryService.getByWeddingId(w).isEmpty()) {
        		wd.setImage(weddingGalleryService.getByWeddingId(w).get(0).getImage());
        	}else {
        		wd.setImage("/images/null.jpg");
        	}
        		
        	
        	weddingList.add(wd);
        }
        model.addAttribute("weddingList", weddingList);
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
        if(authentication != null) {
    		User user = userService.findByUsername(authentication.getName()).orElse(null);
    	    
    		if (user.getRole().equals(User.ROLE.CUSTOMER)) {
    			model.addAttribute("wedding", new Wedding());
    			model.addAttribute("weddingTypes", weddingTypeService.get());
    	    }
    	}
        
        return "weddings";
    }
    
    @GetMapping("/view/{id}")
    public String viewWedding(@PathVariable("id") Long id, Model model) {
        Wedding wedding = weddingService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid wedding Id:" + id));
        model.addAttribute("wedding", wedding);
                
        List<WeddingGallery> weddingGallery = weddingGalleryService.getByWeddingId(wedding);
        model.addAttribute("weddingGallery", weddingGallery);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
    	return "wedding";
    }

    @GetMapping("/new")
    public String showNewWeddingForm(Model model, HttpSession session) {
    	System.out.println("New ");
    	User user = (User)session.getAttribute("user");
    	
    	Wedding wedding = new Wedding();
    	wedding.setCreatedBy(user.getUsername());
    	wedding.setCreatedDate(LocalDate.now());    	
        model.addAttribute("wedding", wedding);
        
        model.addAttribute("weddingTypes", weddingTypeService.get());
        
        List<WeddingGallery> images = new ArrayList<WeddingGallery>();
        model.addAttribute("images", images);
        
        return "admin/wedding-form";
    }

    @PostMapping
    public String saveWedding(Wedding wedding, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveWedding " + wedding);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	if((wedding.getWeddingId() != null) && (weddingService.get(wedding.getWeddingId()).isPresent())) {
//    		wedding = weddingService.get(wedding.getWeddingId()).get();
    		if(wedding.getDate() == null) {
    			wedding.setDate(weddingService.get(wedding.getWeddingId()).get().getDate());
    		}
    	}else {
        	wedding.setCreatedDate(null);
        	wedding.setCreatedBy(loggedUser.getUsername());
        }
    	
        if((wedding.getName() == null) || (wedding.getWeddingTypeId() == null)) {
    		model.addAttribute("error", "Required fields are missing.");
       		model.addAttribute("wedding", wedding);

    		return "admin/wedding-form"; 
    		
    	}
        
        weddingService.save(wedding);
        
        return "redirect:/weddings"; 
    }
    
    @PostMapping("/view")
    public String saveWedding(CustomerWeddingDto weddingDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveWedding " + weddingDto);

    	User loggedUser = (User)session.getAttribute("user");
    	Wedding wedding;
    	
    	if((weddingDto.getWeddingId() != null) && (weddingService.get(weddingDto.getWeddingId()).isPresent())) {
    		wedding = weddingService.get(weddingDto.getWeddingId()).get();
    		if(weddingDto.getDate() == null) {
    			wedding.setDate(weddingService.get(wedding.getWeddingId()).get().getDate());
    		}
    	}else {
    		wedding = new Wedding();
    		wedding.setDate(weddingDto.getDate());
        	wedding.setCreatedDate(null);
        	wedding.setCreatedBy(loggedUser.getUsername());
        	wedding.setUserId(loggedUser);
        }
    	
    	wedding.setBudget(weddingDto.getBudget());
    	wedding.setLocation(weddingDto.getLocation());
    	wedding.setName(weddingDto.getName());
    	wedding.setWeddingTypeId(weddingTypeService.get(Long.parseLong(weddingDto.getWeddingTypeId())).get());
    	
        if((weddingDto.getName() == null) || (weddingDto.getWeddingTypeId() == null)) {
    		redirectAttributes.addAttribute("error", "Required fields are missing.");
       		redirectAttributes.addAttribute("wedding", wedding);
       		
       		return "redirect:/dashboard";
    		
    	}
        weddingService.save(wedding);
        
        if ((weddingDto.getImages() != null) && (!weddingDto.getImages().isEmpty())) {
            try {
            	for (MultipartFile image1 : weddingDto.getImages()) {
                    if (!image1.isEmpty()) {

                    	WeddingGallery gallery = new WeddingGallery();
                        gallery.setWeddingId(wedding);
                        weddingGalleryService.save(gallery);
                        
                    	String imagePath = saveFile(image1, wedding.getWeddingId(), "GALLERY"+gallery.getWeddingGalleryId());
                        
                        gallery.setImage(imagePath);
                        weddingGalleryService.save(gallery);
                    }
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        return "redirect:/dashboard";
    }
    
    private String saveFile(MultipartFile file, Wedding wedding, String type) throws IOException {        
        String fileName = "VENDOR" + wedding.getWeddingId() + type.toUpperCase() + ".png";
        String uploadDir = imageLocation + "wedding/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/wedding/" + fileName;
        
    }
    
    @GetMapping("/edit/{id}")
    public String showEditWeddingForm(@PathVariable("id") Long id, Model model) {
        Wedding wedding = weddingService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid wedding Id:" + id));
        model.addAttribute("wedding", wedding);
        
        model.addAttribute("weddingTypes", weddingTypeService.get());
        
        List<WeddingGallery> images = weddingGalleryService.getByWeddingId(wedding);
        model.addAttribute("images", images);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
    	return "admin/wedding-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteWedding(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    	    	
    	weddingGalleryService.deleteByWeddingId(weddingService.get(id).get());
        weddingService.delete(id);
                
        redirectAttributes.addFlashAttribute("error", "Wedding has been deleted with id: " + id);
        User loggedUser = (User)session.getAttribute("user");
        if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
        	return "redirect:/dashboard"; 
        }else {
        	return "redirect:/weddings";
        }  
    }
    
    /*******************/
    
    @GetMapping("/types")
    public String listWeddingTypes(Model model) {
        List<WeddingType> weddingTypes = weddingTypeService.get();
        model.addAttribute("weddingTypes", weddingTypes);
        return "admin/wedding-type-list";
    }

    @GetMapping("/types/new")
    public String showNewWeddingType(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	WeddingType type = new WeddingType();
    	type.setActive(true);
        model.addAttribute("type", type);
                
        return "admin/wedding-type-form";
    }

    @PostMapping("/types")
    public String saveWeddingType(WeddingTypeDto typeDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveWeddingType " + typeDto);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	WeddingType type;
    	if((typeDto.getWeddingTypeId() != null) && (weddingTypeService.get(typeDto.getWeddingTypeId()).isPresent())) {
    		type = weddingTypeService.get(typeDto.getWeddingTypeId()).get();
    	}else {
    		type = new WeddingType();
    		type.setActive(true);
        }
    	
    	type.setTypeName(typeDto.getTypeName());
    	type.setDescription(typeDto.getDescription());
    	type.setActive(typeDto.isActive());
    	
        if((type.getTypeName().isEmpty())) {
    		model.addAttribute("error", "Required fields are missing.");
       		model.addAttribute("type", type);

    		return "admin/wedding-type-form"; 
    		
    	}
        
//        weddingTypeService.save(type);
        
        try {
        	weddingTypeService.save(type);

        	if (!typeDto.getImage().isEmpty()) {
                String imagePath = saveFile(typeDto.getImage(), type.getWeddingTypeId(), "TYPE");
                type.setImage(imagePath);
            }
            weddingTypeService.save(type);
            redirectAttributes.addFlashAttribute("message", "Wedding type saved successfully!");

        } catch (IOException e) {
        	e.printStackTrace();
        	redirectAttributes.addFlashAttribute("error", "Error saving wedding type: " + e.getMessage());
        }
        
        weddingTypeService.save(type);
        
        return "redirect:/weddings/types"; 
    }
    
    @GetMapping("/types/edit/{id}")
    public String showEditWeddingType(@PathVariable("id") Long id, Model model) {
        WeddingType type = weddingTypeService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("type", type);
        return "admin/wedding-type-form";
    }

    @GetMapping("/types/delete/{id}")
    public String deleteWeddingType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	try {
    		weddingTypeService.delete(id);
    	}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Cannot delete!! Selected Wedding type already linked with another data. ");
		}    	
        
        
        return "redirect:/weddings/types"; 
    }
    
    /*******************/
    
    @PostMapping("/images")
    public String saveWeddingImage(WeddingGalleryDto image, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveWeddingImage " + image);
    	
    	if (!image.getImage().isEmpty()) {
            String uploadDir = imageLocation + "wedding/";
            Path uploadPath = Paths.get(uploadDir);

            try {
            	if (!Files.exists(uploadPath)) {
            		Files.createDirectories(uploadPath);
            	}
            	for (MultipartFile image1 : image.getImage()) {
                    if (!image1.isEmpty()) {
                        String fileName = "WEDDINGGALLERY" + image.getWeddingId() + "T" + System.currentTimeMillis() + ".png";
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(image1.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                        WeddingGallery gallery = new WeddingGallery();
                        gallery.setImage("/wedding/" + fileName);
                        gallery.setWeddingId(weddingService.get(image.getWeddingId()).get());
                        gallery.setAlternativeText(image.getWeddingId().toString());
                        weddingGalleryService.save(gallery);
                    }
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return "redirect:/weddings/edit/" + image.getWeddingId(); 
    }
    
    @GetMapping("/images/delete/{id}")
    public String deleteWeddingImage(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    	    	
    	Wedding wedding = weddingGalleryService.get(id).get().getWeddingId();
    	weddingGalleryService.delete(id);
        
        User loggedUser = (User)session.getAttribute("user");
        if(loggedUser.getRole().equals(User.ROLE.CUSTOMER)) {
        	return "redirect:/weddings/view/" + wedding.getWeddingId();
        }else {
        	return "redirect:/weddings/edit/" + wedding.getWeddingId();
        }
         
    }
    
    @GetMapping("/weddingPlan/view/{id}")
    public String viewWeddingPlan(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("viewWeddingPlan " + id);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	WeddingPlan weddingPlan = weddingPlanService.get(id).get();
    	model.addAttribute("weddingPlan", weddingPlan);
    	model.addAttribute("weddingTypes", weddingTypeService.get(true));
    	
    	model.addAttribute("totalPercentage", budgetBreakdownService.calculateTotalPercentageByPlanId(weddingPlan.getPlanId()));
    	model.addAttribute("totalBudget", budgetBreakdownService.calculateTotalAmountByPlanId(weddingPlan.getPlanId()));
    	
    	Double remainBudget = (budgetBreakdownService.calculateTotalAmountByPlanId(weddingPlan.getPlanId()) * (100 - budgetBreakdownService.calculateTotalPercentageByPlanId(weddingPlan.getPlanId()))) / 100;
    	model.addAttribute("remainBudget", remainBudget);
    	
    	List<BudgetBreakdown> breakdownList = budgetBreakdownService.findByPlanId(weddingPlan);
    	model.addAttribute("breakdownList", breakdownList);
    	
    	List<ServiceCategory> remainServiceCategoryList = serviceCategoryService.get(true);
    	List<ServiceCategory> tempList = budgetBreakdownService.findVendorServicesByPlanId(weddingPlan.getPlanId());
    	remainServiceCategoryList.removeAll(tempList);
    	model.addAttribute("remainServiceCategoryList", remainServiceCategoryList);
    	
    	model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
        return "wedding-plan-view";
    }
    
    @Transactional
    @GetMapping("/saveWeddingPlan")
    public ResponseEntity<Map<String, Object>> saveWeddingPlan(@RequestParam("data") String data, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WeddingPlanRequest request = objectMapper.readValue(data, WeddingPlanRequest.class);
            System.out.println("request : " + request);
            User loggedUser = (User) session.getAttribute("user");

            WeddingPlan weddingPlan;
            if ((request.getWeddingPlan().getPlanId() != null) && (weddingPlanService.get(request.getWeddingPlan().getPlanId()).isPresent())) {
                weddingPlan = weddingPlanService.get(request.getWeddingPlan().getPlanId()).get();
                budgetBreakdownService.deleteByPlanId(weddingPlan);
            } else {
                weddingPlan = new WeddingPlan();
                weddingPlan.setUserId(loggedUser);
            }

            weddingPlan.setWeddingTypeId(weddingTypeService.get(request.getWeddingPlan().getWeddingTypeId()).get());
            weddingPlan.setPlanNickName(request.getWeddingPlan().getPlanNickName());
            weddingPlan.setBudgetType(WeddingPlan.BUDGET.valueOf(request.getWeddingPlan().getBudgetType()));
            weddingPlan.setEstimatedTotal(request.getWeddingPlan().getEstimatedTotal());
            weddingPlan.setLocation(request.getWeddingPlan().getLocation());
            weddingPlan.setWeddingDate(request.getWeddingPlan().getWeddingDate());

            WeddingPlan savedWeddingPlan = weddingPlanService.save(weddingPlan);

            List<BudgetBreakdown> budgetBreakdowns = request.getBudgetBreakdowns().stream()
                .map(breakdownDto -> {
                    BudgetBreakdown breakdown = new BudgetBreakdown();
                    breakdown.setPlanId(savedWeddingPlan);
                    breakdown.setCategoryId(serviceCategoryService.get(breakdownDto.getCategoryId()).get());
                    breakdown.setAmount(breakdownDto.getAmount());
                    breakdown.setPercentage(breakdownDto.getPercentage());
                    return breakdown;
                })
                .collect(Collectors.toList());

            budgetBreakdownService.saveAll(budgetBreakdowns);

            Map<String, Object> planData = new HashMap<>();
            planData.put("planId", savedWeddingPlan.getPlanId().toString());
            planData.put("planNickName", savedWeddingPlan.getPlanNickName());
            planData.put("estimatedTotal", savedWeddingPlan.getEstimatedTotal());
            
            response.put("success", true);
            response.put("message", "Wedding Plan saved successfully");
            response.put("plan", planData); // Send the saved wedding plan data
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error saving Wedding Plan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/weddingPlan/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteWeddingPlan(@PathVariable("id") Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            WeddingPlan weddingPlan = weddingPlanService.get(id).orElse(null);
            if (weddingPlan != null) {
                budgetBreakdownService.deleteByPlanId(weddingPlan);
                weddingPlanService.delete(id);

                response.put("success", true);
                response.put("message", "Wedding Plan deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Wedding Plan not found");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting Wedding Plan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /********************************/
    private String saveFile(MultipartFile file, long Id, String type) throws IOException {        
        String fileName = "WEDDING" + Id + type.toUpperCase() + ".png";
        String uploadDir = imageLocation + "wedding/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/wedding/" + fileName;
    }
}
