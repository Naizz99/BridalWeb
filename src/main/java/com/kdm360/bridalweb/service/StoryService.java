package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.Story;
import com.kdm360.bridalweb.repository.StoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

	@Autowired
    private StoryRepository repo;
		
    public List<Story> get() {
        return repo.findAll();
    }

    public List<Story> get(boolean active){
        return repo.getByActive(active);
    }
    
    public Optional<Story> get(Long id) {
        return repo.findById(id);
    }

    public void save(Story vendor) {
      repo.save(vendor);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

}
