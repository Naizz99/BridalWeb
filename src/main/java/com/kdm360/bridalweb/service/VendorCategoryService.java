package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.Vendor.DISTRICT;
import com.kdm360.bridalweb.model.VendorCategory;
import com.kdm360.bridalweb.repository.VendorCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VendorCategoryService {
	
	@Autowired
    private VendorCategoryRepository repo;

    public List<VendorCategory> get() {
        return repo.findAll();
    }

    public Optional<VendorCategory> get(Long id) {
        return repo.findById(id);
    }

    public VendorCategory save(VendorCategory vendorCategory) {
        return repo.save(vendorCategory);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    
    public List<Vendor> findVendorsByCategoryId(Long categoryId){
    	return repo.findVendorsByCategoryId(categoryId);
    }

	public List<VendorCategory> getByVendor(Vendor vendor) {
		return repo.findByVendorId(vendor);
	}

	public int getCountByVendorId(Vendor vendor) {
		return repo.getCountByVendorId(vendor);
	}

	public int getCountByCategoryId(ServiceCategory category) {
		return repo.getCountByCategoryId(category);
	}
	
	public Optional<VendorCategory> getByVendorAndCategory(Vendor vendor, ServiceCategory category) {
		return repo.getCountByVendorIdAndCategoryId(vendor, category);
	}

	@Transactional
	public void deleteByVendorId(Vendor vendorId) {
		repo.deleteByVendorId(vendorId);
	}

}
