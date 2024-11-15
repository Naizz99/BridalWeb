package com.kdm360.bridalweb.dto;

import lombok.Data;

@Data
public class PasswordDto {
    
    private Long userId; 
    private String username; 
    private String currentPassword; 
    private String newPassword; 
    private String newPassword2;
    private String otp;
}