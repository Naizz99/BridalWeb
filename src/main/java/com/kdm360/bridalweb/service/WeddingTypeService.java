package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.WeddingType;
import com.kdm360.bridalweb.repository.WeddingRepository;
import com.kdm360.bridalweb.repository.WeddingTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingTypeService {

	@Autowired
    private WeddingTypeRepository repo;

    public List<WeddingType> get() {
        return repo.findAll();
    }

    public Optional<WeddingType> get(Long id) {
        return repo.findById(id);
    }
    
    public List<WeddingType> get(boolean isActive) {
        return repo.findByActive(isActive);
    }

    public WeddingType save(WeddingType weddingType) {
        return repo.save(weddingType);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }
}
