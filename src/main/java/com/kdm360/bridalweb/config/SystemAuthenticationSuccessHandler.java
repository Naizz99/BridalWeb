package com.kdm360.bridalweb.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kdm360.bridalweb.model.ServiceCategory;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.service.BlogPostService;
import com.kdm360.bridalweb.service.ServiceCategoryService;
import com.kdm360.bridalweb.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SystemAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServiceCategoryService serviceCategoryService;
	
	@Autowired
	private BlogPostService blogPostService;
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                    Authentication authentication) throws IOException, ServletException {
	    
	    if (authentication == null) {
	        redirectStrategy.sendRedirect(request, response, "/login");
	        return;
	    }
	    
	    User user = userService.findByUsername(authentication.getName()).orElse(null);
	    
	    if (user == null) {
	        redirectStrategy.sendRedirect(request, response, "/login");
	        return;
	    }

	    HttpSession session = request.getSession();
	    session.setAttribute("user", user);
	    session.setAttribute("serviceCategoryList", serviceCategoryService.get(true));
	    session.setAttribute("blogList", blogPostService.get(true));
	    
	    System.out.println("serviceCategoryService.get(true) : " + serviceCategoryService.get(true));
	    
	    boolean isActive = User.STATUS.ACTIVE.equals(user.getStatus());
	    if (isActive && ((user.getRole()).equals(User.ROLE.ADMIN) || (user.getRole()).equals(User.ROLE.VENDOR))) {
	        redirectStrategy.sendRedirect(request, response, "/dashboard");
	    } else if (isActive && (user.getRole()).equals(User.ROLE.CUSTOMER)) {
	    	redirectStrategy.sendRedirect(request, response, "/index");
	    } else if (isActive) {
	        redirectStrategy.sendRedirect(request, response, "/dashboard");
	    } else if (!isActive) {
	        redirectStrategy.sendRedirect(request, response, "/login?userNotActive");
	    } else {
	        redirectStrategy.sendRedirect(request, response, "/pageNotFound");
	    }
	}

	
	public static String getName() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentLoggedName = authentication.getName();
		return currentLoggedName;
	}

}
