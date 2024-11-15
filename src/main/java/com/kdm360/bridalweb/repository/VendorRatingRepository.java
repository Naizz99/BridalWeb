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
import com.kdm360.bridalweb.model.VendorRating;

@Repository
public interface VendorRatingRepository extends JpaRepository<VendorRating, Long> {

	@Transactional
    @Modifying
    @Query("DELETE FROM VendorRating vc WHERE vc.vendorId = :vendorId")
	void deleteByVendorId(Vendor vendorId);

	List<VendorRating> findByVendorId(Vendor vendor);

	boolean existsByUserId(User user);
	
	@Query("SELECT COUNT(*) FROM VendorRating v WHERE v.vendorId.vendorId = :vendorId")
    Integer getTotalViewCountByVendorId(@Param("vendorId") Long vendorId);
	
	@Query("SELECT v FROM VendorRating v WHERE " +
		       "(:vendorId IS NULL OR v.vendorId.vendorId = :vendorId) AND " +
		       "(:userId IS NULL OR v.userId.userId = :userId) AND " +
		       "(:rating IS NULL OR v.rating = :rating) AND " +
		       "(:ratingDate IS NULL OR v.ratingDate = :ratingDate)")
	    List<VendorRating> findViewsByCriteria(@Param("vendorId") Long vendorId,
                								@Param("userId") Long userId,
        										@Param("rating") Integer rating,
        										@Param("ratingDate") Integer ratingDate);
	
	@Query("SELECT AVG(v.rating) FROM VendorRating v WHERE v.vendorId.vendorId = :vendorId")
    Integer getAverageRatesByVendorId(@Param("vendorId") Long vendorId);
}
