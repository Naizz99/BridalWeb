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
@Table(name = "vendor_profile_view")
@AllArgsConstructor
@NoArgsConstructor
public class VendorProfileView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendorId;
    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User userId; // This can be null if the viewer is not logged in
    
//    private LocalDate viewDate;
//    private LocalTime viewTime;
    private Integer viewDate;
    
    private String ipAddress; 
    
    private int viewCount;
    
}