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
@Table(name = "wedding_type")
@AllArgsConstructor
@NoArgsConstructor
public class WeddingType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long weddingTypeId;
	
	private String typeName;
	private String image;
	private String description;
	
	private boolean active;
}