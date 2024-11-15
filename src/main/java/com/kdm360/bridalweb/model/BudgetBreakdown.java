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
@Table(name = "budget_breakdown")
@AllArgsConstructor
@NoArgsConstructor
public class BudgetBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breakdownId;
    
    @ManyToOne
    @JoinColumn(name = "planId")
    private WeddingPlan planId;
    
    @ManyToOne
	@JoinColumn(name = "categoryId")
	private ServiceCategory categoryId;
        
    private Double amount;
    private Double percentage;
}