package com.kdm360.bridalweb.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    public enum ROLE {ADMIN, CUSTOMER, VENDOR}
    public enum GENDER {MALE, FEMALE}
    public enum STATUS {ACTIVE, SUSPEND, DEACTIVE}
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String tempOTP;
}
