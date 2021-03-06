package com.github.zaza.allegro;

import java.util.ArrayList;
import java.util.List;

import com.allegro.webapi.ArrayOfFilteroptionstype;
import com.allegro.webapi.ArrayOfString;
import com.allegro.webapi.FilterOptionsType;
import com.allegro.webapi.RangeValueType;
import com.google.common.collect.Range;

class FilterOptionsBuilder {

	private String query;
	private Range<Integer> price;
	private Condition condition;
	private int category;
	private int userId;

	private FilterOptionsBuilder(String query) {
		this.query = query;
	}

	static FilterOptionsBuilder search(String query) {
		return new FilterOptionsBuilder(query);
	}

	FilterOptionsBuilder price(Range<Integer> price) {
		this.price = price;
		return this;
	}

	FilterOptionsBuilder condition(Condition condition) {
		this.condition = condition;
		return this;
	}
	
	FilterOptionsBuilder category(int category) {
		this.category = category;
		return this;
	}
	
	FilterOptionsBuilder userId(int userId) {
		this.userId = userId;
		return this;
	}

	ArrayOfFilteroptionstype build() {
		ArrayOfFilteroptionstype filter = new ArrayOfFilteroptionstype();
		List<FilterOptionsType> subFilters = new ArrayList<>();
		if (query != null && !query.isEmpty())
			subFilters.add(newSubFilter(FilterId.Szukaj_w_tytule, query));
		if (condition != null)
			subFilters.add(newSubFilter(FilterId.Stan, condition.name().toLowerCase()));
		if (price != null)
			subFilters.add(newSubFilter(FilterId.Cena, price));
		if (category != 0)
			subFilters.add(newSubFilter(FilterId.Kategoria, Integer.toString(category)));
		if (userId != 0)
			subFilters.add(newSubFilter(FilterId.Identyfikator_uzytkownika, Integer.toString(userId)));
		filter.setItem(subFilters.toArray(new FilterOptionsType[subFilters.size()]));
		return filter;
	}

	private FilterOptionsType newSubFilter(String filterId, String... values) {
		FilterOptionsType subFilter = new FilterOptionsType();
		subFilter.setFilterId(filterId);
		ArrayOfString subFilterValues = new ArrayOfString();
		subFilterValues.setItem(values);
		subFilter.setFilterValueId(subFilterValues);
		return subFilter;
	}

	private FilterOptionsType newSubFilter(String filterId, Range<Integer> range) {
		FilterOptionsType subFilter = new FilterOptionsType();
		subFilter.setFilterId(filterId);
		RangeValueType valueRange = new RangeValueType();
		if (range.hasLowerBound())
			valueRange.setRangeValueMin(range.lowerEndpoint().toString());
		if (range.hasUpperBound())
			valueRange.setRangeValueMax(range.upperEndpoint().toString());
		subFilter.setFilterValueRange(valueRange);
		return subFilter;
	}

}
