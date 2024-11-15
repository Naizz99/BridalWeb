package com.kdm360.bridalweb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class WeddingPlanDto {
    private Long planId;
    private Long userId;
    private Long weddingTypeId;
    private String planNickName;
    private String budgetType;
    private Double estimatedTotal;
    private String location;
    private LocalDate weddingDate;
}