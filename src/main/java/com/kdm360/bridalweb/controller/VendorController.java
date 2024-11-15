package com.kdm360.bridalweb.controller;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.ServiceCategoryDto;
import com.kdm360.bridalweb.dto.VendorDto;
import com.kdm360.bridalweb.dto.VendorPackageDto;
import com.kdm360.bridalweb.dto.VendorProfileViewDto;
import com.kdm360.bridalweb.dto.VendorSearch;
import com.kdm360.bridalweb.model.Email;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.Vendor.DISTRICT;
import com.kdm360.bridalweb.model.VendorCategory;
import com.kdm360.bridalweb.model.VendorContact;
import com.kdm360.bridalweb.model.VendorGallery;
import com.kdm360.bridalweb.model.VendorPackage;
import com.kdm360.bridalweb.model.VendorProfileView;
import com.kdm360.bridalweb.model.VendorRating;
import com.kdm360.bridalweb.model.WeddingGallery;
import com.kdm360.bridalweb.service.EmailServiceImpl;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserService;
import com.kdm360.bridalweb.service.VendorCategoryService;
import com.kdm360.bridalweb.service.VendorContactService;
import com.kdm360.bridalweb.service.VendorGalleryService;
import com.kdm360.bridalweb.service.VendorPackageService;
import com.kdm360.bridalweb.service.VendorProfileViewService;
import com.kdm360.bridalweb.service.VendorRatingService;
import com.kdm360.bridalweb.service.VendorService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorPackageService vendorPackageService;
    
    @Autowired
    private VendorCategoryService vendorCategoryService;
    
    @Autowired
    private VendorProfileViewService vendorProfileService;
    
    @Autowired
    private VendorRatingService vendorRatingService;
    
    @Autowired
    private VendorContactService vendorContactService;
    
    @Autowired
    private VendorGalleryService vendorGalleryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailServiceImpl emailService;
    
    @Value("${file.image.location}")
    private String imageLocation;
    
    @GetMapping
    public String listVendors(
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "vendorCats", required = false) String vendorCats,
            @RequestParam(value = "districts", required = false) String districts,
            @RequestParam(value = "averagePrice", required = false) Long averagePrice,
            @RequestParam(value = "startAveragePrice", required = false) Long startAveragePrice,
            @RequestParam(value = "endAveragePrice", required = false) Long endAveragePrice,
            Model model, HttpSession session, Authentication authentication) {
    	
    	
        List<Vendor> vendors = new ArrayList<>();

        if (authentication != null) {
            User loggedUser = (User) session.getAttribute("user");
            if (loggedUser.getRole().equals(User.ROLE.ADMIN)) {
                vendors = vendorService.get();
                model.addAttribute("vendors", vendors);
                return "admin/vendor-list";
            } else if (loggedUser.getRole().equals(User.ROLE.VENDOR)) {
                return "redirect:/vendors/profile";
            } else {
                vendors = vendorService.findByActiveUsers();
            }
        } else {
            vendors = vendorService.findByActiveUsers();
        }

        if (companyName != null && !companyName.isEmpty()) {
		    List<Vendor> tempList = vendorService.findVendorsByCompanyName(companyName);
		    vendors.retainAll(tempList);
		}

		if (vendorCats != null && !vendorCats.isEmpty()) {
		    List<Vendor> tempList = vendorCategoryService.findVendorsByCategoryId(Long.parseLong(vendorCats));
		    vendors.retainAll(tempList);
		}

		if (districts != null && !districts.isEmpty()) {
		    List<Vendor> tempList = vendorService.findVendorsByDistrict(Vendor.DISTRICT.valueOf(districts));
		    vendors.retainAll(tempList);
		}
		
		if ((startAveragePrice != null && startAveragePrice != 0) && (endAveragePrice != null && endAveragePrice != 0)) {
		    List<Vendor> tempList = vendorService.findVendorsByAveragePrice(startAveragePrice, endAveragePrice);
		    vendors.retainAll(tempList);
		}else {
			startAveragePrice = 10000L;
			endAveragePrice = 100000000L;
		}
		
		for(Vendor v: vendors) {
			v.setAvgRate(vendorRatingService.getAverageRatesByVendorId(v.getVendorId()));
		}
		model.addAttribute("vendorList", vendors);
		
		VendorSearch vendorSearch = new VendorSearch(
		    0,
		    companyName,
		    vendorCats,
		    averagePrice,
		    startAveragePrice,
		    endAveragePrice,
		    districts
		);
		model.addAttribute("vendorSearch", vendorSearch);

		model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
		return "vendors";
    }
    
