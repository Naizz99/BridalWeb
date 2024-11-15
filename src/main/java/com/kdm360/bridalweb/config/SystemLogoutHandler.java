package com.kdm360.bridalweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class SystemLogoutHandler  implements LogoutHandler{
	
	@Autowired
	private UserRepository UserRepository;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Object principal = authentication.getPrincipal();
		
		String username="";
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		} else {
		   username = principal.toString();
		}
		if(username != "") {
			User user=UserRepository.findByUsername(username).get();
		}
		System.out.println("Logout : " + username);
		
	}

	

}
