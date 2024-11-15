package com.kdm360.bridalweb.dto;

import java.time.LocalDate;
import com.kdm360.bridalweb.model.User.GENDER;
import com.kdm360.bridalweb.model.User.ROLE;
import com.kdm360.bridalweb.model.User.STATUS;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto {

	private Long userId;
    
    @Enumerated(EnumType.STRING)
    private ROLE role; 
    
    private String firstName; 
    private String lastName; 
    @Enumerated(EnumType.STRING)
    private GENDER gender; 
    private String email; 
    private String mobile;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private STATUS status; 
    private String createBy;
    private LocalDate createDate;
    private String updateBy;
    private LocalDate updateDate;
}