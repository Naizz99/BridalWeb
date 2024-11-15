package com.kdm360.bridalweb.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "blog_post")
@AllArgsConstructor
@NoArgsConstructor
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogPostId;
    
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
    
    private String image;
	
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    
    private boolean active;
}