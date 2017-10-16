package com.github.zaza.allegro;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearchBuilderTest {

	private static final String TEST_STRING = "xbox one 500";

	private static List<Item> allItems;

	// TODO: move to TestableClient?
	@BeforeClass
	public static void executeTestSearch() throws Exception {
		allItems = client().searchByString(TEST_STRING).search();
		assertFalse("Expected test search to return items", allItems.isEmpty());
		assertAtLeastTwoUsers();
		assertAtLeastTwoCategories();
		assertAtLeastThreeDifferentPrices();
		assertUsedAndNewItemsPresent();
	}

	private static void assertAtLeastTwoUsers() {
		assertThat(allItems.stream().mapToInt(i -> i.getSellerInfo().getUserId()).distinct().count())
				.isGreaterThanOrEqualTo(2);
	}

	private static void assertAtLeastTwoCategories() {
		assertThat(allItems.stream().mapToInt(i -> i.getCategoryId()).distinct().count()).isGreaterThanOrEqualTo(2);
	}

	private static void assertAtLeastThreeDifferentPrices() {
		// TODO: items can have multiple prices
		assertThat(allItems.stream().mapToDouble(i -> i.getPriceInfo().getItem(0).getPriceValue()).distinct().count())
				.isGreaterThanOrEqualTo(3);
	}

	private static void assertUsedAndNewItemsPresent() {
		boolean anyNew = allItems.stream().anyMatch(i -> i.isNew());
		boolean anyUsed = allItems.stream().anyMatch(i -> i.isUsed());
		assertTrue("Expected test search to return used and new items", anyNew && anyUsed);
	}

	@Test
	public void byString() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).search();

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
	}

	@Test
	public void byStringAndUsedOnly() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).usedOnly().search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.isUsed()));
	}

	@Test
	public void byStringAndNewOnly() throws Exception {
		List<Item> items = client().searchByString(TEST_STRING).newOnly().search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.isNew()));
	}

	@Test
	public void byUser() throws Exception {
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		List<Item> items = client().searchByUser(userId).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId));
	}

	@Test
	public void byCategory() throws Exception {
		int categoryId = allItems.iterator().next().getCategoryId();
		List<Item> items = client().searchByCategory(categoryId).search();

		assertFalse(items.isEmpty());
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
	}

	@Test
	public void byStringAndUser() throws Exception {
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		List<Item> items = client().searchByString(TEST_STRING).userId(userId).search();

		assertFalse(items.isEmpty());
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
		int categoryId = allItems.iterator().next().getCategoryId();
		int userId = allItems.iterator().next().getSellerInfo().getUserId();
		boolean isNew = true;
		List<Item> items = client().searchByString(TEST_STRING).categoryId(categoryId).userId(userId).newOnly()
				.search();
		// the selected user has no new items in the given category, try used only
		if (items.isEmpty()) {
			isNew = false;
			items = client().searchByString(TEST_STRING).categoryId(categoryId).userId(userId).usedOnly().search();
		}

		assertFalse(items.isEmpty());
		assertAllTitlesContainWordsFromSearchString(items);
		assertTrue(items.stream().allMatch(i -> i.getCategoryId() == categoryId));
		assertTrue(items.stream().allMatch(i -> i.getSellerInfo().getUserId() == userId));
		if (isNew)
			assertTrue(items.stream().allMatch(i -> i.isNew()));
		else
			assertTrue(items.stream().allMatch(i -> i.isUsed()));
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

	private static TestableAllegroClient client() throws ServiceException {
		return TestableAllegroClient.get();
	}

}
