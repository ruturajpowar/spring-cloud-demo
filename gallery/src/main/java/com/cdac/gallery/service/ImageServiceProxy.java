package com.cdac.gallery.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cdac.image.entity.*;

@FeignClient(name="image-service")
public interface ImageServiceProxy {

	@RequestMapping("/images")
	public List<Image> findAll();
}
