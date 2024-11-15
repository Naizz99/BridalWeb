package com.kdm360.bridalweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "gallery")
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long galleryId;
	
	private String image;
	private String caption;
	
	private boolean active;
}