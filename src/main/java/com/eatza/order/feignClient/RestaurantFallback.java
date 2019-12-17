package com.eatza.order.feignClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eatza.order.dto.ItemFetchDto;

import feign.FeignException;

public class RestaurantFallback implements RestaurantFeignClient {
	private final Throwable cause;
	Logger logger = LogManager.getLogger(RestaurantFallback.class);

	public RestaurantFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public ItemFetchDto getItemDto(long id) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			// Treat the HTTP 404 status
			logger.error("404 not found in the restaurant service");
		}
		return null;
	}

}