package com.kdm360.bridalweb.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "faq")
@AllArgsConstructor
@NoArgsConstructor
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;
    
    private String question;
    private String answer;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    private boolean isActive;
}