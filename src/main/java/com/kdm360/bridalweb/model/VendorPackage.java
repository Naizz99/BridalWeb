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
@Table(name = "vendor_package")
@AllArgsConstructor
@NoArgsConstructor
public class VendorPackage { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendorId;
    
    private String packageName;
    private String description;
    private BigDecimal price; 
    private LocalDate creationDate;
    
    private String features; 
    private String packageImage; 
    
    private boolean active;
}