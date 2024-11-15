package com.kdm360.bridalweb.dto;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;

import lombok.Data;

@Data
public class VendorProfileViewDto {

    private Long viewId;
    private Vendor vendorId;
    private User userId;
    private Integer viewDate;
    private String formattedViewDate;
    private String ipAddress; 
    private int viewCount;
    
    private int ratingCount;
    
}