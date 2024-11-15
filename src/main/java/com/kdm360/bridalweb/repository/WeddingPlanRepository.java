package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.WeddingPlan;

@Repository
public interface WeddingPlanRepository extends JpaRepository<WeddingPlan, Long> {

	List<WeddingPlan> findByUserId(User user);

	boolean existsByUserId(User user);
}
