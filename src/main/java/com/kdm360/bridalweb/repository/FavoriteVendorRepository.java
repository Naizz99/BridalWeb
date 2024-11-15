package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.FavoriteVendor;
import com.kdm360.bridalweb.model.User;

import java.util.List;

@Repository
public interface FavoriteVendorRepository extends JpaRepository<FavoriteVendor, Long> {

    @Query("SELECT f.vendor, COUNT(f) AS favoriteCount FROM FavoriteVendor f GROUP BY f.vendor ORDER BY favoriteCount DESC")
    List<Object[]> findMostFavoritedVendors();

	boolean existsByUser(User user);
}
