package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

	List<Gallery> findByActive(boolean active);
}
