package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorGallery;

@Repository
public interface VendorGalleryRepository extends JpaRepository<VendorGallery, Long> {

	List<VendorGallery> findByVendorId(Vendor vendor);

	@Transactional
    @Modifying
    @Query("DELETE FROM VendorGallery vc WHERE vc.vendorId = :vendorId")
	void deleteByVendorId(Vendor vendorId);
}
