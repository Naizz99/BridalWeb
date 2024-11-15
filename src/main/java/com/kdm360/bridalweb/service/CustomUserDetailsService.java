package com.kdm360.bridalweb.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user by username
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Check if the user is present, otherwise throw an exception
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        User user = optionalUser.get(); // Get the user from the Optional
        // Return the UserDetails object required by Spring Security
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
