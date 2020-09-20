package com.tribehired.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.tribehired.model.entities.Comment;
import com.tribehired.model.request.SearchCommentsRequest;
import com.tribehired.util.Config;

@Component
public class CommentService {
	public static final String ALL_COMMENTS_PATH = "/comments";
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	Config config;
	
	public List<Comment> getPostWithFilter(SearchCommentsRequest request){
		return getAllComments().thenApply(comments -> {
			//Convert array to list of comments
			List<Comment> listComments = Arrays.stream((Comment[]) comments).collect(Collectors.toList());
			//Filter the comments based on what we implemented in the search request class
			return listComments.stream().filter(request.getFilter()).collect(Collectors.toList());
		}).toCompletableFuture().join();
	}
	
	public CompletionStage<Comment[]> getAllComments(){
		return CompletableFuture.supplyAsync(() -> {
			return restTemplate.getForObject(config.getApiEndpoint() + ALL_COMMENTS_PATH, Comment[].class);
		});
	}

}
