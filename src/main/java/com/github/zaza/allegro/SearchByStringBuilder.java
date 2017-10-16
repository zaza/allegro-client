package com.github.zaza.allegro;

public class SearchByStringBuilder extends SearchBuilder {

	public SearchByStringBuilder(AllegroClient client, String string) {
		super(client);
		this.string = string;
	}

}
