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
@Table(name = "vendor_gallery")
@AllArgsConstructor
@NoArgsConstructor
public class VendorGallery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorGalleryId;
	
	private String image;
	
	@ManyToOne
	@JoinColumn(name = "vendorId")
	private Vendor vendorId;
}