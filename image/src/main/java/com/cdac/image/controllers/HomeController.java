package com.cdac.image.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.common.security.Image;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping("/images/{id}")
	public List<Image> getImages(@PathVariable("id") int id) throws Exception{
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+id);
		throw new Exception("Images can't be fetched");
//		List<Image> images=Arrays.asList(
//				new Image(1, "Treehouse of Horror V", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3842005760"),
//				new Image(2, "The Town", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3698134272"),
//				new Image(3, "The Last Traction Hero", "https://www.imdb.com/title/tt0096697/mediaviewer/rm1445594112"));
//			return images;
		}

}
