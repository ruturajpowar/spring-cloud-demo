package com.cdac.gallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cdac.gallery.entity.Gallery;
import com.cdac.gallery.service.ImageServiceProxy;
import com.cdac.image.entity.Image;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class FeignImageServiceController {
	
	@Autowired
	ImageServiceProxy imageServiceProxy;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Environment env;
	
	@RequestMapping("/")
	public String home() {
		
		return "hello from gallery service running at port "+env.getProperty("local.server.port");
	}
	
	@HystrixCommand(fallbackMethod="fallback")
	@RequestMapping("/{id}")
	public Gallery getGallery(@PathVariable final int id) {
		
		Gallery gallery=new Gallery();
		gallery.setId(id);
		
		@SuppressWarnings("unchecked")    // we'll throw an exception from image service to simulate a failure
		List<Image> images=imageServiceProxy.findAll();
		gallery.setImages(images);
		return gallery;
	}
	
	@RequestMapping("/admin")
	public String homeAdmin() {
		
		return "This is admin area of gallery service running at port: "+env.getProperty("local.server.port");
	}
	

	// a fallback method to be called if failure happened
	public Gallery fallback(int galleryId, Throwable hystrixCommand) {
		return new Gallery(galleryId);
	}

}
