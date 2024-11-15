package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdm360.bridalweb.model.Wedding;
import com.kdm360.bridalweb.model.WeddingGallery;
import com.kdm360.bridalweb.repository.WeddingGalleryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingGalleryService {

	@Autowired
    private WeddingGalleryRepository repo;

    public List<WeddingGallery> get() {
        return repo.findAll();
    }

    public Optional<WeddingGallery> get(Long id) {
        return repo.findById(id);
    }

    public WeddingGallery save(WeddingGallery weddingGallery) {
        return repo.save(weddingGallery);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public WeddingGallery  getFirstWeddingGalleryById(Long weddingId) {
		return repo.getFirstWeddingGalleryById(weddingId);
	}

	public List<WeddingGallery> getByWeddingId(Wedding wedding) {
		return repo.getByWeddingId(wedding);
	}

	@Transactional
	public void deleteByWeddingId(Wedding wedding) {
		repo.deleteByWeddingId(wedding);
	}
}
