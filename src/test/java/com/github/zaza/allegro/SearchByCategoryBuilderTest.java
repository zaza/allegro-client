package com.github.zaza.allegro;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchByCategoryBuilderTest extends SearchBuilderTest {
	
	@BeforeClass
	public static void executeTestSearch() throws Exception {
		executeTestSearchIfNeeded();
	}
	
	@Test
	public void byCategory() throws Exception {
		List<Item> items = client().searchByCategory(getCategoryIdUnderTest()).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == getCategoryIdUnderTest()));
	}
	
	@Test
	public void byCategoryAndUsedOnly() throws Exception {
		List<Item> items = client().searchByCategory(getCategoryIdUnderTest()).usedOnly().search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.isUsed()));
	}
	
	@Test
	public void byCategoryAndUser() throws Exception {
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		List<Item> items = client().searchByCategory(getCategoryIdUnderTest()).userId(userId).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == getCategoryIdUnderTest()));
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId));
	}
	
	private int getCategoryIdUnderTest() {
		return allItems.iterator().next().getCategoryId();
	}
	
}
