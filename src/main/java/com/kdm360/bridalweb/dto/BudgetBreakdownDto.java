package com.kdm360.bridalweb.dto;

import lombok.Data;

@Data
public class BudgetBreakdownDto {
    private Long breakdownId;
    private Long planId;
    private Long categoryId;
    private Double amount;
    private Double percentage;
}