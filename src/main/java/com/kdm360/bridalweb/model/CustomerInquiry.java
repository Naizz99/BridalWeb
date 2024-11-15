package com.kdm360.bridalweb.model;

import java.time.LocalDateTime;

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
@Table(name = "customer_inquiry")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendor;
    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user; // This can be null if the contact is made by a non-registered user
    
    private String senderEmail; 
    private String senderMobile; 
    
    private LocalDateTime inquiredDate;
    private LocalDateTime inquiredTime;
    private String message; 
    private String ipAddress; // Optionally track IP address
    
}
