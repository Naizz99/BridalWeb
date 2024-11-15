package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorProfileView;
import com.kdm360.bridalweb.repository.VendorProfileViewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendorProfileViewService {

	@Autowired
    private VendorProfileViewRepository repo;

    public List<VendorProfileView> get() {
        return repo.findAll();
    }

    public Optional<VendorProfileView> get(Long id) {
        return repo.findById(id);
    }
    
    public List<VendorProfileView> findByUserIdOrderByViewDateDesc(User user) {
    	return repo.findByUserIdOrderByViewDateDesc(user);
	}

    public VendorProfileView save(VendorProfileView vendorProfileView) {
        return repo.save(vendorProfileView);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    @Transactional
	public void deleteByVendorId(Vendor vendorId) {
		repo.deleteByVendorId(vendorId);
	}

	public List<VendorProfileView> findByVendorId(Vendor vendor) {
		return repo.findByVendorId(vendor);
	}

	public Optional<VendorProfileView> getByVendorIdAndViewDate(Vendor vendor, int today) {
		return repo.getByVendorIdAndViewDate(vendor, today);
	}

	public Optional<VendorProfileView> getByVendorIdAndUserIdAndViewDate(Vendor vendor, User user, int today) {
		return repo.getByVendorIdAndUserIdAndViewDate(vendor, user, today);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}

	public Integer getTotalViewCountByVendorId(Long vendorId) {
        return repo.getTotalViewCountByVendorId(vendorId);
    }
	
	public List<VendorProfileView> findByVendorIdAndUserId(Vendor vendor, User user) {
        return repo.findByVendorIdAndUserId(vendor, user);
    }

    public List<VendorProfileView> findByVendorIdAndViewDate(Vendor vendor, Integer date) {
        return repo.findByVendorIdAndViewDate(vendor, date);
    }

    public List<VendorProfileView> findByVendorIdAndUserIdAndViewDate(Vendor vendor, User user, Integer date) {
        return repo.findByVendorIdAndUserIdAndViewDate(vendor, user, date);
    }

	public List<VendorProfileView> getFilteredViews(Long vendorId, Long userId, Integer viewDate) {
	    List<VendorProfileView> ViewList = new ArrayList<>();

	    ViewList = repo.findViewsByCriteria(vendorId, userId, viewDate);
	    
	    return ViewList;
	}
}
