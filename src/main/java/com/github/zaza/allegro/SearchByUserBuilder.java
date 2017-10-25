package com.github.zaza.allegro;

public class SearchByUserBuilder extends SearchBuilder {

	public SearchByUserBuilder(AllegroClient client, int userId) {
		super(client);
		this.userId = userId;
	}
	
	public SearchBuilder string(String string) {
		this.string = string;
		return this;
	}
	
	public SearchBuilder categoryId(int categoryId) {
		this.categoryId = categoryId;
		return this;
	}

}
