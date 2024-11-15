package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.CustomerInquiry;
import com.kdm360.bridalweb.model.User;

@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {

	boolean existsByUser(User user);
}
