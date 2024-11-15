package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.SettingsRepository;

import java.util.Optional;

@Service
public class SettingsService {

	@Autowired
    private SettingsRepository repo;

    public Optional<Settings> get(Long id) {
        return repo.findById(id);
    }

    public Settings save(Settings settings) {
        return repo.save(settings);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public Optional<Settings> get(int i) {
		return repo.findBySystemSeq(i);
	}
}
