package com.kdm360.bridalweb.model;

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
@Table(name = "blog_view")
@AllArgsConstructor
@NoArgsConstructor
public class BlogView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;
    
    @ManyToOne
    @JoinColumn(name = "blogPostId")
    private BlogPost blogPostId;
    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User userId;
    
    private Integer viewDate;
    private Integer viewCount;
}