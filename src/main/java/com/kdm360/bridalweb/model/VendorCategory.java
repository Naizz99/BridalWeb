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
@Table(name = "vendor_category")
@AllArgsConstructor
@NoArgsConstructor
public class VendorCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorVendorTypeId;
	
	@ManyToOne
	@JoinColumn(name = "vendorId")
	private Vendor vendorId;
	
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private ServiceCategory categoryId;
}