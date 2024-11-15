package com.kdm360.bridalweb.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.Vendor.DISTRICT;
import com.kdm360.bridalweb.repository.UserRepository;
import com.kdm360.bridalweb.repository.VendorRepository;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VendorService {

	@Autowired
    private VendorRepository repo;
	
	@Autowired
    private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
    public List<Vendor> get() {
        return repo.findAll();
    }

    public Optional<Vendor> get(Long id) {
        return repo.findById(id);
    }
    
    public List<Vendor> findByActiveUsers() {
    	return repo.findByActiveUsers();
	}

    public void save(Vendor vendor) {
      repo.save(vendor);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public Vendor findByUserId(User user) {
		return repo.findByUserId(user);
	}

	public List<Vendor> findVendorsByCompanyName(String name) {
	    return repo.searchByCompanyName(name);
	}

	public List<Vendor> findVendorsByDistrict(DISTRICT district) {
		return repo.findByDistrictsContaining(district);
	}

	public List<Vendor> findVendorsByAveragePrice(long startPrice, long endPrice) {
		return repo.findVendorsByAveragePrice(startPrice, endPrice);
	}

	public List<Vendor> findVendorsByDistricts(Set<Vendor.DISTRICT> districts) {
	    return repo.findAll().stream()
	            .filter(vendor -> !Collections.disjoint(vendor.getDistricts(), districts))  // Check if there's any overlap
	            .collect(Collectors.toList());
	}
	
	@Transactional
    public Set<Vendor.DISTRICT> getDistrictsByVendorId(Long vendorId) {
        Vendor vendor = repo.findById(vendorId).orElse(null);
        if (vendor != null) {
            Hibernate.initialize(vendor.getDistricts());
            return vendor.getDistricts();
        }
        return Collections.emptySet();
    }
	
	public List<Vendor> findVendorsByCategoryIdAndBudget(Long categoryId, Long budget) {
        return repo.findVendorsByCategoryIdAndBudget(categoryId, budget);
    }

    public List<Vendor> findVendorsByDistrictCategoryAndBudget(DISTRICT district, long categoryId, Long budget) {
    	return repo.findVendorsByDistrictCategoryAndBudget(district, categoryId, budget);
	}

	public List<Vendor> findVendorsByAllInSriLankaByCategoryAndBudget(long categoryId, Long budget) {
		return repo.findVendorsByAllInSriLankaByCategoryAndBudget(categoryId, budget);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}
    
}
