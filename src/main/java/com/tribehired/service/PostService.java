package com.tribehired.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.tribehired.model.entities.Comment;
import com.tribehired.model.entities.Post;
import com.tribehired.model.response.TopPostResponse;
import com.tribehired.util.Config;

@Component
public class PostService {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	Config config;
	@Autowired
	CommentService commentService;
	
	public static final String ALL_POSTS_PATH = "/posts";
	
	public List<TopPostResponse> getTopPostsByNumberOfComments(){
		List<Post> posts = null;
		List<Comment> comments = null;
		//Request get all posts and comments concurrently to get better performance
		List<CompletableFuture> listFutures = Arrays.asList(getAllPosts().toCompletableFuture(), commentService.getAllComments().toCompletableFuture());
		CompletableFuture<Void> combinedAllPostAndComments = CompletableFuture.allOf(listFutures.toArray(new CompletableFuture[listFutures.size()]));
		CompletableFuture<List<Object>> allCompletableFuture = combinedAllPostAndComments.thenApply(future -> listFutures.stream()
                .map(completableFuture -> completableFuture.join())
                .collect(Collectors.toList()));
		
		//Retrieve list of posts and comments
		List<Object> objects = allCompletableFuture.toCompletableFuture().join();
		
		for(Object obj:objects) {
			if(obj instanceof Post[]) {
				posts = Arrays.stream((Post[]) obj).collect(Collectors.toList());
			}else if(obj instanceof Comment[]) {
				comments = Arrays.stream((Comment[]) obj).collect(Collectors.toList());
			}
		}
		
		if(CollectionUtils.isEmpty(posts) || CollectionUtils.isEmpty(comments)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resouce not found");
		}
		
		//Map post to number of comments
		Map<Integer, Integer> mapPostIdToNumberOfComments = new HashMap<>();
		for(Comment comment:comments) {
			mapPostIdToNumberOfComments.merge(comment.getPostId(), 1, Integer::sum);
		}
		
		List<TopPostResponse> resp = posts.stream()
				//Convert Post element to TopPostResponse
				.map(post -> new TopPostResponse(post.getId(), post.getTitle(), post.getBody(), mapPostIdToNumberOfComments.get(post.getId())))
				//Sort descending while converting elements
				.sorted((topComment1, topComment2) -> topComment1.getTotalNumberOfComments().compareTo(topComment2.getTotalNumberOfComments()))
				//Get the final list
				.collect(Collectors.toList());
	    
		return resp;
	}
	
	public CompletionStage<Post[]> getAllPosts(){
		return CompletableFuture.supplyAsync(() -> {
			return restTemplate.getForObject(config.getApiEndpoint() + ALL_POSTS_PATH, Post[].class);
		});
	}
	
}
