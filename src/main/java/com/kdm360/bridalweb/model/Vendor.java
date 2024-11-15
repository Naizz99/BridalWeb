package com.kdm360.bridalweb.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "vendor")
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {

	public enum DISTRICT {
		ALLINSRILANKA,
	    AMPARA,
	    ANURADHAPURA,
	    BADULLA,
	    BATTICALOA,
	    COLOMBO,
	    GALLE,
	    GAMPAHA,
	    HAMBANTOTA,
	    JAFFNA,
	    KALUTARA,
	    KANDY,
	    KEGALLE,
	    KILINOCHCHI,
	    KURUNEGALA,
	    MANNAR,
	    MATALE,
	    MATARA,
	    MONARAGALA,
	    MULLAITIVU,
	    NUWARA_ELIYA,
	    POLONNARUWA,
	    PUTTALAM,
	    RATNAPURA,
	    TRINCOMALEE,
	    VAVUNIYA;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorId;
	
	
	private String companyName;
	private String contactName;
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
	
	@Lob
	private String description;
	
	private String logo;
	private String coverImage;
	
	private String discount;
	private String advance;
	private Long averagePrice;
	
	private String openDays;
	private String openHours;
	
	private boolean isOnlinePaymentApproved;
	
	private LocalDate registrationDate;
	
//	private DISTRICT district;
	
	@ElementCollection(targetClass = DISTRICT.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "vendor_districts", joinColumns = @JoinColumn(name = "vendor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "district")
    private Set<DISTRICT> districts;
	
	@OneToOne
	@JoinColumn(name = "userId")
	private User userId;
		
	private Integer avgRate;
}