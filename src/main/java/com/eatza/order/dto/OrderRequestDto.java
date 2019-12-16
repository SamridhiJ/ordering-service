package com.eatza.order.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

	private Long customerId;
	private Long restaurantId;
	private List<OrderedItemsDto> items;

//	public Long getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Long customerId) {
//		this.customerId = customerId;
//	}
//
//	public Long getRestaurantId() {
//		return restaurantId;
//	}
//
//	public void setRestaurantId(Long restaurantId) {
//		this.restaurantId = restaurantId;
//	}
//
//	public List<OrderedItemsDto> getItems() {
//		return items;
//	}
//
//	public void setItems(List<OrderedItemsDto> items) {
//		this.items = items;
//	}
//
//	public OrderRequestDto() {
//	}

	public OrderRequestDto(Long customerId, Long restaurantId, List<OrderedItemsDto> items) {
		super();
		this.customerId = customerId;
		this.restaurantId = restaurantId;
		this.items = items;
	}

}
