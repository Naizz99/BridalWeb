package com.kdm360.bridalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kdm360.bridalweb.dto.EventDto;
import com.kdm360.bridalweb.model.Event;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.service.EventService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

	@Autowired
    private EventService eventService;
	    
	@Value("${file.image.location}")
    private String imageLocation;
    
	@GetMapping("/getEventList")
	public ResponseEntity<List<Event>> getEventList(){
    	System.out.println("getEventList");
	    List<Event> eventList = eventService.get(true);
	    return new ResponseEntity<>(eventList, HttpStatus.OK);
	}
	
    @GetMapping
    public String listEvents(Model model, Authentication authentication) {
    	    		    
        List<Event> events = eventService.get();
        model.addAttribute("events", events);
        
        if(authentication == null) {
        	return "redirect:/login"; 
    	}else {
    		return "admin/event-list";
    	}	
    }

    @GetMapping("/new")
    public String showEventForm(Model model, HttpSession session) {
    	    	
    	Event event = new Event();
    	model.addAttribute("event", event);
        
        return "admin/event-form";
    }

    @PostMapping
    public String saveEvent(EventDto eventDto, Model model, Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes) {
    	System.out.println("save event " + eventDto);

    	User loggedUser = (User)session.getAttribute("user");
    	if(authentication == null) {
        	return "redirect:/login"; 
    	}
    	
    	Event event;
    	if((eventDto.getEventId() != null) && (eventService.get(eventDto.getEventId()).isPresent())) {
            event = eventService.get(eventDto.getEventId()).get();
    	}else {
    		event = new Event();
    	}
    	event.setTitle(eventDto.getTitle());
    	event.setContent(eventDto.getContent());
    	event.setDate(eventDto.getDate());
    	event.setActive(eventDto.isActive());
    	
        eventService.save(event);
        
        if (!eventDto.getImage().isEmpty()) {
            String imagePath;
			try {
				imagePath = saveFile(eventDto.getImage(), "events/", "EVENT"+event.getEventId() + ".png");
				event.setImage(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        eventService.save(event);
        
        return "redirect:/events"; 
    }

    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable("id") Long id, Model model) {
    	Event event = eventService.get(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("event", event);
      return "admin/event-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
    	eventService.delete(id);
        return "redirect:/events";
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
