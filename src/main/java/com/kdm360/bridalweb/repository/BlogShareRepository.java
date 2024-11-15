package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.BlogShare;
import com.kdm360.bridalweb.model.User;

@Repository
public interface BlogShareRepository extends JpaRepository<BlogShare, Long> {

	boolean existsByUserId(User user);
}
