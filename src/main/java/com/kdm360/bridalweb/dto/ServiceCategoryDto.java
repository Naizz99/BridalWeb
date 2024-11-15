package com.kdm360.bridalweb.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryDto {

	private Long categoryId;
	
	private String typeName;
	private MultipartFile image;
	private String description;
	private boolean active;
		
	private String imagePath;
	private int vendorCount;
}