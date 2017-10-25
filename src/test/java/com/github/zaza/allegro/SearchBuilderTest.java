package com.github.zaza.allegro;

import java.util.List;

import javax.xml.rpc.ServiceException;

abstract class SearchBuilderTest {
	protected static List<Item> allItems;

	protected static void executeTestSearchIfNeeded() throws Exception {
		if (allItems == null)
			allItems = client().executeTestSearch();
	}

	protected static TestableAllegroClient client() throws ServiceException {
		return TestableAllegroClient.get();
	}
}
