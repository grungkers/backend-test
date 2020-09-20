package com.tribehired.model.request;

import java.util.function.Predicate;

import com.tribehired.model.entities.Comment;
import com.tribehired.util.Filterable;

/**
 * Requirement is to filter the comments based on all available fields, 
 * thus can assume this request fields the same as Comment's
 * @author Agung Nugroho
 */

public class SearchCommentsRequest extends Comment implements Filterable<Comment>{

	@Override
	public Predicate<Comment> getFilter() {
		return filter -> 
		//Ignore if null, otherwise match exact id
		(this.getId() != null ? this.getId().intValue() == filter.getId().intValue() : true) &&
		//Ignore if null, otherwise match exact email
		(this.getEmail() != null ? this.getEmail().equals(filter.getEmail()) : true) &&
		//Ignore if null, otherwise match exact name
		(this.getName() != null ? this.getName().equals(filter.getName()) : true) &&
		//Ignore if null, otherwise match exact post id
		(this.getPostId() != null ? this.getPostId().intValue() == filter.getPostId().intValue() : true) &&
		//Ignore if null, otherwise match when containing substring
		(this.getBody() != null ? filter.getBody().contains(this.getBody()) : true);
	}
	
}
