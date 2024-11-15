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
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String messageContent;
    private String reply;
    private LocalDateTime submittedDate;
    private LocalDateTime repliedDate;
    
    private boolean isRead;
}