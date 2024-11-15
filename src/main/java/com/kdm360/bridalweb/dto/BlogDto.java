package com.kdm360.bridalweb.dto;

import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class BlogDto {

    private Long blogPostId;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
    private MultipartFile image;
    private boolean active;
    
	private Long userId;
	
	private String imagePath;
	private BlogStatsDto blogStatus;
}