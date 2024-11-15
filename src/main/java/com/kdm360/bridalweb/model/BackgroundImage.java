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
@Table(name = "background_image")
@AllArgsConstructor
@NoArgsConstructor
public class BackgroundImage{
	
	public enum TYPE {IMAGE, VIDEO}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageId;
	
	private String path;
	private TYPE type;
	
	@ManyToOne
	@JoinColumn(name = "id")
	private Settings settingsId;
}