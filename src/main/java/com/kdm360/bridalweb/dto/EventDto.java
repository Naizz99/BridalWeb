package com.kdm360.bridalweb.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class EventDto {

	private Long eventId;
	    
    private Integer date;
    private String title;
    private String content;
    private MultipartFile image;
    private boolean active;
   
}