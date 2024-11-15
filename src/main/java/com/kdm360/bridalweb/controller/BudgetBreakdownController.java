package com.kdm360.bridalweb.controller;

import org.springframework.web.bind.annotation.*;
import com.kdm360.bridalweb.model.BudgetBreakdown;
import com.kdm360.bridalweb.service.BudgetBreakdownService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budget-breakdowns")
public class BudgetBreakdownController {

    private final BudgetBreakdownService budgetBreakdownService;

    public BudgetBreakdownController(BudgetBreakdownService budgetBreakdownService) {
        this.budgetBreakdownService = budgetBreakdownService;
    }

    @GetMapping
    public List<BudgetBreakdown> getAllBudgetBreakdowns() {
        return budgetBreakdownService.getAllBudgetBreakdowns();
    }

    @GetMapping("/{id}")
    public Optional<BudgetBreakdown> getBudgetBreakdownById(@PathVariable Long id) {
        return budgetBreakdownService.getBudgetBreakdownById(id);
    }

    @PostMapping
    public BudgetBreakdown addBudgetBreakdown(@RequestBody BudgetBreakdown budgetBreakdown) {
        return budgetBreakdownService.addBudgetBreakdown(budgetBreakdown);
    }

    @PutMapping("/{id}")
    public BudgetBreakdown updateBudgetBreakdown(@PathVariable Long id, @RequestBody BudgetBreakdown budgetBreakdown) {
        return budgetBreakdownService.updateBudgetBreakdown(id, budgetBreakdown);
    }

    @DeleteMapping("/{id}")
    public void deleteBudgetBreakdown(@PathVariable Long id) {
        budgetBreakdownService.deleteBudgetBreakdown(id);
    }
}
