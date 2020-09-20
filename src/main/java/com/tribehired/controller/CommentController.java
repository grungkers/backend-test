package com.tribehired.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tribehired.model.entities.Comment;
import com.tribehired.model.request.SearchCommentsRequest;
import com.tribehired.service.CommentService;

@RequestMapping("/comments")
@RestController
public class CommentController {
	
	@Autowired
	CommentService commentService; 
	
	@GetMapping("/search")
	public List<Comment> getTopPostsByNumberOfComments(@RequestBody SearchCommentsRequest request) {
    	return commentService.getPostWithFilter(request);
    }
}
