package com.kdm360.bridalweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	private String systemName; 
	private String systemLogo; 
	private String email1; 
	private String email2; 
	private String mobile1;
	private String mobile2;
	private String footerDescription;
	private String aboutUs;
	private String aboutUsImage;
	private String whatsapp;
	private String facebook;
	private String instagram;
	private String twitter;
	private int systemSeq;
}