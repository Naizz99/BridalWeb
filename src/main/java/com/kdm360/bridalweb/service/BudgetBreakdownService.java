package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.BudgetBreakdown;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.WeddingPlan;
import com.kdm360.bridalweb.repository.BudgetBreakdownRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetBreakdownService {
	
	@Autowired
    private BudgetBreakdownRepository repo;

	public List<BudgetBreakdown> getAllBudgetBreakdowns() {
        return repo.findAll();
    }

    public Optional<BudgetBreakdown> getBudgetBreakdownById(Long id) {
        return repo.findById(id);
    }

    public BudgetBreakdown addBudgetBreakdown(BudgetBreakdown budgetBreakdown) {
        return repo.save(budgetBreakdown);
    }

    public BudgetBreakdown updateBudgetBreakdown(Long id, BudgetBreakdown updatedBudgetBreakdown) {
        Optional<BudgetBreakdown> existingBudgetBreakdown = repo.findById(id);
        if (existingBudgetBreakdown.isPresent()) {
            BudgetBreakdown budgetBreakdown = existingBudgetBreakdown.get();
            budgetBreakdown.setPlanId(updatedBudgetBreakdown.getPlanId());
            budgetBreakdown.setCategoryId(updatedBudgetBreakdown.getCategoryId());
            budgetBreakdown.setAmount(updatedBudgetBreakdown.getAmount());
            budgetBreakdown.setPercentage(updatedBudgetBreakdown.getPercentage());
            return repo.save(budgetBreakdown);
        } else {
            throw new RuntimeException("BudgetBreakdown not found with id " + id);
        }
    }

    public void deleteBudgetBreakdown(Long id) {
    	repo.deleteById(id);
    }

	public void saveAll(List<BudgetBreakdown> budgetBreakdowns) {
		repo.saveAll(budgetBreakdowns);
	}

	@Transactional
    public void deleteByPlanId(WeddingPlan weddingPlan) {
		repo.deleteByPlanId(weddingPlan);
    }

	public List<BudgetBreakdown> findByPlanId(WeddingPlan weddingPlan) {
		return repo.findByPlanId(weddingPlan);
	}

	public Double calculateTotalAmountByPlanId(Long planId) {
        return repo.calculateTotalAmountByPlanId(planId);
    }

    public Double calculateTotalPercentageByPlanId(Long planId) {
        return repo.calculateTotalPercentageByPlanId(planId);
    }

	public List<ServiceCategory> findVendorServicesByPlanId(Long planId) {
		return repo.findVendorServicesByPlanId(planId);
	}

}
