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
@Table(name = "partner")
@AllArgsConstructor
@NoArgsConstructor
public class Partner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	private String name; 
	private String image; 
	private String webSite; 
}