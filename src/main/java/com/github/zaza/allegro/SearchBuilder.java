package com.github.zaza.allegro;

import java.rmi.RemoteException;
import java.util.List;

public class SearchBuilder {

	private AllegroClient client;
	protected String string;
	private int userId;
	private int categoryId;
	private Boolean buyNew;

	SearchBuilder(AllegroClient client) {
		this.client = client;
	}

	public SearchBuilder userId(int userId) {
		this.userId = userId;
		return this;
	}

	public SearchBuilder categoryId(int categoryId) {
		this.categoryId = categoryId;
		return this;
	}

	public SearchBuilder usedOnly() {
		this.buyNew = false;
		return this;
	}

	public SearchBuilder newOnly() {
		this.buyNew = true;
		return this;
	}

	public List<Item> search() throws RemoteException {
		FilterOptionsBuilder optionsBuilder = FilterOptionsBuilder.search(string).userId(userId).category(categoryId);
		if (buyNew != null)
			optionsBuilder.condition(buyNew);
		return client.search(optionsBuilder.build());
	}

}
