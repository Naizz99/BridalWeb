package com.kdm360.bridalweb.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.Vendor.DISTRICT;

import lombok.Data;

@Data
public class VendorDto {

	private Long vendorId;
	
	private String companyName;
	private String contactName;
	
	private String username;
	private String password;
	
	private String email;
	private String mobile1;
	private String mobile2;
	private String mobile3;
	private String address;
	private String branches; // branch 01 | branch 02 | branch 03
	
	private String whatsapp;
	private String website;
	private String googleMap;
	private String facebook;
	private String instagram;
	private String youtube;
	private String tiktok;
	private String twitter;
	private String linkedin;
	
	private String description;
	private MultipartFile logo;
	private MultipartFile coverImage;
	private List<MultipartFile> images;
	private String vendorCats;
	
	private String discount;
	private String advance;
	private Long averagePrice;
	private Long startAveragePrice;
	private Long endAveragePrice;
	
	private String openDays;
	private String openHours;
	
	private String status;
	
	private boolean isOnlinePaymentApproved;
	
	private LocalDate registrationDate;
	
	private long userId;
	
	private Set<String> districts;
	
	public Set<String> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<String> districts) {
        this.districts = districts;
    }
	
		
}