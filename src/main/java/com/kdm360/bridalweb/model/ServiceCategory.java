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
@Table(name = "service_category")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	private String typeName;
	private String image;
	private String description;
	
	private boolean active;
}
