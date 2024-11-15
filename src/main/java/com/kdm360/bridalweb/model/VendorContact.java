package com.kdm360.bridalweb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@Table(name = "vendor_contact")
@AllArgsConstructor
@NoArgsConstructor
public class VendorContact {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendorId;
    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User userId; // This can be null if the contact is made by a non-registered user
    
    private Integer contactDate;
    private Integer contactTime;
    private String message; 
    private String ipAddress; // Optionally track IP address
    
    private Long packageId;
}