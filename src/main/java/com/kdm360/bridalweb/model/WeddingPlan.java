package com.kdm360.bridalweb.model;

import java.math.BigDecimal;
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
@Table(name = "wedding_plan")
@AllArgsConstructor
@NoArgsConstructor
public class WeddingPlan {

	public enum BUDGET {FIXED, ADJUSTABLE}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    
    @ManyToOne
	@JoinColumn(name = "weddingTypeId")
	private WeddingType weddingTypeId;
    
    private String planNickName;
    private BUDGET budgetType; 
    private Double estimatedTotal;
    private String location;
    private LocalDate weddingDate;
}