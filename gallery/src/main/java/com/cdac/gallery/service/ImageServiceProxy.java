package com.cdac.gallery.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cdac.common.security.Image;

@FeignClient(name="image-service")
public interface ImageServiceProxy {

	@RequestMapping("/images/{id}")
	public List<Image> findAll(@PathVariable(value="id") Integer id);
}
