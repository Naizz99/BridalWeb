package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.FavoriteVendor;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.FavoriteVendorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteVendorService {

    private final FavoriteVendorRepository repo;

    @Autowired
    public FavoriteVendorService(FavoriteVendorRepository repo) {
        this.repo = repo;
    }

    public List<FavoriteVendor> getAllFavoriteVendors() {
        return repo.findAll();
    }

    public Optional<FavoriteVendor> getFavoriteVendorById(Long id) {
        return repo.findById(id);
    }

    public FavoriteVendor addFavoriteVendor(FavoriteVendor favoriteVendor) {
        return repo.save(favoriteVendor);
    }

    public FavoriteVendor updateFavoriteVendor(Long id, FavoriteVendor updatedFavoriteVendor) {
        Optional<FavoriteVendor> existingFavoriteVendor = repo.findById(id);
        if (existingFavoriteVendor.isPresent()) {
            FavoriteVendor favoriteVendor = existingFavoriteVendor.get();
            favoriteVendor.setUser(updatedFavoriteVendor.getUser());
            favoriteVendor.setVendor(updatedFavoriteVendor.getVendor());
            favoriteVendor.setAddedDate(updatedFavoriteVendor.getAddedDate());
            return repo.save(favoriteVendor);
        } else {
            throw new RuntimeException("FavoriteVendor not found with id " + id);
        }
    }

    public void deleteFavoriteVendor(Long id) {
        repo.deleteById(id);
    }

    public List<Object[]> findMostFavoritedVendors() {
        return repo.findMostFavoritedVendors();
    }

    public boolean existsByUserId(User user) {
		return repo.existsByUser(user);
	}
}
