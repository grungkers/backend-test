package com.tribehired.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tribehired.model.response.TopPostResponse;
import com.tribehired.service.PostService;

@RequestMapping("/posts")
@RestController
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/top")
	public List<TopPostResponse> getTopPostsByNumberOfComments() {
		
    	return postService.getTopPostsByNumberOfComments();
    }
}
