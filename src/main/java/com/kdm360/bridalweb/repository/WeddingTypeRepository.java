package com.kdm360.bridalweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.WeddingType;

@Repository
public interface WeddingTypeRepository extends JpaRepository<WeddingType, Long> {

	List<WeddingType> findByActive(boolean isActive);
}
