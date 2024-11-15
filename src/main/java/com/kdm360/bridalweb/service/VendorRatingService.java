package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorRating;
import com.kdm360.bridalweb.repository.VendorRatingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendorRatingService {

	@Autowired
    private VendorRatingRepository repo;

    public List<VendorRating> get() {
        return repo.findAll();
    }

    public Optional<VendorRating> get(Long id) {
        return repo.findById(id);
    }

    public VendorRating save(VendorRating vendorRating) {
        return repo.save(vendorRating);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    @Transactional
	public void deleteByVendorId(Vendor vendor) {
		repo.deleteByVendorId(vendor);
	}

	public List<VendorRating> findByVendorId(Vendor vendor) {
		return repo.findByVendorId(vendor);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}

	public List<VendorRating> getFilteredViews(Long vendorId, Long userId, Integer rating, Integer ratingDate) {
	    List<VendorRating> rateList = new ArrayList<>();

	    rateList = repo.findViewsByCriteria(vendorId, userId, rating, ratingDate);
	    
	    return rateList;
	}

	public int getTotalViewCountByVendorId(Long vendorId) {
		return repo.getTotalViewCountByVendorId(vendorId);
	}

	public Integer getAverageRatesByVendorId(Long vendorId) {
		return repo.getAverageRatesByVendorId(vendorId);
	}
}
