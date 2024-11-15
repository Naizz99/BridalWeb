package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Wedding;

@Repository
public interface WeddingRepository extends JpaRepository<Wedding, Long> {

	List<Wedding> findByUserId(User user);

	boolean existsByUserId(User user);
}
