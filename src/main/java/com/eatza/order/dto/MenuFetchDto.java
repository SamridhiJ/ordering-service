package com.eatza.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuFetchDto {

	private Long id;
	private String activeFrom;
	private String activeTill;
	@JsonProperty("restaurant")
	private RestaurantFetchDto restaurant;

	public MenuFetchDto(Long id, String activeFrom, String activeTill, RestaurantFetchDto restaurant) {
		super();
		this.id = id;
		this.activeFrom = activeFrom;
		this.activeTill = activeTill;
		this.restaurant = restaurant;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getActiveFrom() {
//		return activeFrom;
//	}
//
//	public void setActiveFrom(String activeFrom) {
//		this.activeFrom = activeFrom;
//	}
//
//	public String getActiveTill() {
//		return activeTill;
//	}
//
//	public void setActiveTill(String activeTill) {
//		this.activeTill = activeTill;
//	}
//
//	public RestaurantFetchDto getRestaurant() {
//		return restaurant;
//	}
//
//	public void setRestaurant(RestaurantFetchDto restaurant) {
//		this.restaurant = restaurant;
//	}
//
//	public MenuFetchDto() {
//	}

}
