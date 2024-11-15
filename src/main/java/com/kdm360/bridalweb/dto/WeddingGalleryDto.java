package com.kdm360.bridalweb.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WeddingGalleryDto {

	private Long weddingGalleryId;
	private List<MultipartFile> image;
	private Long weddingId;
}