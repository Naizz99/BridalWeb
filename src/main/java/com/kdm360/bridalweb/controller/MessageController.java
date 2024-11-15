package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.model.Email;
import com.kdm360.bridalweb.model.Message;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.service.EmailServiceImpl;
import com.kdm360.bridalweb.service.MessageService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.SettingsService;
import com.kdm360.bridalweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;
    
    @Autowired
    private ServiceCategoryService serviceCategoryService;
    
    @Autowired
    private SettingsService settingsService;
    
    @GetMapping
    public String listMessages(Model model) {
        List<Message> messages = messageService.get();
        System.out.println(messages);
        model.addAttribute("messages", messages);
        return "admin/messages";
    }

//    @GetMapping("/new")
//    public String showNewMessageForm(Model model, HttpSession session) {
//    	
//    	User user = (User)session.getAttribute("user");
//    	
//    	User vendorUser = new User();
//    	vendorUser.setCreateBy(user.getUsername());
//    	vendorUser.setCreateDate(LocalDate.now());
//    	vendorUser.setUpdateBy(user.getUsername());
//    	vendorUser.setUpdateDate(LocalDate.now());
//    	
//    	Message message = new Message();
//    	message.setRegistrationDate(LocalDate.now());
//    	message.setUserId(vendorUser);
//    	
//        model.addAttribute("message", message);
//        
//        return "admin/message-form";
//    }
//
    @PostMapping("/reply")
    public String replyMessage(Message message, Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
    	System.out.println("replyMessage " + message);
    	
    	Message mes = messageService.get(message.getContactId()).get();
    	if(!mes.isRead() && message.getReply()!=null && message.getReply()!="") {
    		mes.setReply(message.getReply());
        	mes.setRead(true);
        	mes.setRepliedDate(LocalDateTime.now());
        	messageService.save(mes);
        	
        	Email email = new Email(
        			mes.getEmail(),
    				("Re: " + ((mes.getSubject() !=null && mes.getSubject()!="")?mes.getSubject() : "Reply For Your Message")),
    				("Dear " + mes.getName() + ", \n\n " + mes.getReply()));
    		emailService.sendMail(email);
    	}
    	
    	return "redirect:/messages"; 
        
    }

    
//    @GetMapping("/edit/{id}")
//    public String showEditVendorForm(@PathVariable("id") Long id, Model model) {
//        Message message = vendorService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
//        model.addAttribute("message", message);
//        model.addAttribute("images", vendorGalleryService.getByVendorId(message));
//        model.addAttribute("categories", vendorCategoryService.getByVendor(message));
//        
//        String selectedCats = new String();
//        for(VendorCategory vc: vendorCategoryService.getByVendor(message)) {
//        	selectedCats += (vc.getCategoryId().getCategoryId()).toString()+",";
//        }
//        model.addAttribute("selectedCats", selectedCats);
//        System.out.println(vendorCategoryService.getByVendor(message));
//      return "admin/message-form";
//    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteMessage(@PathVariable("id") Long id) {
    	messageService.delete(id);
        return "redirect:/messages";
    }
    
    @GetMapping("/contact-us")
    public String message(Model model) {
    	
    	Message message = new Message();
    	model.addAttribute("message", message);
    	
    	model.addAttribute("serviceCategoryList", serviceCategoryService.get(true));
        Optional<Settings> settings = settingsService.get(1);
    	model.addAttribute("settings", settings.get());
    	
    	return "contact-us";
    }
    
    @PostMapping("/send")
    public String sendMessage(Message message) {
    	System.out.println("message " + message);
    	
    	message.setSubmittedDate(LocalDateTime.now());
    	messageService.save(message);
    	
    	return "redirect:/messages/contact-us";
    }
        
}
