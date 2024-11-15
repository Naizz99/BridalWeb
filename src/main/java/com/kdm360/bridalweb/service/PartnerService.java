package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.Partner;
import com.kdm360.bridalweb.repository.PartnerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

	@Autowired
    private PartnerRepository repo;

    public Optional<Partner> get(Long id) {
        return repo.findById(id);
    }
    
    public List<Partner> get() {
        return repo.findAll();
    }

    public Partner save(Partner client) {
        return repo.save(client);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

}
