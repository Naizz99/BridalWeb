package com.kdm360.bridalweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorCategory;

@Repository
public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Long> {
	
	@Query("SELECT vc.vendorId FROM VendorCategory vc WHERE vc.categoryId.categoryId = :categoryId")
    List<Vendor> findVendorsByCategoryId(@Param("categoryId") Long categoryId);

	List<VendorCategory> findByVendorId(Vendor vendor);

	@Query("SELECT COUNT(*) FROM VendorCategory vc WHERE vc.vendorId = :vendorId")
	int getCountByVendorId(Vendor vendorId);

	@Query("SELECT COUNT(*) FROM VendorCategory vc WHERE vc.categoryId = :categoryId")
	int getCountByCategoryId(ServiceCategory categoryId);
	
	Optional<VendorCategory> getCountByVendorIdAndCategoryId(Vendor vendor, ServiceCategory category);

	@Transactional
    @Modifying
    @Query("DELETE FROM VendorCategory vc WHERE vc.vendorId = :vendorId")
	void deleteByVendorId(Vendor vendorId);

}
