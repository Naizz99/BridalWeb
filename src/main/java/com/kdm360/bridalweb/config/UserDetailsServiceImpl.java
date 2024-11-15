package com.kdm360.bridalweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.UserRepository;


public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
		
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username).get();
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}else {
			return new SystemUserDetails(user);
		}
		
		
	}

}

