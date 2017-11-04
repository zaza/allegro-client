package com.github.zaza.allegro;

import static com.github.zaza.allegro.TestableAllegroClient.TEST_STRING;
import static java.lang.String.format;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;
import java.util.PrimitiveIterator.OfInt;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchByStringBuilderTest extends SearchBuilderTest {

	@BeforeClass
	public static void executeTestSearch() throws Exception {
		executeTestSearchIfNeeded();
	}
	
	@Test
	public void byString() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
	}

	@Test
	public void byStringAndUsedOnly() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).condition(Condition.USED).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.isUsed()));
	}

	@Test
	public void byStringAndNewOnly() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).condition(Condition.NEW).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.isNew()));
	}

	@Test
	public void byStringAndBothConditions() throws Exception {
		// the last criteria used overwrites those previously set
		List<Item> items = client().searchByString(TEST_STRING).condition(Condition.NEW).condition(Condition.USED).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().noneMatch(i -> i.isNew()));
		assertTrue(items.stream().allMatch(i -> i.isUsed()));
	}

	@Test
	public void byStringAndPriceFrom() throws Exception {
		OfDouble sortedPrices = allItems.stream().mapToDouble(i -> i.lowestPrice()).distinct().sorted().iterator();
		sortedPrices.next();
		int price = sortedPrices.next().intValue();

		List<Item> items = client().searchByString(TEST_STRING).priceFrom(price).search();

		assertTrue(items.size() < allItems.size());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.lowestPrice() >= price));
	}

	@Test
	public void byStringAndPriceTo() throws Exception {
		OfDouble sortedPrices = allItems.stream().mapToDouble(i -> i.highestPrice()).distinct().sorted().iterator();
		sortedPrices.next();
		int price = sortedPrices.next().intValue();

		List<Item> items = client().searchByString(TEST_STRING).priceTo(price).search();

		assertTrue(items.size() < allItems.size());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.highestPrice() <= price));
	}

	@Test
	public void byStringAndPriceRange() throws Exception {
		OfDouble sortedPrices = allItems.stream().mapToDouble(i -> i.lowestPrice()).distinct().sorted().iterator();
		sortedPrices.next();
		int priceFrom = sortedPrices.next().intValue();
		int priceTo = sortedPrices.next().intValue();

		List<Item> items = client().searchByString(TEST_STRING).priceFrom(priceFrom).priceTo(priceTo).search();

		assertTrue(items.size() < allItems.size());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.lowestPrice() >= priceFrom));
		assertTrue(items.stream().allMatch(i -> i.lowestPrice() <= priceTo));
	}

	@Test
	public void byStringAndUser() throws Exception {
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		List<Item> items = client().searchByString(TEST_STRING).userId(userId).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId));
	}

	@Test
	public void byStringAndCategory() throws Exception {
		int categoryId = allItems.iterator().next().getCategoryId();
		List<Item> items = client().searchByString(TEST_STRING).categoryId(categoryId).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
	}

	@Test
	public void byStringAndCategoryAndUserId() throws Exception {
		int categoryId = allItems.iterator().next().getCategoryId();
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		List<Item> items = client().searchByString(TEST_STRING).categoryId(categoryId).userId(userId).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId));
	}

	@Test
	public void byStringAndCategoryAndUserIdAndNewOnly() throws Exception {
		Iterator<Item> iterator = allItems.iterator();
		int categoryId = iterator.next().getCategoryId();
		OfInt userIds = allItems.stream().mapToInt(i -> i.getSellerInfo().getUserId()).distinct().iterator();
		final int[] userId = {0};
		List<Item> items = new ArrayList<>();
		while  (items.isEmpty()) {
			userId[0] = userIds.next();
			items = client().searchByString(TEST_STRING).categoryId(categoryId).userId(userId[0]).condition(Condition.NEW)
					.search();
		}
			
		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId[0]));
		assertTrue(items.stream().allMatch(i -> i.isNew()));
	}

	private static void assertAllTitlesContainWordsFromSearchString(List<Item> items) {
		Arrays.asList(TEST_STRING.split(" ")).stream().forEach(word -> assertAllTitlesContain(items, word));
	}

	private static void assertAllTitlesContain(List<Item> items, String string) {
		for (Item item : items) {
			assertTrue(format("Expected all titles to contain '%s', but found '%s'", string, item.getItemTitle()),
					item.getItemTitle().toLowerCase().contains(string));
		}
	}

}
