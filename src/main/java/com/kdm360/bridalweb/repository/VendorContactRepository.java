package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorContact;
import com.kdm360.bridalweb.model.VendorRating;

@Repository
public interface VendorContactRepository extends JpaRepository<VendorContact, Long> {

	@Transactional
	@Modifying
    @Query("DELETE FROM VendorContact vc WHERE vc.vendorId = :vendorId")
	void deleteByVendorId(Vendor vendorId);

	List<VendorContact> findByVendorId(Vendor vendor);

	List<VendorContact> findByUserId(User user);

	boolean existsByUserId(User user);
	
	@Query("SELECT v FROM VendorContact v WHERE " +
		       "(:vendorId IS NULL OR v.vendorId.vendorId = :vendorId) AND " +
		       "(:userId IS NULL OR v.userId.userId = :userId) AND " +
		       "(:contactDate IS NULL OR v.contactDate = :contactDate)")
	    List<VendorContact> findViewsByCriteria(@Param("vendorId") Long vendorId,
             								@Param("userId") Long userId,
     										@Param("contactDate") Integer contactDate);
}
