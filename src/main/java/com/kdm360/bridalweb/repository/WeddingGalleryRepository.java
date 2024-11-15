package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.model.WeddingGallery;

@Repository
public interface WeddingGalleryRepository extends JpaRepository<WeddingGallery, Long> {

	@Query("SELECT wg FROM WeddingGallery wg WHERE wg.weddingId.weddingId = :weddingId ORDER BY wg.weddingGalleryId ASC")
	WeddingGallery  getFirstWeddingGalleryById(Long weddingId);

	List<WeddingGallery> getByWeddingId(Wedding wedding);

	@Transactional
    @Modifying
    @Query("DELETE FROM WeddingGallery vc WHERE vc.weddingId = :weddingId")
	void deleteByWeddingId(Wedding weddingId);
}
