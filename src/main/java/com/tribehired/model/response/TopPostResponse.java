package com.tribehired.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopPostResponse {
	public TopPostResponse(Integer postId, String postTitle, String postBody, Integer totalNumberOfComments) {
		super();
		this.postId = postId;
		this.postTitle = postTitle;
		this.postBody = postBody;
		this.totalNumberOfComments = totalNumberOfComments;
	}
	@JsonProperty("post_id")
	private Integer postId;
	@JsonProperty("post_title")
	private String postTitle;
	@JsonProperty("post_body")
	private String postBody;
	@JsonProperty("total_number_of_comments")
	private Integer totalNumberOfComments;

	public Integer getTotalNumberOfComments() {
		return totalNumberOfComments;
	}
}
