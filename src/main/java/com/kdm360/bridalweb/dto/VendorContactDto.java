package com.kdm360.bridalweb.dto;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class VendorContactDto {
    private Long contactId;
    private Vendor vendorId;
    private User userId;
    private Integer contactDate;
    private Integer contactTime;
    private String formattedContactDate;
    private String formattedContactTime;
    private String message; 
    private String ipAddress; 
}