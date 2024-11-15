package com.kdm360.bridalweb.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class SettingsDto {

	private Long id;
	
	private String systemName; 
	private MultipartFile systemLogo; 
	
	private List<MultipartFile> landingContents;
	
	private String aboutUs;
	//private String aboutUsImage;
	private String footerDescription;
	
	private List<MultipartFile> gallery;
	
	private String email1; 
	private String email2; 
	private String mobile1;
	private String mobile2;
	private String whatsapp;
	
	private String facebook;
	private String instagram;
	private String twitter;
	
}