package com.kdm360.bridalweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.kdm360.bridalweb.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private SystemAuthenticationSuccessHandler successHandler;
	
    private final UserDetailsService userService;
//    private final CustomOAuth2UserService oauthUserService;

//    public WebSecurityConfig(CustomUserDetailsService userService, CustomOAuth2UserService oauthUserService) {
//        this.userService = userService;
//        this.oauthUserService = oauthUserService;
//    }

    public WebSecurityConfig(CustomUserDetailsService userService) {
        this.userService = userService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("deprecation")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(
                		"/**", 
                		"/faq/**", 
//                		"/public/**",
                		"/index/**",
                		"/blogs/**",
//                		"/test/**",
                		"/weddings/**",
                		"/error/**",
                		"/login/**",
                		"/sendOtp/**"
                	).permitAll()
                .anyRequest().authenticated())               
            .formLogin(formLogin -> formLogin
                .loginPage("/login") 
                .successHandler(successHandler)
                .permitAll())
            .logout(logout -> logout
                .permitAll())
//            .oauth2Login(oauthLogin -> oauthLogin
//                .loginPage("/login")                 
//                .userInfoEndpoint(userInfo -> userInfo
//                    .userService(oauthUserService)))
            .userDetailsService(userService);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/resources/**", 
                "/static/**", 
                "/bootstrap/css/**", 
                "/bootstrap/fonts/**", 
                "/bootstrap/js/**", 
                "/css/**", 
                "/font-awesome/css/**", 
                "/font-awesome/fonts/**",
                "/gallery-plugin/css/**", 
                "/images/**", 
                "/js/**", 
                "/owl.carousel/**", 
                "/rs-plugin/js/**", 
                "/rs-plugin/css/**", 
                "/slick-slider/**", 
                "/prettyPhoto/css/**", 
                "/prettyPhoto/images/**", 
                "/prettyPhoto/js/**", 
                "/video/**",
                "/admin/img/**",
                "/admin/vendor/bootstrap/css/**",
                "/admin/vendor/bootstrap-icons/**",
                "/admin/vendor/boxicons/css/**",
                "/admin/vendor/quill/**",
                "/admin/vendor/quill/**",
                "/admin/vendor/remixicon/**",
                "/admin/vendor/simple-datatables/**",
                "/admin/css/**",
                "/admin/vendor/apexcharts/**",
                "/admin/vendor/bootstrap/js/**",
                "/admin/vendor/chart.js/**",
                "/admin/vendor/echarts/**",
                "/admin/vendor/quill**",
                "/admin/vendor/simple-datatables/**",
                "/admin/vendor/tinymce/**",
                "/admin/vendor/php-email-form/**",
                "/admin/js/**"                
        );
    }
}
