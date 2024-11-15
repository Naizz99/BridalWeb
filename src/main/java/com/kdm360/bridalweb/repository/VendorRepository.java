package com.kdm360.bridalweb.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Vendor;
import com.kdm360.bridalweb.model.Vendor.DISTRICT;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

	Vendor findByUserId(User user);

	@Query("SELECT v FROM Vendor v WHERE v.userId.status = 'ACTIVE'")
	List<Vendor> findByActiveUsers();
	
	@Query("SELECT v FROM Vendor v WHERE LOWER(v.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Vendor> searchByCompanyName(String name);

	List<Vendor> findByDistrictsContaining(Vendor.DISTRICT district);

	@Query("SELECT v FROM Vendor v WHERE v.averagePrice BETWEEN :startPrice AND :endPrice")
	List<Vendor> findVendorsByAveragePrice(long startPrice, long endPrice);

//	@Query("SELECT v FROM Vendor v WHERE v.districts IN :districts")
//	List<Vendor> findByDistrictsIn(@Param("districts") Set<Vendor.DISTRICT> districts);
	
//	@Query("SELECT v FROM Vendor v WHERE v.district IN :districts")
//	List<Vendor> findByDistrictsIn(@Param("districts") Set<String> districts);

	@Query("SELECT v FROM Vendor v LEFT JOIN FETCH v.districts WHERE v.vendorId = :vendorId")
    Vendor findByIdWithDistricts(@Param("vendorId") Long vendorId);

	@Query("SELECT v FROM Vendor v JOIN VendorCategory vc ON v.vendorId = vc.vendorId.vendorId WHERE vc.categoryId.categoryId = :categoryId AND v.averagePrice <= :budget")
	List<Vendor> findVendorsByCategoryIdAndBudget(@Param("categoryId") Long categoryId, @Param("budget") Long budget);

	@Query("SELECT v FROM Vendor v JOIN VendorCategory vc ON v.vendorId = vc.vendorId.vendorId " +
		       "JOIN v.districts d " +
		       "WHERE d = :district AND vc.categoryId.categoryId = :categoryId AND v.averagePrice <= :budget")
	List<Vendor> findVendorsByDistrictCategoryAndBudget(@Param("district") Vendor.DISTRICT district, 
	                                                    @Param("categoryId") Long categoryId, 
	                                                    @Param("budget") Long budget);

	@Query("SELECT v FROM Vendor v JOIN VendorCategory vc ON v.vendorId = vc.vendorId.vendorId " +
		       "JOIN v.districts d " +
		       "WHERE d = com.kdm360.bridalweb.model.Vendor.DISTRICT.ALLINSRILANKA " +
		       "AND vc.categoryId.categoryId = :categoryId AND v.averagePrice <= :budget")
	List<Vendor> findVendorsByAllInSriLankaByCategoryAndBudget(@Param("categoryId") Long categoryId, 
	                                        @Param("budget") Long budget);

	boolean existsByUserId(User user);

}
