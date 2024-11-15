package com.kdm360.bridalweb.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class WeddingTypeDto {
		
	private Long weddingTypeId;
	private String typeName;
	private MultipartFile image;
	private String description;
	private boolean active;
		
}