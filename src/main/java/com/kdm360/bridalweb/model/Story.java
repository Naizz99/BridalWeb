package com.kdm360.bridalweb.model;

import jakarta.persistence.Column;
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
@Table(name = "story")
@AllArgsConstructor
@NoArgsConstructor
public class Story {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storyId;
	
	private String image;
	private long date;
	private String title;
	
	@Column(length = 250)
	private String description;
	
	private boolean active;
	
}