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
@Table(name = "blog_like")
@AllArgsConstructor
@NoArgsConstructor
public class BlogLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogLikeId;
        
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    
    private LocalDateTime likedDate;
    
    @ManyToOne
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPostId;
}