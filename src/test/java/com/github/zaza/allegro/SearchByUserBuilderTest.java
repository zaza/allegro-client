package com.github.zaza.allegro;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchByUserBuilderTest extends SearchBuilderTest {
	
	@BeforeClass
	public static void executeTestSearch() throws Exception {
		executeTestSearchIfNeeded();
	}
	
	@Test
	public void byUser() throws Exception {
		List<Item> items = client().searchByUser(getUserIdUnderTest()).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == getUserIdUnderTest()));
	}

	@Test
	public void byUserAndCategory() throws Exception {
		int categoryId = allItems.iterator().next().getCategoryId();
		List<Item> items = client().searchByUser(getUserIdUnderTest()).categoryId(categoryId).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == getUserIdUnderTest()));
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
	}

	private int getUserIdUnderTest() {
		return allItems.iterator().next().getSellerInfo().getUserId();
	}
}
