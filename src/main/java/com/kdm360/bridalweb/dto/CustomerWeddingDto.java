package com.kdm360.bridalweb.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CustomerWeddingDto {

	private Long weddingId;
	
	private String name; 
	private LocalDate date;
	private String location;
	private List<MultipartFile> images;
	private String weddingTypeId;
	private long budget;
	private String createdBy;
	private LocalDate createdDate;
		
	
}