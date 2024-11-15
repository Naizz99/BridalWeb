package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.BackgroundImage;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.model.WeddingGallery;
import com.kdm360.bridalweb.repository.BackgroundImageRepository;
import com.kdm360.bridalweb.repository.WeddingGalleryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BackgroundImageService {

	@Autowired
    private BackgroundImageRepository repo;

    public List<BackgroundImage> get() {
        return repo.findAll();
    }

    public Optional<BackgroundImage> get(Long id) {
        return repo.findById(id);
    }
    
    public List<BackgroundImage> get(Settings id) {
        return repo.findBySettingsId(id);
    }

    public BackgroundImage save(BackgroundImage image) {
        return repo.save(image);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

}
