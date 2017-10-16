package com.github.zaza.allegro;

public class SearchByCategoryBuilder extends SearchBuilder {

	public SearchByCategoryBuilder(AllegroClient client, int categoryId) {
		super(client);
		categoryId(categoryId);
	}
	
	public SearchBuilder string(String string) {
		this.string = string;
		return this;
	}

}
