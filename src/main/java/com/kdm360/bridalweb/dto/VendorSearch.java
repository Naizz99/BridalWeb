package com.kdm360.bridalweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorSearch {

	private int vendorId;
	private String companyName;
	private String vendorCats;
	private Long averagePrice;
	private Long startAveragePrice;
	private Long endAveragePrice;
	private String districts;
	
	
		
}