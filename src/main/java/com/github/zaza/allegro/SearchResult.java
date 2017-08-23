package com.github.zaza.allegro;

import java.util.List;

public class SearchResult {

	private String filterDescription;
	private List<Item> items;

	SearchResult(String description, List<Item> items) {
		this.filterDescription = description;
		this.items = items;
	}

	public String getFilterDescription() {
		return filterDescription;
	}

	public List<Item> getItems() {
		return items;
	}

}
