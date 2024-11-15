package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorContact;
import com.kdm360.bridalweb.model.VendorRating;
import com.kdm360.bridalweb.repository.VendorContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendorContactService {

	@Autowired
    private VendorContactRepository repo;

    public List<VendorContact> get() {
        return repo.findAll();
    }

    public Optional<VendorContact> get(Long id) {
        return repo.findById(id);
    }
    
    public List<VendorContact> findByUserId(User user) {
    	return repo.findByUserId(user);
	}

    public VendorContact save(VendorContact vendorContact) {
        return repo.save(vendorContact);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    @Transactional
	public void deleteByVendorId(Vendor vendor) {
		repo.deleteByVendorId(vendor);
	}

	public List<VendorContact> findByVendorId(Vendor vendor) {
		return repo.findByVendorId(vendor);
	}

	public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}

	public List<VendorContact> getFilteredViews(Long vendorId, Long userId, Integer contactDate) {
	    List<VendorContact> profileContacts = new ArrayList<>();

	    profileContacts = repo.findViewsByCriteria(vendorId, userId, contactDate);
	    
	    return profileContacts;
	}
}
