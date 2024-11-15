package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.model.UserLogin;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

	void deleteByUser(User user);
}
