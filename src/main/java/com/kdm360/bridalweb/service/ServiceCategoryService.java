package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.repository.ServiceCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {

	@Autowired
    private ServiceCategoryRepository repo;

    public List<ServiceCategory> get() {
        return repo.findAll();
    }

    public Optional<ServiceCategory> get(Long id) {
        return repo.findById(id);
    }

    public List<ServiceCategory> get(boolean active) {
    	return repo.getByActive(active);
	}
    
    public ServiceCategory save(ServiceCategory serviceCategory) {
        return repo.save(serviceCategory);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	
}
