package com.github.zaza.allegro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.allegro.webapi.CatInfoType;
import com.allegro.webapi.CountryInfoType;
import com.allegro.webapi.DoGetCatsDataRequest;
import com.allegro.webapi.DoGetCountriesRequest;
import com.allegro.webapi.DoGetStatesInfoRequest;
import com.allegro.webapi.ServicePort_PortType;
import com.allegro.webapi.StateInfoStruct;

class TestableAllegroClient {
	static final String TEST_STRING = "xbox one 500";
	private AllegroClient delegate;

	private TestableAllegroClient(AllegroClient client) {
		this.delegate = client;
	}

	static TestableAllegroClient get() {
		return new TestableAllegroClient(
				AllegroClientProvider.getInstance().getClient(System.getenv().get(Env.ALLEGRO_WEBAPI_KEY)));
	}

	long getCountryCode(String countryName) throws RemoteException, ServiceException {
		List<CountryInfoType> countries = Arrays
				.asList(allegro().doGetCountries(new DoGetCountriesRequest(AllegroClient.POLAND, webApiKey()))
						.getCountryArray().getItem());
		return countries.stream().filter(c -> countryName.equalsIgnoreCase(c.getCountryName()))
				.mapToInt(c -> c.getCountryId()).findFirst().getAsInt();
	}

	int getCategoryId(String categoryName) throws RemoteException, ServiceException {
		// TODO: move to AllegroClient, make API
		List<CatInfoType> categories = Arrays
				.asList(allegro().doGetCatsData(new DoGetCatsDataRequest(AllegroClient.POLAND, 0L, webApiKey(), false))
						.getCatsList().getItem());
		return categories.stream().filter(c -> categoryName.equalsIgnoreCase(c.getCatName()))
				.mapToInt(c -> c.getCatId()).findFirst().getAsInt();
	}

	String getState(int stateId) throws RemoteException, ServiceException {
		List<StateInfoStruct> states = Arrays
				.asList(allegro().doGetStatesInfo(new DoGetStatesInfoRequest(AllegroClient.POLAND, webApiKey()))
						.getStatesInfoArray().getItem());
		return states.stream().filter(s -> s.getStateId() == stateId).map(s -> s.getStateName()).findFirst()
				.orElse(State.nieokreślone.name());
	}

	List<Item> executeTestSearch() throws RemoteException {
		List<Item> items = searchByString(TEST_STRING).search();
		assertFalse("Expected test search to return items", items.isEmpty());
		assertAtLeastTwoUsers(items);
		assertAtLeastTwoCategories(items);
		assertAtLeastThreeDifferentPrices(items);
		assertUsedAndNewItemsPresent(items);
		return items;
	}

	private void assertAtLeastTwoUsers(List<Item> items) {
		assertThat(items.stream().mapToInt(i -> i.getSellerInfo().getUserId()).distinct().count())
				.isGreaterThanOrEqualTo(2);
	}

	private static void assertAtLeastTwoCategories(List<Item> items) {
		assertThat(items.stream().mapToInt(i -> i.getCategoryId()).distinct().count()).isGreaterThanOrEqualTo(2);
	}

	private static void assertAtLeastThreeDifferentPrices(List<Item> items) {
		assertThat(items.stream().mapToDouble(i -> i.lowestPrice()).distinct().count()).isGreaterThanOrEqualTo(3);
	}

	private static void assertUsedAndNewItemsPresent(List<Item> items) {
		boolean anyNew = items.stream().anyMatch(i -> i.isNew());
		boolean anyUsed = items.stream().anyMatch(i -> i.isUsed());
		assertTrue("Expected test search to return used and new items", anyNew && anyUsed);
	}

	public SearchByStringBuilder searchByString(String string) throws RemoteException {
		return delegate.searchByString(string);
	}

	public SearchByCategoryBuilder searchByCategory(int categoryId) throws RemoteException {
		return delegate.searchByCategory(categoryId);
	}

	public SearchByUserBuilder searchByUser(int userId) throws RemoteException {
		return delegate.searchByUser(userId);
	}

	long getLatestVersionKey() throws RemoteException {
		return delegate.getLatestVersionKey();
	}

	long getVersionKey() throws RemoteException {
		return delegate.getVersionKey();
	}

	private ServicePort_PortType allegro() {
		return delegate.allegro;
	}

	private String webApiKey() {
		return delegate.webApiKey;
	}

}
