package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.UserLogin;
import com.kdm360.bridalweb.repository.UserLoginRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserLoginService {

    private final UserLoginRepository repo;

    @Autowired
    public UserLoginService(UserLoginRepository repo) {
        this.repo = repo;
    }

    public List<UserLogin> getAllUserLogins() {
        return repo.findAll();
    }

    public Optional<UserLogin> getUserLoginById(Long id) {
        return repo.findById(id);
    }

    public UserLogin addUserLogin(UserLogin userLogin) {
        return repo.save(userLogin);
    }

    public UserLogin updateUserLogin(Long id, UserLogin updatedUserLogin) {
        Optional<UserLogin> existingUserLogin = repo.findById(id);
        if (existingUserLogin.isPresent()) {
            UserLogin userLogin = existingUserLogin.get();
            userLogin.setUser(updatedUserLogin.getUser());
            userLogin.setLoginTime(updatedUserLogin.getLoginTime());
            userLogin.setIpAddress(updatedUserLogin.getIpAddress());
            userLogin.setSuccess(updatedUserLogin.isSuccess());
            return repo.save(userLogin);
        } else {
            throw new RuntimeException("UserLogin not found with id " + id);
        }
    }

    public void deleteUserLogin(Long id) {
        repo.deleteById(id);
    }

	public void deleteByUser(User user) {
		repo.deleteByUser(user);
	}
}
