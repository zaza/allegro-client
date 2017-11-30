package com.github.zaza.allegro;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Calendar;

import com.allegro.webapi.ArrayOfPhotoinfotype;
import com.allegro.webapi.ArrayOfPriceinfotype;
import com.allegro.webapi.ItemInfo;
import com.allegro.webapi.ItemsListType;
import com.allegro.webapi.UserInfoType;
import com.google.common.base.Objects;

public class Item {

	private ItemsListType itemsListType;

	private ItemInfo itemInfo;

	public Item(ItemsListType itemsListType, ItemInfo itemInfo) {
		this.itemsListType = itemsListType;
		this.itemInfo = itemInfo;
	}

	public String getItemTitle() {
		return itemsListType.getItemTitle();
	}

	public long getItemId() {
		return itemsListType.getItemId();
	}

	public UserInfoType getSellerInfo() {
		return itemsListType.getSellerInfo();
	}

	public ArrayOfPriceinfotype getPriceInfo() {
		return itemsListType.getPriceInfo();
	}

	public String getTimeToEnd() {
		return itemsListType.getTimeToEnd();
	}

	public Calendar getEndingTime() {
		return itemsListType.getEndingTime();
	}

	public ArrayOfPhotoinfotype getPhotosInfo() {
		return itemsListType.getPhotosInfo();
	}

	int getCategoryId() {
		return itemsListType.getCategoryId();
	}

	public boolean is(Condition condition) {
		return Condition.valueOf(itemsListType.getConditionInfo().toUpperCase()) == condition;
	}

	public boolean isNew() {
		return is(Condition.NEW);
	}

	public boolean isUsed() {
		return !isNew();
	}

	public String getLocation() {
		return itemInfo.getItLocation();
	}

	public String getState() {
		return State.valueOf(itemInfo.getItState()).name();
	}

	public double lowestPrice() {
		return Arrays.asList(getPriceInfo().getItem()).stream().mapToDouble(p -> p.getPriceValue()).min().getAsDouble();
	}

	public double highestPrice() {
		return Arrays.asList(getPriceInfo().getItem()).stream().mapToDouble(p -> p.getPriceValue()).max().getAsDouble();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Item other = (Item) obj;
		return Objects.equal(this.itemsListType, other.itemsListType) && Objects.equal(this.itemInfo, other.itemInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(itemsListType, itemInfo);
	}

	@Override
	public String toString() {
		return format("'%s' (%s)", getItemTitle(), getItemId());
	}
}
