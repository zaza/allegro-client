package com.github.zaza.allegro;

import java.rmi.RemoteException;
import java.util.List;

import com.google.common.collect.Range;

public class SearchBuilder {

	private AllegroClient client;
	protected String string;
	protected int userId;
	protected int categoryId;
	private Boolean buyNew;
	private Integer priceFrom;
	private Integer priceTo;

	SearchBuilder(AllegroClient client) {
		this.client = client;
	}

	public SearchBuilder usedOnly() {
		this.buyNew = false;
		return this;
	}

	public SearchBuilder newOnly() {
		this.buyNew = true;
		return this;
	}

	public SearchBuilder priceFrom(int priceFrom) {
		this.priceFrom = priceFrom;
		return this;
	}

	public SearchBuilder priceTo(int priceTo) {
		this.priceTo = priceTo;
		return this;
	}

	private Range<Integer> getPriceRange() {
		if (priceFrom != null && priceTo != null)
			return Range.closed(priceFrom, priceTo);
		if (priceFrom != null && priceTo == null)
			return Range.atLeast(priceFrom);
		if (priceFrom == null && priceTo != null)
			return Range.atMost(priceTo);
		return null;
	}

	public List<Item> search() throws RemoteException {
		FilterOptionsBuilder optionsBuilder = FilterOptionsBuilder.search(string).userId(userId).category(categoryId);
		if (buyNew != null)
			optionsBuilder.condition(buyNew);
		optionsBuilder.price(getPriceRange());
		return client.search(optionsBuilder.build());
	}

}
