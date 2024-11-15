package com.kdm360.bridalweb.model;

import java.time.LocalDate;
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
@Table(name = "wedding")
@AllArgsConstructor
@NoArgsConstructor
public class Wedding {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long weddingId;
	
	private String name; //Bride Name & Groom Name
	private LocalDate date;
	private String location;
	
	@ManyToOne
	@JoinColumn(name = "weddingTypeId")
	private WeddingType weddingTypeId;
	
	private long budget;
	
	private String createdBy;
	private LocalDate createdDate;
		
	@ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
	
}