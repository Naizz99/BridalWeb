package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

}
