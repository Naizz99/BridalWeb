package com.kdm360.bridalweb.dto;

import java.time.LocalDate;

import com.kdm360.bridalweb.model.User;

import lombok.Data;

@Data
public class WeddingDto {

	private Long weddingId;
	
	private String name; 
	private LocalDate date;
	private String location;
	private String image;
	private String weddingTypeId;
	private long typeId;
	private long budget;
	private String createdBy;
	private LocalDate createdDate;
	private User userId;	
	
}