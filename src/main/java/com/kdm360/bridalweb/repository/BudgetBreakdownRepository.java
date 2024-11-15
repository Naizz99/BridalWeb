package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.BudgetBreakdown;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.WeddingPlan;

@Repository
public interface BudgetBreakdownRepository extends JpaRepository<BudgetBreakdown, Long> {

	void deleteByPlanId(WeddingPlan weddingPlan);

	List<BudgetBreakdown> findByPlanId(WeddingPlan weddingPlan);
	
	@Query("SELECT SUM(b.amount) FROM BudgetBreakdown b WHERE b.planId.planId = :planId")
	Double calculateTotalAmountByPlanId(@Param("planId") Long planId);

	@Query("SELECT SUM(b.percentage) FROM BudgetBreakdown b WHERE b.planId.planId = :planId")
	Double calculateTotalPercentageByPlanId(@Param("planId") Long planId);

	@Query("SELECT b.categoryId FROM BudgetBreakdown b WHERE b.planId.planId = :planId")
    List<ServiceCategory> findVendorServicesByPlanId(@Param("planId") Long planId);
}
