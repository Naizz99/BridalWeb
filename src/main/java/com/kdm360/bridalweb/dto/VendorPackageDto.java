package com.kdm360.bridalweb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

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
public class VendorPackageDto { 

    private Long packageId;
    private Long vendorId;
    private String packageName;
    private String description;
    private BigDecimal price; 
    private LocalDate creationDate;
    private String features; 
    private MultipartFile packageImage; 
    private boolean active;
}