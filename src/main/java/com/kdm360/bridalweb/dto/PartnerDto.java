package com.kdm360.bridalweb.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PartnerDto {

	private Long id;
	
	private String name; 
	private MultipartFile image; 
	private String webSite; 
	
}