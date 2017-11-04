package com.github.zaza.allegro;

import java.rmi.RemoteException;
import java.util.List;

import com.google.common.collect.Range;

public class SearchBuilder {

	private AllegroClient client;
	protected String string;
	protected int userId;
	protected int categoryId;
	private Condition condition;
	private Integer priceFrom;
	private Integer priceTo;

	SearchBuilder(AllegroClient client) {
		this.client = client;
	}

	public SearchBuilder condition(Condition condition) {
		this.condition = condition;
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
		return client.search(FilterOptionsBuilder.search(string).userId(userId).category(categoryId)
				.condition(condition).price(getPriceRange()).build());
	}

}
