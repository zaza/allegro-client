package com.github.zaza.allegro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class AllegroClientTest {

	@Test
	@Ignore
	public void latestVersionKeyCheck() throws Exception {
		// The key changes frequently, resulting in the test to fail, see:
		// http://allegro.pl/webapi/general.php#version_keys
		// http://allegro.pl/webapi/faq.php#faq_3
		assertEquals(AllegroClient.WEBAPI_VERSION_KEY, client().getLatestVersionKey());
	}

	@Test
	public void versionKeyCheck() throws Exception {
		assertEquals(client().getVersionKey(), client().getLatestVersionKey());
	}

	@Test
	public void countryCodeCheck() throws Exception {
		assertEquals(AllegroClient.POLAND, client().getCountryCode("Polska"));
	}

	@Test
	public void getCategoryIdForSportyWalki() throws Exception {
		assertCategory(13522, "Sporty walki");
	}

	@Test
	public void getCategoryIdForSportITurystyka() throws Exception {
		assertCategory(3919, "Sport i Turystyka");
	}

	// FIXME: temporarily increased timeout to 20sec, but it should take just
	// few seconds to complete
	// see https://github.com/zaza/allegro-rss/issues/5
	@Test(timeout = 20000)
	public void searchWithLocationIsFastForLargeResultSets() throws Exception {
		List<Item> items = client().searchByString("cmax błotnik prawy przod").search();

		assertTrue(items.size() > 100);
		assertTrue(items.stream().anyMatch(i -> !i.getLocation().isEmpty()));
	}

	@Test
	public void searchByUser() throws RemoteException, ServiceException {
		List<Item> itemsByString = client().searchByString("mata judo").search();
		Item itemByString = Iterables.getLast(itemsByString);

		List<Item> itemsByUser = client().searchByUser(itemByString.getSellerInfo().getUserId()).search();

		assertThat(itemsByUser).contains(itemByString);
	}

	@Test
	public void searchByExactPrice() throws RemoteException, ServiceException {
		List<Item> itemsByString = client().searchByString(TestableAllegroClient.TEST_STRING).search();
		Item itemWithIntegerPrice = itemsByString.stream()
				.filter(i -> i.lowestPrice() == Double.valueOf(i.lowestPrice()).intValue()).findAny().get();
		int price = Double.valueOf(itemWithIntegerPrice.lowestPrice()).intValue();

		List<Item> itemsByExactPrice = client().searchByString(TestableAllegroClient.TEST_STRING).priceFrom(price)
				.priceTo(price).search();

		assertThat(itemsByExactPrice).allMatch(i -> i.lowestPrice() == price);
	}

	@Test
	@Ignore
	// FIXME: user rating is always 0
	// https://github.com/zaza/allegro-client/issues/1
	public void anySellerHasPositiveRating() throws Exception {
		assertTrue(client().executeTestSearch().stream()
				.anyMatch(i -> i.getSellerInfo().getUserRating() > 0));
	}

	private void assertCategory(int id, String name) throws RemoteException, ServiceException {
		assertEquals(id, client().getCategoryId(name));
	}

	private TestableAllegroClient client() throws ServiceException {
		return TestableAllegroClient.get();
	}

}
