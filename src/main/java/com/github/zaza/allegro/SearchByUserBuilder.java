package com.github.zaza.allegro;

public class SearchByUserBuilder extends SearchBuilder {

	public SearchByUserBuilder(AllegroClient client, int userId) {
		super(client);
		userId(userId);
	}
	
	public SearchBuilder string(String string) {
		this.string = string;
		return this;
	}

}
