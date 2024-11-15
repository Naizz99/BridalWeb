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
@Table(name = "favorite_vendor")
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendor;
    
    private LocalDateTime addedDate;
}