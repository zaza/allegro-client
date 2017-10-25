package com.github.zaza.allegro;

public class SearchByCategoryBuilder extends SearchBuilder {

	public SearchByCategoryBuilder(AllegroClient client, int categoryId) {
		super(client);
		this.categoryId = categoryId;
	}
	
	public SearchBuilder string(String string) {
		this.string = string;
		return this;
	}
	
	public SearchBuilder userId(int userId) {
		this.userId = userId;
		return this;
	}

}
