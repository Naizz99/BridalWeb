package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.User.ROLE;
import com.kdm360.bridalweb.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
    private UserRepository repo;

    public List<User> get() {
        return repo.findAll();
    }

    public Optional<User> get(Long id) {
        return repo.findById(id);
    }
    
    public List<User> get(ROLE role) {
    	return repo.findByRole(role);
	}

    public User save(User user) {
        return repo.save(user);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public Optional<User> findByUsername(String name) {
		return repo.findByUsername(name);
	}

	
}
