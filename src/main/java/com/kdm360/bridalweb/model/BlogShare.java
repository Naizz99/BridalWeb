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
@Table(name = "blog_share")
@AllArgsConstructor
@NoArgsConstructor
public class BlogShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogShareId;
        
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    
    private LocalDateTime sharedDate;
    
    @ManyToOne
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPostId;
}