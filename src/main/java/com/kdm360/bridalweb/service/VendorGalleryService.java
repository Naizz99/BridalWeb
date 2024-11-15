package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorGallery;
import com.kdm360.bridalweb.repository.VendorGalleryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VendorGalleryService {
	
	@Autowired
    private VendorGalleryRepository repo;

    public List<VendorGallery> get() {
        return repo.findAll();
    }

    public Optional<VendorGallery> get(Long id) {
        return repo.findById(id);
    }

    public VendorGallery save(VendorGallery vendorGallery) {
        return repo.save(vendorGallery);
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }

	public List<VendorGallery> getByVendorId(Vendor vendor) {
		return repo.findByVendorId(vendor);
	}

	@Transactional
	public void deleteByVendorId(Vendor vendorId) {
		repo.deleteByVendorId(vendorId);
	}
}
