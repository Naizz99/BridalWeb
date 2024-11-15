package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.FAQ;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

	List<FAQ> findByIsActive(boolean isActive);
}
