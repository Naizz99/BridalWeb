package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.dto.WeddingPlanRequest;
import com.kdm360.bridalweb.model.BudgetBreakdown;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.WeddingPlan;
import com.kdm360.bridalweb.repository.BudgetBreakdownRepository;
import com.kdm360.bridalweb.repository.WeddingPlanRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeddingPlanService {

	@Autowired
    private WeddingPlanRepository repo;

	@Autowired
    private BudgetBreakdownRepository budgetBreakdownRepo;
	
    public List<WeddingPlan> get() {
        return repo.findAll();
    }

    public Optional<WeddingPlan> get(Long id) {
        return repo.findById(id);
    }

    public List<WeddingPlan> get(User user) {
    	return repo.findByUserId(user);
	}
    
    public WeddingPlan save(WeddingPlan weddingPlan) {
        return repo.save(weddingPlan);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}

}
