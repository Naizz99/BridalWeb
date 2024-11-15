package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.Gallery;
import com.kdm360.bridalweb.repository.GalleryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {
	
	@Autowired
    private GalleryRepository repo;
	
    public List<Gallery> get() {
        return repo.findAll();
    }

    public List<Gallery> get(boolean active) {
        return repo.findByActive(active);
    }
    
    public Optional<Gallery> get(Long id) {
        return repo.findById(id);
    }

    public void save(Gallery gallery) {
      repo.save(gallery);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

}
