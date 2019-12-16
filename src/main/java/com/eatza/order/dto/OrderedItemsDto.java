package com.eatza.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderedItemsDto {

	private long itemId;
	private int quantity;

	public OrderedItemsDto(long itemId, int quantity) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
	}

//	public long getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(long itemId) {
//		this.itemId = itemId;
//	}
//
//	public int getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}
//
//	public OrderedItemsDto() {
//	}

}
