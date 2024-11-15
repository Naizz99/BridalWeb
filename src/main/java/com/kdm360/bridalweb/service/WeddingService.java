package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.repository.VendorRepository;
import com.kdm360.bridalweb.repository.WeddingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingService {

    @Autowired
    private WeddingRepository repo;
    
    public List<Wedding> get() {
        return repo.findAll();
    }

    public Optional<Wedding> get(Long id) {
        return repo.findById(id);
    }
    
    public List<Wedding> get(User user) {
    	return repo.findByUserId(user);
	}

    public Wedding save(Wedding wedding) {
        return repo.save(wedding);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

    public boolean existsByUserId(User user) {
		return repo.existsByUserId(user);
	}

}
