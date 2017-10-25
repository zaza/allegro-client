package com.github.zaza.allegro;

public class SearchByStringBuilder extends SearchBuilder {

	public SearchByStringBuilder(AllegroClient client, String string) {
		super(client);
		this.string = string;
	}
	
	public SearchByStringBuilder categoryId(int categoryId) {
		this.categoryId = categoryId;
		return this;
	}
	
	public SearchBuilder userId(int userId) {
		this.userId = userId;
		return this;
	}

}
