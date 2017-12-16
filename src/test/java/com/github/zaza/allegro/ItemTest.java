package com.github.zaza.allegro;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.rpc.ServiceException;

import org.junit.Test;

public class ItemTest {

	@Test
	public void getPhotosIsNeverNull() throws Exception {
		List<Item> items = client().searchByString("xbox one x").search();

		// for items without a photo return an empty list, not null
		assertTrue(items.stream().allMatch(i -> i.getPhotos() != null));
	}

	private TestableAllegroClient client() throws ServiceException {
		return TestableAllegroClient.get();
	}

}
