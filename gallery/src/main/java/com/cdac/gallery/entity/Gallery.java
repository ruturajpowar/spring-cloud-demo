package com.cdac.gallery.entity;

import java.util.List;

import com.cdac.common.security.Image;

public class Gallery {
	
	int id;
	
	List<Image> images;
	
	public Gallery() {
		
	}

	public Gallery(int galleryId) {
		// TODO Auto-generated constructor stub
		this.id = galleryId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	
}
