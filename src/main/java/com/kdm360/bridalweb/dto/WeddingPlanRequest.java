package com.kdm360.bridalweb.dto;

import java.util.List;
import lombok.Data;

@Data
public class WeddingPlanRequest {
    private WeddingPlanDto weddingPlan;
    private List<BudgetBreakdownDto> budgetBreakdowns;
}
