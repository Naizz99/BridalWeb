package com.kdm360.bridalweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.VendorProfileView;

@Repository
public interface VendorProfileViewRepository extends JpaRepository<VendorProfileView, Long> {

	@Transactional
    @Modifying
    @Query("DELETE FROM VendorProfileView vc WHERE vc.vendorId = :vendorId")
	void deleteByVendorId(Vendor vendorId);

	List<VendorProfileView> findByVendorId(Vendor vendor);

	Optional<VendorProfileView> getByVendorIdAndViewDate(Vendor vendor, int today);

	Optional<VendorProfileView> getByVendorIdAndUserIdAndViewDate(Vendor vendor, User user, int today);

	List<VendorProfileView> findByUserIdOrderByViewDateDesc(User user);

	boolean existsByUserId(User user);
	
	@Query("SELECT SUM(v.viewCount) FROM VendorProfileView v WHERE v.vendorId.vendorId = :vendorId")
    Integer getTotalViewCountByVendorId(@Param("vendorId") Long vendorId);
	
    List<VendorProfileView> findByVendorIdAndUserId(Vendor vendor, User userId);

    List<VendorProfileView> findByVendorIdAndViewDate(Vendor vendor, Integer viewDate);

    List<VendorProfileView> findByVendorIdAndUserIdAndViewDate(Vendor vendor, User userId, Integer viewDate);
	
	@Query("SELECT v FROM VendorProfileView v WHERE " +
		       "(:vendorId IS NULL OR v.vendorId.vendorId = :vendorId) AND " +
		       "(:userId IS NULL OR v.userId.userId = :userId) AND " +
		       "(:viewDate IS NULL OR v.viewDate = :viewDate)")
	    List<VendorProfileView> findViewsByCriteria(@Param("vendorId") Long vendorId,
	                                          @Param("userId") Long userId,
	                                          @Param("viewDate") Integer viewDate);
}