//    @GetMapping("/getVendors")
//    public ResponseEntity<List<Vendor>> getVendors(@RequestParam String district, @RequestParam String category, @RequestParam Long budget) {
//    	System.out.println("/getVendors : " + district  + " | " + category + " | " + budget);
//    	List<Vendor> vendors = vendorCategoryService.findVendorsByCategoryIdAndBudget(Long.parseLong(category), budget);
//    	System.out.println(vendors);
//        return ResponseEntity.ok(vendors);
//    }
    
    @GetMapping("/getVendors")
    public ResponseEntity<List<Vendor>> getVendors(@RequestParam String district, 
                                                   @RequestParam String category, 
                                                   @RequestParam Long budget) {
        System.out.println("/getVendors : " + district + " | " + category + " | " + budget);
        
        Vendor.DISTRICT districtEnum = Vendor.DISTRICT.valueOf(district.toUpperCase().replace(" ", "_"));
        
        List<Vendor> vendors = vendorService.findVendorsByDistrictCategoryAndBudget(districtEnum, 
                                                                                            Long.parseLong(category), 
                                                                                            budget);
        
        if (!districtEnum.equals(Vendor.DISTRICT.ALLINSRILANKA)) {
            List<Vendor> allInSriLankaVendors = vendorService.findVendorsByAllInSriLankaByCategoryAndBudget(Long.parseLong(category), budget);
            for(Vendor i: allInSriLankaVendors) {
            	if(!vendors.contains(i)) {
            		vendors.add(i);
            	}
            }
        }

        System.out.println(vendors);
        return ResponseEntity.ok(vendors);
    }


    
    @PostMapping("/search")
    public String searchVendors(
            VendorSearch searchDto, 
            RedirectAttributes redirectAttributes) {
    	System.out.println("Search Vendor " + searchDto);
        redirectAttributes.addAttribute("companyName", searchDto.getCompanyName());
        redirectAttributes.addAttribute("vendorCats", searchDto.getVendorCats());
        redirectAttributes.addAttribute("districts", searchDto.getDistricts());
        redirectAttributes.addAttribute("averagePrice", searchDto.getAveragePrice());
        redirectAttributes.addAttribute("startAveragePrice", searchDto.getStartAveragePrice());
        redirectAttributes.addAttribute("endAveragePrice", searchDto.getEndAveragePrice());

        return "redirect:/vendors";
    }
    
    @GetMapping("/new")
    public String showNewVendorForm(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	User vendorUser = new User();
    	vendorUser.setCreateBy(user.getUsername());
    	vendorUser.setCreateDate(LocalDate.now());
    	vendorUser.setUpdateBy(user.getUsername());
    	vendorUser.setUpdateDate(LocalDate.now());
    	vendorUser.setRole(User.ROLE.VENDOR);
    	
    	Vendor vendor = new Vendor();
    	vendor.setRegistrationDate(LocalDate.now());
    	vendor.setUserId(vendorUser);
    	
        model.addAttribute("vendor", vendor);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        
        return "admin/vendor-form";
    }

    @PostMapping
    public String saveVendor(VendorDto vendorDto, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	
    	Vendor vendor;
    	User user;
    	
    	if((vendorDto.getVendorId() != null) && (vendorDto.getVendorId() == 0)) {
    		System.out.println("Search Vendor " + vendorDto);
            redirectAttributes.addAttribute("companyName", vendorDto.getCompanyName());
            redirectAttributes.addAttribute("vendorCats", vendorDto.getVendorCats());
            redirectAttributes.addAttribute("districts", vendorDto.getDistricts());
            redirectAttributes.addAttribute("averagePrice", vendorDto.getAveragePrice());
            redirectAttributes.addAttribute("startAveragePrice", vendorDto.getStartAveragePrice());
            redirectAttributes.addAttribute("endAveragePrice", vendorDto.getEndAveragePrice());

            return "redirect:/vendors";
    	}else {
    		System.out.println("saveVendor " + vendorDto);
    		if((vendorDto.getVendorId() != null) && (vendorService.get(vendorDto.getVendorId()).isPresent())) {
        		vendor = vendorService.get(vendorDto.getVendorId()).get();
                user = vendor.getUserId();
        	}else {
            	vendor = new Vendor();
            	vendor.setRegistrationDate(LocalDate.now());
            	
            	user = new User();
            	user.setRole(User.ROLE.VENDOR);
            	if(authentication != null) {
            		User loggedUser = (User)session.getAttribute("user");
            		user.setCreateBy(loggedUser.getUsername());
            	}else {
            		user.setCreateBy(vendorDto.getUsername());
            	}
                user.setCreateDate(LocalDate.now());
                
            }

    		if (isNotEmpty(vendorDto.getCompanyName())) {
    			System.out.println(1);
    		    vendor.setCompanyName(vendorDto.getCompanyName());
    		}
    		if (isNotEmpty(vendorDto.getContactName())) {
    			System.out.println(2);
    		    vendor.setContactName(vendorDto.getContactName());
    		}
    		if (isNotEmpty(vendorDto.getEmail())) {
    			System.out.println(3);
    		    vendor.setEmail(vendorDto.getEmail());
    		}

    		// Update and format mobile numbers and other fields
    		String[] mobileFields = {vendorDto.getMobile1(), vendorDto.getMobile2(), vendorDto.getMobile3()};
    		for (int i = 0; i < mobileFields.length; i++) {
    		    if (isNotEmpty(mobileFields[i])) {
    		        String mobile = mobileFields[i];
    		        if (mobile.startsWith("0")) {
    		            mobile = mobile.replaceFirst("0", "+94");
    		        }
    		        switch (i) {
    		            case 0:
    		                vendor.setMobile1(mobile);
    		                break;
    		            case 1:
    		                vendor.setMobile2(mobile);
    		                break;
    		            case 2:
    		                vendor.setMobile3(mobile);
    		                break;
    		        }
    		    }
    		}

    		if (isNotEmpty(vendorDto.getWhatsapp())) {
    		    String whatsapp = vendorDto.getWhatsapp();
    		    if (whatsapp.startsWith("0")) {
    		        whatsapp = whatsapp.replaceFirst("0", "+94");
    		    }
    		    vendor.setWhatsapp(whatsapp);
    		}

    		if (isNotEmpty(vendorDto.getAddress())) {
    			System.out.println(4);
    		    vendor.setAddress(vendorDto.getAddress());
    		}
    		if (isNotEmpty(vendorDto.getBranches())) {
    			System.out.println(5);
    		    vendor.setBranches(vendorDto.getBranches());
    		}
    		if (isNotEmpty(vendorDto.getWebsite())) {
    			System.out.println(6);
    		    vendor.setWebsite(vendorDto.getWebsite());
    		}
    		if (isNotEmpty(vendorDto.getGoogleMap())) {
    			System.out.println(7);
    		    vendor.setGoogleMap(vendorDto.getGoogleMap());
    		}
    		if (isNotEmpty(vendorDto.getFacebook())) {
    		    vendor.setFacebook(vendorDto.getFacebook());
    		}
    		if (isNotEmpty(vendorDto.getInstagram())) {
    		    vendor.setInstagram(vendorDto.getInstagram());
    		}
    		if (isNotEmpty(vendorDto.getYoutube())) {
    		    vendor.setYoutube(vendorDto.getYoutube());
    		}
    		if (isNotEmpty(vendorDto.getTiktok())) {
    		    vendor.setTiktok(vendorDto.getTiktok());
    		}
    		if (isNotEmpty(vendorDto.getTwitter())) {
    		    vendor.setTwitter(vendorDto.getTwitter());
    		}
    		if (isNotEmpty(vendorDto.getLinkedin())) {
    		    vendor.setLinkedin(vendorDto.getLinkedin());
    		}
    		if (isNotEmpty(vendorDto.getDescription())) {
    		    vendor.setDescription(vendorDto.getDescription());
    		}
    		if (vendorDto.getDiscount() != null) {
    		    vendor.setDiscount(vendorDto.getDiscount());
    		}
    		if (vendorDto.getAdvance() != null) {
    		    vendor.setAdvance(vendorDto.getAdvance());
    		}
    		if (vendorDto.getAveragePrice() != null) {
    		    vendor.setAveragePrice(vendorDto.getAveragePrice());
    		}
    		if (vendorDto.getOpenDays() != null) {
    		    vendor.setOpenDays(vendorDto.getOpenDays());
    		}
    		if (vendorDto.getOpenHours() != null) {
    		    vendor.setOpenHours(vendorDto.getOpenHours());
    		}

    		// Handle districts
    		Set<DISTRICT> districts = new HashSet<>();
    		if (vendorDto.getDistricts() != null && !vendorDto.getDistricts().isEmpty()) {
    		    districts = vendorDto.getDistricts().stream()
    		        .map(districtStr -> DISTRICT.valueOf(districtStr))
    		        .collect(Collectors.toSet());
    		} else {
    		    districts.add(DISTRICT.ALLINSRILANKA);
    		}
    		vendor.setDistricts(districts);

    		// Update user fields
    		if (isNotEmpty(vendorDto.getContactName())) {
    		    user.setFirstName(vendorDto.getContactName());
    		}
    		if (isNotEmpty(vendorDto.getEmail())) {
    		    user.setEmail(vendorDto.getEmail());
    		}
    		if (isNotEmpty(vendorDto.getMobile1())) {
    		    user.setMobile(vendorDto.getMobile1());
    		}
    		if (isNotEmpty(vendorDto.getUsername())) {
    		    user.setUsername(vendorDto.getUsername());
    		}
    		if (isNotEmpty(vendorDto.getPassword())) {
    		    user.setPassword(passwordEncoder.encode(vendorDto.getPassword()));
    		}

    		// Set user status
    		if (isNotEmpty(vendorDto.getStatus())) {
    		    user.setStatus(User.STATUS.valueOf(vendorDto.getStatus()));
    		} else {
    		    user.setStatus(User.STATUS.ACTIVE);
    		}

    		// Set update information
    		if (authentication != null) {
    		    User loggedUser = (User) session.getAttribute("user");
    		    user.setUpdateBy(loggedUser.getUsername());
    		} else {
    		    user.setUpdateBy(vendorDto.getUsername());
    		}
    		user.setUpdateDate(LocalDate.now());

            if((vendorDto.getCompanyName().isEmpty()) || (vendorDto.getEmail().isEmpty()) || (vendorDto.getMobile1().isEmpty()) || (vendorDto.getDescription().isEmpty())) {
        		model.addAttribute("error", "Required fields are missing.");
        		
        		vendor.setUserId(user);
        		model.addAttribute("vendor", vendor);

        		if(authentication != null) {
        			User loggedUser = (User)session.getAttribute("user");
        			if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        				return "admin/vendor-form"; 
        			}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
        				return "admin/vendor-profile"; 
        			}else {
        				return "vendor-form";
        			}
        			
            	}else {
            		return "vendor-form"; 
            	}
        		
        	}else if((vendorDto.getVendorId() == null) && ((vendorDto.getUsername().isEmpty()))) {
        		model.addAttribute("error", "Username is missing.");
        		
        		vendor.setUserId(user);
        		model.addAttribute("vendor", vendor);

        		if(authentication != null) {
        			User loggedUser = (User)session.getAttribute("user");
        			if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        				return "admin/vendor-form"; 
        			}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
        				return "admin/vendor-profile"; 
        			}else {
        				return "vendor-form";
        			}
            	}else {
            		return "vendor-form"; 
            	}
        		
        	}else if((vendorDto.getVendorId() == null) && ((vendorDto.getPassword().isEmpty()))) {
        		model.addAttribute("error", "Password is missing.");
        		
        		vendor.setUserId(user);
        		model.addAttribute("vendor", vendor);
                
        		if(authentication != null) {
        			User loggedUser = (User)session.getAttribute("user");
        			if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        				return "admin/vendor-form"; 
        			}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
        				return "admin/vendor-profile"; 
        			}else {
        				return "vendor-form";
        			}
            	}else {
            		return "vendor-form"; 
            	}
        		
        	}else if((vendorDto.getVendorId() == null) && ((userService.findByUsername(vendorDto.getUsername()).isPresent()))) {
        		model.addAttribute("error", "Username already exists.");
        		
        		vendor.setUserId(user);
        		model.addAttribute("vendor", vendor);

        		if(authentication != null) {
        			User loggedUser = (User)session.getAttribute("user");
        			if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        				return "admin/vendor-form"; 
        			}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
        				return "admin/vendor-profile"; 
        			}else {
        				return "vendor-form";
        			}
            	}else {
            		return "vendor-form"; 
            	}
        	}
            
            userService.save(user);
            
            vendor.setUserId(user); 
            vendorService.save(vendor);
            
            String[] categories = vendorDto.getVendorCats().split(",");
            for(VendorCategory vc: vendorCategoryService.getByVendor(vendor)) {
            	String categoryId = vc.getCategoryId().getCategoryId().toString();
                boolean contains = Arrays.asList(categories).contains(categoryId);
                if(!contains) {
                	vendorCategoryService.delete(vc.getVendorVendorTypeId());
                }
            }
            
            for (String cat : categories) {
            	if(!cat.isEmpty()) {
            		ServiceCategory category = serviceCategoryService.get(Long.parseLong(cat)).get();
            		if(!vendorCategoryService.getByVendorAndCategory(vendor,category).isPresent()) {
            			VendorCategory vCat = new VendorCategory();
                		vCat.setCategoryId(category);
                		vCat.setVendorId(vendor);
                		vendorCategoryService.save(vCat);
            		}
            		
            	}
            }
            
            try {
                if (!vendorDto.getLogo().isEmpty()) {
                    String logoPath = saveFile(vendorDto.getLogo(), vendor.getVendorId(), "LOGO");
                    vendor.setLogo(logoPath);
                }
                if (!vendorDto.getCoverImage().isEmpty()) {
                    String coverImagePath = saveFile(vendorDto.getCoverImage(), vendor.getVendorId(), "COVER");
                    vendor.setCoverImage(coverImagePath);
                }
                if ((vendorDto.getImages() != null) && (!vendorDto.getImages().isEmpty())) {
                    try {
                    	for (MultipartFile image1 : vendorDto.getImages()) {
                            if (!image1.isEmpty()) {

                            	VendorGallery gallery = new VendorGallery();
                                gallery.setVendorId(vendor);
                                vendorGalleryService.save(gallery);
                                
                            	String imagePath = saveFile(image1, vendor.getVendorId(), "GALLERY"+gallery.getVendorGalleryId());
                                
                                gallery.setImage(imagePath);
                                vendorGalleryService.save(gallery);
                            }
                        }
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
                }
                
                vendorService.save(vendor);	
                redirectAttributes.addFlashAttribute("message", "Vendor saved successfully! Login to manage. ");

            } catch (IOException e) {
            	e.printStackTrace();
            	redirectAttributes.addFlashAttribute("error", "Error saving vendor: " + e.getMessage());
            }

            if(authentication != null) {
            	User loggedUser = (User)session.getAttribute("user");
    			if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
    				return "redirect:/vendors";
    			}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
    				return "redirect:/vendors/profile"; 
    			}else {
    				return "redirect:/vendors";
    			}
        	}else {
        		return "redirect:/new-vendor"; 
        	}
    	}
    }

    @GetMapping("/{id}")
    public String viewVendor(@PathVariable("id") Long id, Model model, HttpSession session, Authentication authentication) {
        Vendor vendor = vendorService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid vendor Id:" + id));
        model.addAttribute("vendor", vendor);
        
        VendorProfileView pofileView;
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        
        if(authentication != null) {
        	User user = (User)session.getAttribute("user");
        	if(user.getRole().equals(User.ROLE.CUSTOMER)) {
        		pofileView = vendorProfileService.getByVendorIdAndUserIdAndViewDate(vendor, user, today).orElse(null);
                
            	if(pofileView == null) {
            		pofileView = new VendorProfileView();
            		pofileView.setVendorId(vendor);
            		pofileView.setUserId(user);
            		pofileView.setViewDate(today);
            		pofileView.setViewCount(1);
            	}else {
            		pofileView.setViewCount(pofileView.getViewCount() + 1);
            	}
            	vendorProfileService.save(pofileView);        
        	}
        }else {
        	pofileView = vendorProfileService.getByVendorIdAndUserIdAndViewDate(vendor, null, today).orElse(null);
            
        	if(pofileView == null) {
        		pofileView = new VendorProfileView();
        		pofileView.setVendorId(vendor);
        		pofileView.setUserId(null);
        		pofileView.setViewDate(today);
        		pofileView.setViewCount(1);
        	}else {
        		pofileView.setViewCount(pofileView.getViewCount() + 1);
        	}
        	vendorProfileService.save(pofileView);
        }        
        
        
        Set<String> vendorDistricts = vendor.getDistricts().stream()
                .map(DISTRICT::name)
                .collect(Collectors.toSet());
        model.addAttribute("vendorDistricts", vendorDistricts);
        
        model.addAttribute("vendorCats", vendorCategoryService.getByVendor(vendor));
        
        model.addAttribute("vendorGallery", vendorGalleryService.getByVendorId(vendor));
        
        model.addAttribute("vendorPackages", vendorPackageService.getByVendorId(vendor));
        
        model.addAttribute("message", new VendorContact());
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
    	model.addAttribute("avgRatings", vendorRatingService.getAverageRatesByVendorId(id));
    	
        return "vendor";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditVendorForm(@PathVariable("id") Long id, Model model) {
        Vendor vendor = vendorService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid vendor Id:" + id));
        model.addAttribute("vendor", vendor);
        
        Set<String> vendorDistricts = vendor.getDistricts().stream()
                .map(DISTRICT::name)
                .collect(Collectors.toSet());
        model.addAttribute("vendorDistricts", vendorDistricts);
        model.addAttribute("images", vendorGalleryService.getByVendorId(vendor));
        model.addAttribute("categories", vendorCategoryService.getByVendor(vendor));
        
        String selectedCats = new String();
        for(VendorCategory vc: vendorCategoryService.getByVendor(vendor)) {
        	selectedCats += (vc.getCategoryId().getCategoryId()).toString()+",";
        }
        model.addAttribute("selectedCats", selectedCats);

        return "admin/vendor-form";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteVendor(@PathVariable("id") Long id) {
    	
    	long userId = vendorService.get(id).get().getUserId().getUserId();
    	
    	Vendor vendor = vendorService.get(id).get();
    	
    	try {
    		vendorCategoryService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorCategoryService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorGalleryService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorGalleryService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorContactService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorContactService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorRatingService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorRatingService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorPackageService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorPackageService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorProfileService.deleteByVendorId(vendor);
    	}catch (Exception e) {
    		System.out.println("vendorProfileService.deleteByVendorId(id);");
    		e.printStackTrace();
		}
    	
    	try {
    		vendorService.delete(id);
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	
        
        userService.delete(userId);
        
        return "redirect:/vendors";
    }
    
    /*********************************/
    
    @GetMapping("/packages/{vendorId}")
    public String listVendorPackages(@PathVariable("vendorId") Long vendorId, Model model) {
        List<VendorPackage> packages = vendorPackageService.getByVendorId(vendorService.get(vendorId).get());
        model.addAttribute("packages", packages);
        
        model.addAttribute("vendor", vendorService.get(vendorId).get());
        
        return "admin/vendor-package-list";
    }

    @GetMapping("/packages/view/{id}")
    public String vendorPackage(@PathVariable("id") Long id, Model model) {
    	System.out.println("vendorPackage");
    	VendorPackage vendorPackage = vendorPackageService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid package Id:" + id));
        model.addAttribute("package", vendorPackage);
        
        List<String> featuresList = vendorPackage.getFeatures() != null
                ? Arrays.asList(vendorPackage.getFeatures().split(","))
                : Arrays.asList();
        model.addAttribute("featuresList", featuresList);
        System.out.println("featuresList : " + featuresList);

        model.addAttribute("vendor", vendorPackage.getVendorId());
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
      
    	return "vendor-package";
    }
    
    @GetMapping("/packages/new/{vendorId}")
    public String showNewVendorPackageForm(@PathVariable("vendorId") Long vendorId, Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	VendorPackageDto vendorPackage = new VendorPackageDto();
    	vendorPackage.setCreationDate(LocalDate.now());
    	vendorPackage.setVendorId(vendorId);
    	model.addAttribute("vendorPackage", vendorPackage);
    	
    	Vendor vendor = vendorService.get(vendorId).get();
        model.addAttribute("vendor", vendor);
        
        return "admin/vendor-package-form";
    }

    @PostMapping("/packages")
    public String saveVendorPackage(VendorPackageDto vendorPackageDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("saveVendorPackage " + vendorPackageDto);

    	User loggedUser = (User)session.getAttribute("user");
    	
    	VendorPackage vendorPackage;
    	
    	if((vendorPackageDto.getPackageId() != null) && (vendorPackageService.get(vendorPackageDto.getPackageId()).isPresent())) {
    		vendorPackage = vendorPackageService.get(vendorPackageDto.getPackageId()).get();
    		vendorPackage.setPackageName(vendorPackageDto.getPackageName());
    		vendorPackage.setDescription(vendorPackageDto.getDescription());
    		vendorPackage.setFeatures(vendorPackageDto.getFeatures());
    		vendorPackage.setPrice(vendorPackageDto.getPrice());
    		vendorPackage.setActive(vendorPackageDto.isActive());
    	}else {
    		vendorPackage = new VendorPackage();
    		vendorPackage.setPackageName(vendorPackageDto.getPackageName());
    		vendorPackage.setCreationDate(LocalDate.now());
    		vendorPackage.setDescription(vendorPackageDto.getDescription());
    		vendorPackage.setFeatures(vendorPackageDto.getFeatures());
    		vendorPackage.setPrice(vendorPackageDto.getPrice());
    		vendorPackage.setVendorId(vendorService.get(vendorPackageDto.getVendorId()).get());
    		vendorPackage.setActive(vendorPackageDto.isActive());
        }
 
        
        if((vendorPackage.getPackageName() == null) || (vendorPackage.getPrice() == null)) {
    		model.addAttribute("error", "Required fields are missing.");
    		
    		model.addAttribute("vendorPackage", vendorPackage);
    		model.addAttribute("vendor", vendorService.get(vendorPackageDto.getVendorId()).get());
    		
    		return "admin/vendor-package-form"; 
    	}
        
        vendorPackageService.save(vendorPackage);
        
        try {
            if (!vendorPackageDto.getPackageImage().isEmpty()) {
                String logoPath = saveFile(vendorPackageDto.getPackageImage(), vendorPackage.getPackageId(), "PACKAGE");
                vendorPackage.setPackageImage(logoPath);
            }
            
            vendorPackageService.save(vendorPackage);
            redirectAttributes.addFlashAttribute("message", "Package saved successfully!");

        } catch (IOException e) {
        	e.printStackTrace();
        	redirectAttributes.addFlashAttribute("error", "Error saving package: " + e.getMessage());
        }

        if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        	return "redirect:/vendors/packages/"+vendorPackageDto.getVendorId(); 
		}else {
			return "redirect:/vendors/profile/packages";
		}
    }

    @GetMapping("/packages/edit/{id}")
    public String showEditVendorPackage(@PathVariable("id") Long id, Model model) {
    	VendorPackage vendorPackage = vendorPackageService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid package Id:" + id));
        model.addAttribute("vendorPackage", vendorPackage);
        
        model.addAttribute("vendor", vendorPackage.getVendorId());
        
      return "admin/vendor-package-form";
    }

    @GetMapping("/packages/delete/{id}")
    public String deleteVendorPackage(@PathVariable("id") Long id, HttpSession session) {
    	
    	User loggedUser = (User)session.getAttribute("user");
    	Vendor vendor = vendorPackageService.get(id).get().getVendorId();
    	
        vendorPackageService.delete(id);
        
        if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
        	return "redirect:/vendors/packages/"+ vendor.getVendorId(); 
		}else {
			return "redirect:/vendors/profile/packages";
		}
    }
    
    /*********************************/
    @PostMapping("/gallery/save")
    public String saveImages(VendorDto vendorDto, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("saveGallery " + vendorDto);
		
    	User loggedUser = (User)session.getAttribute("user");
		
    	if((vendorDto.getVendorId() == null) || (!(vendorService.get(vendorDto.getVendorId()).isPresent()))) {
    		redirectAttributes.addFlashAttribute("message", "Vendor is not exist in the system!");
    		if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
    			return "redirect:/vendors";
    		}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
    			return "redirect:/vendors/profile/gallery"; 
    		}else {
    			return "redirect:/vendors";
    		}
    		
    	}
		
		Vendor vendor = vendorService.get(vendorDto.getVendorId()).get();
		
        if ((vendorDto.getImages() != null) && (!vendorDto.getImages().isEmpty())) {
		    try {
		    	for (MultipartFile image1 : vendorDto.getImages()) {
		            if (!image1.isEmpty()) {

		            	VendorGallery gallery = new VendorGallery();
		                gallery.setVendorId(vendor);
		                vendorGalleryService.save(gallery);
		                
		            	String imagePath = saveFile(image1, vendor.getVendorId(), "GALLERY"+gallery.getVendorGalleryId());
		                
		                gallery.setImage(imagePath);
		                vendorGalleryService.save(gallery);
		            }
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "Vendor images saved successfully!");

		if(loggedUser.getRole().equals(User.ROLE.ADMIN)) {
			return "redirect:/vendors/edit/" + vendor.getVendorId();
		}else if(loggedUser.getRole().equals(User.ROLE.VENDOR)) {
			return "redirect:/vendors/profile/gallery"; 
		}else {
			return "redirect:/vendors";
		}
    }

    @GetMapping("/gallery/delete/{id}")
    public String deleteVendorImage(@PathVariable("id") Long id) {
    	vendorGalleryService.delete(id);
        return "redirect:/vendors/profile/gallery";
    }
    /*************************************/
    @GetMapping("/categories/{cat}")
    public String listCategories(@PathVariable("cat") Long cat, Model model) {
        System.out.println("listCategories : " + cat);
        
        List<Vendor> vendorList = vendorCategoryService.findVendorsByCategoryId(cat);
        model.addAttribute("vendorList", vendorList);

        VendorSearch vendorSearch = new VendorSearch(
    		    0,
    		    null,
    		    String.valueOf(cat),
    		    0L,
    		    0L,
    		    0L,
    		    null
    		);
        model.addAttribute("vendorSearch", vendorSearch);
        
        model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
        return "vendors";
    }
    
    /********************************/
    
    @PostMapping("/message/send")
    public String saveVendorMessage(VendorContact message, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("saveVendorMessage " + message);

    	if(authentication != null) {
    		User loggedUser = (User)session.getAttribute("user");
    	}
    	    	
    	if((message.getContactId() != null) && (vendorContactService.get(message.getContactId()).isPresent())) {

    	}else {
    		message.setContactDate(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    		 message.setContactTime(Integer.parseInt(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))));
//    		message.setVendorId(vendorService.get(message.getVendorId()));
    		
        	if(authentication != null) {
        		User loggedUser = (User)session.getAttribute("user");
        		message.setUserId(loggedUser);
        	}else {
        		message.setUserId(null);
        	}            
        }

        if((message.getMessage().isEmpty())) {
    		redirectAttributes.addFlashAttribute("error", "Message cannot be empty.");
    		redirectAttributes.addFlashAttribute("message", message);

    		return "redirect:/vendors/"+message.getVendorId().getVendorId(); 
    	}
        
        vendorContactService.save(message);
        
        Email email = new Email(
        		message.getVendorId().getEmail(),
				("Inquiry from customer"),
				("Dear " + message.getVendorId().getCompanyName() + ", \n\n " + message.getMessage()));
		emailService.sendMail(email);
		
		redirectAttributes.addFlashAttribute("emailSent", "Your inquiry has been successfully sent to the vendor. Thank you!");
        return "redirect:/vendors/"+message.getVendorId().getVendorId(); 
    }
    
    @PostMapping("/package/message/send")
    public String saveVendorMessage2(VendorContact message, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("saveVendorMessage " + message);

    	if(authentication != null) {
    		User loggedUser = (User)session.getAttribute("user");
    	}
    	    	
    	if((message.getContactId() != null) && (vendorContactService.get(message.getContactId()).isPresent())) {

    	}else {
    		message.setContactDate(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    		 message.setContactTime(Integer.parseInt(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))));
    		
        	if(authentication != null) {
        		User loggedUser = (User)session.getAttribute("user");
        		message.setUserId(loggedUser);
        	}else {
        		message.setUserId(null);
        	}            
        }

        if((message.getMessage().isEmpty())) {
    		redirectAttributes.addFlashAttribute("error", "Message cannot be empty.");
    		redirectAttributes.addFlashAttribute("message", message);

    		return "redirect:/vendors/packages/view/"+message.getPackageId(); 
    	}
        
        vendorContactService.save(message);
        
        Email email = new Email(
        		message.getVendorId().getEmail(),
				("Inquiry from customer"),
				("Dear " + message.getVendorId().getCompanyName() + ", \n\n " + message.getMessage()));
		emailService.sendMail(email);
		
        redirectAttributes.addFlashAttribute("emailSent", "Your inquiry has been successfully sent to the vendor. Thank you!");
        return "redirect:/vendors/packages/view/"+message.getPackageId(); 
    }
    
    /********************************/
    
    @GetMapping("/profile")
    public String showEditVendorForm(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		Set<String> vendorDistricts = vendor.getDistricts().stream()
                    .map(DISTRICT::name)
                    .collect(Collectors.toSet());
            model.addAttribute("vendorDistricts", vendorDistricts);
            
    		model.addAttribute("categories", vendorCategoryService.getByVendor(vendor));
    		
    		String selectedCats = new String();
            for(VendorCategory vc: vendorCategoryService.getByVendor(vendor)) {
            	selectedCats += (vc.getCategoryId().getCategoryId()).toString()+",";
            }
            model.addAttribute("selectedCats", selectedCats);
            
            return "admin/vendor-profile";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Cannot find vendor profile!");
    		return "redirect:/index";
    	}
    	
    }
    
    @GetMapping("/profile/views")
    public String profileViews(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		VendorProfileViewDto vendorViewDto = new VendorProfileViewDto();
    		
    		Integer viewCount = vendorProfileService.getTotalViewCountByVendorId(vendor.getVendorId());
    		vendorViewDto.setViewCount(viewCount != null ? viewCount : 0);
    		model.addAttribute("vendorViewDto", vendorViewDto);
    		
    		List<VendorProfileView> profileViews = vendorProfileService.findByVendorId(vendor);
            model.addAttribute("profileViews", profileViews);
            
            return "admin/vendor-profile-view";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Error while fetching profile views!");
    		return "redirect:/index";
    	}
    	
    }
    
    @GetMapping("/{vendorId}/profile/views/searchVendorProfileViews")
	public ResponseEntity<List<VendorProfileView>> searchVendorProfileViews(
			@PathVariable("vendorId") Long vendorId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "viewDate", required = false) Integer viewDate){
    	System.out.println("searchVendorProfileViews : vendorId= " + vendorId + " ,userId= " + userId + " ,viewDate= " + viewDate);
	    List<VendorProfileView> profileViewList = vendorProfileService.getFilteredViews(vendorId, userId, viewDate);
	    return new ResponseEntity<>(profileViewList, HttpStatus.OK);
	}
    
    @PostMapping("/profile/ratings")
    public String saveProfileRatings(VendorRating rateData,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("Save Ratig : " + rateData);
    	
    	int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    	User user = (User)session.getAttribute("user");
    	
    	VendorRating rate = new VendorRating();
    	rate.setRatingDate(today);
    	rate.setRating(rateData.getRating());
    	rate.setReview(rateData.getReview());
    	rate.setUserId(user);
    	rate.setVendorId(rateData.getVendorId());
    	vendorRatingService.save(rate);
    	
    	redirectAttributes.addFlashAttribute("rated", "Thank you! Your rating has been saved!");
    	return "redirect:/vendors/" + rateData.getVendorId().getVendorId();
    }
    
    @GetMapping("/profile/ratings")
    public String profileRatings(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		List<VendorRating> profileRatings = vendorRatingService.findByVendorId(vendor);
            model.addAttribute("profileRatings", profileRatings);
            
            VendorProfileViewDto vendorViewDto = new VendorProfileViewDto();
    		vendorViewDto.setRatingCount(vendorRatingService.getTotalViewCountByVendorId(vendor.getVendorId()));
    		model.addAttribute("vendorViewDto", vendorViewDto);
    		
            return "admin/vendor-profile-rating";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Error while fetching profile ratings!");
    		return "redirect:/index";
    	}
    	
    }
    
    @GetMapping("/{vendorId}/profile/ratings/searchVendorRatings")
	public ResponseEntity<List<VendorRating>> searchVendorRatings(
			@PathVariable("vendorId") Long vendorId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "ratingDate", required = false) Integer ratingDate){
    	System.out.println("searchVendorRatings : vendorId= " + vendorId + " ,userId= " + userId + " ,rating= " + rating + ",ratingDate= " + ratingDate);
	    List<VendorRating> rateList = vendorRatingService.getFilteredViews(vendorId, userId, rating, ratingDate);
	    return new ResponseEntity<>(rateList, HttpStatus.OK);
	}
    
    @GetMapping("/profile/messages")
    public String profileMessages(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		List<VendorContact> profileContacts = vendorContactService.findByVendorId(vendor);
            model.addAttribute("profileContacts", profileContacts);
            
            return "admin/vendor-profile-message";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Error while fetching messages!");
    		return "redirect:/index";
    	}
    	
    }
    
    @GetMapping("/{vendorId}/profile/messages/searchVendorMessages")
	public ResponseEntity<List<VendorContact>> searchVendorMessages(
			@PathVariable("vendorId") Long vendorId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "contactDate", required = false) Integer contactDate){
    	System.out.println("searchVendorMessages : vendorId= " + vendorId + " ,userId= " + userId + ",contactDate= " + contactDate);
	    List<VendorContact> profileContacts = vendorContactService.getFilteredViews(vendorId, userId, contactDate);
	    return new ResponseEntity<>(profileContacts, HttpStatus.OK);
	}
    
    @GetMapping("/profile/messages/delete/{id}")
    public String deleteMessage(@PathVariable("id") Long id) {
    	vendorContactService.delete(id);
        return "redirect:/vendors/profile/messages";
    }
    
    @GetMapping("/profile/packages")
    public String profilePackages(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		List<VendorPackage> packages = vendorPackageService.getByVendorId(vendorService.get(vendor.getVendorId()).get());
            model.addAttribute("packages", packages);
            
            return "admin/vendor-profile-package";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Error while fetching packages!");
    		return "redirect:/index";
    	}
    }
    
    @GetMapping("/profile/gallery")
    public String profileGallery(Model model,  HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	if(authentication != null) {
    		User user = (User)session.getAttribute("user");
    		Vendor vendor = vendorService.findByUserId(user);
    		if(vendor == null) {
    			redirectAttributes.addFlashAttribute("error", "Cannot ind vendor profile!");
        		return "redirect:/dashboard";
    		}
    		model.addAttribute("vendor", vendor);
    		
    		List<VendorGallery> gallery = vendorGalleryService.getByVendorId(vendorService.get(vendor.getVendorId()).get());
            model.addAttribute("gallery", gallery);
            
            return "admin/vendor-profile-gallery";
    	}else {
    		redirectAttributes.addFlashAttribute("error", "Error while fetching images!");
    		return "redirect:/index";
    	}
    	
    }
    
    /*********************************/
    @GetMapping("/categories")
    public String listCategories(Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        List<ServiceCategory> categories = serviceCategoryService.get();
        model.addAttribute("categories", categories);
        
        return "admin/service-category-list";        
    }

    @GetMapping("/categories/new")
    public String showCategoriesForm(Model model, HttpSession session) {
    	
    	User user = (User)session.getAttribute("user");
    	
    	ServiceCategory category = new ServiceCategory();
    	model.addAttribute("category", category);
        
        return "admin/service-category-form";
    }

    @PostMapping("/categories")
    public String saveCategories(ServiceCategoryDto categoryDto, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("saveCategories " + categoryDto);

    	ServiceCategory category;
    	
    	if((categoryDto.getCategoryId() != null) && (serviceCategoryService.get(categoryDto.getCategoryId()).isPresent())) {
    		category = serviceCategoryService.get(categoryDto.getCategoryId()).get();
    	}else {
    		category = new ServiceCategory();
    		category.setActive(true);
        }
    	
    	category.setActive(categoryDto.isActive());
    	category.setTypeName(categoryDto.getTypeName());
		category.setDescription(categoryDto.getDescription());

    	if((categoryDto.getTypeName().isEmpty())) {
    		model.addAttribute("error", "Required fields are missing.");
    		
    		model.addAttribute("category", category);

    		return "admin/service-category-form";
    		
    	}
        
    	serviceCategoryService.save(category);
                
        try {
            if (!categoryDto.getImage().isEmpty()) {
                String imagePath = saveFile(categoryDto.getImage(), category.getCategoryId(), "CAT");
                category.setImage(imagePath);
            }
            serviceCategoryService.save(category);
            redirectAttributes.addFlashAttribute("message", "Category saved successfully!");

        } catch (IOException e) {
        	e.printStackTrace();
        	redirectAttributes.addFlashAttribute("error", "Error saving category: " + e.getMessage());
        }

        return "redirect:/vendors/categories";
        
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategories(@PathVariable("id") Long id, Model model, HttpSession session, Authentication authentication) {
    	ServiceCategory category = serviceCategoryService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Category Id:" + id));
        model.addAttribute("category", category);
        
        return "admin/service-category-form";
    }
    
    
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
    	serviceCategoryService.delete(id);
        return "redirect:/vendors/categories";
    }
    
    /********************************/
    private String saveFile(MultipartFile file, long Id, String type) throws IOException {        
        String fileName = "VENDOR" + Id + type.toUpperCase() + ".png";
        String uploadDir = imageLocation + "vendor/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/vendor/" + fileName;
        
    }
    
	private boolean isNotEmpty(String str) {
	    return str != null && !str.trim().isEmpty();
	}
}
