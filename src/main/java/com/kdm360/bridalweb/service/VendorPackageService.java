package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorPackage;
import com.kdm360.bridalweb.repository.VendorPackageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VendorPackageService {
	
	@Autowired
    private VendorPackageRepository repo;

    public List<VendorPackage> get() {
        return repo.findAll();
    }

    public Optional<VendorPackage> get(Long id) {
        return repo.findById(id);
    }

    public VendorPackage save(VendorPackage vendorPackage) {
        return repo.save(vendorPackage);
    }

    public VendorPackage updateVendorPackage(Long id, VendorPackage updatedVendorPackage) {
        Optional<VendorPackage> existingVendorPackage = repo.findById(id);
        if (existingVendorPackage.isPresent()) {
            VendorPackage vendorPackage = existingVendorPackage.get();
            vendorPackage.setVendorId(updatedVendorPackage.getVendorId());
            vendorPackage.setPackageName(updatedVendorPackage.getPackageName());
            vendorPackage.setDescription(updatedVendorPackage.getDescription());
            vendorPackage.setPrice(updatedVendorPackage.getPrice());
            vendorPackage.setCreationDate(updatedVendorPackage.getCreationDate());
            vendorPackage.setFeatures(updatedVendorPackage.getFeatures());
            vendorPackage.setPackageImage(updatedVendorPackage.getPackageImage());
            return repo.save(vendorPackage);
        } else {
            throw new RuntimeException("VendorPackage not found with id " + id);
        }
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public List<VendorPackage> getByVendorId(Vendor vendor) {
		return repo.getByVendorId(vendor);
	}

	@Transactional
	public void deleteByVendorId(Vendor vendorId) {
		repo.deleteByVendorId(vendorId);
	}
}
