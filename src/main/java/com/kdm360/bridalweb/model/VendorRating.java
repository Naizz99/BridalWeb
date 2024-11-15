package com.kdm360.bridalweb.model;

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
@Table(name = "vendor_rating")
@AllArgsConstructor
@NoArgsConstructor
public class VendorRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendorId;
    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User userId; // This can be null if the rating is anonymous
    
    private Integer rating; // Rating value (e.g., 1 to 5)
    private String review; // Optional review text
    private Integer ratingDate;
}