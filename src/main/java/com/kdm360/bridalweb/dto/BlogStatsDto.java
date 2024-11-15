package com.kdm360.bridalweb.dto;

import lombok.Data;

@Data
public class BlogStatsDto {

    private Long blogPostId;
    private String title;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Long viewCount;

    public BlogStatsDto(Long blogPostId, String title, Long likeCount, Long commentCount, Long shareCount, Long viewCount) {
        this.blogPostId = blogPostId;
        this.title = title;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.viewCount = viewCount;
    }

}

