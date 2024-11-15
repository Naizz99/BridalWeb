package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.Event;
import com.kdm360.bridalweb.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

	@Autowired
    private EventRepository repo;

    public List<Event> get() {
        return repo.findAll();
    }

    public List<Event> get(boolean active) {
        return repo.getByActive(active);
    }
    
    public Optional<Event> get(Long id) {
        return repo.findById(id);
    }

    public Event save(Event blogPost) {
        return repo.save(blogPost);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }
}
