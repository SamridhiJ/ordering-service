package com.eatza.order.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eatza.order.dto.ItemFetchDto;

@FeignClient(name = "search-service", fallbackFactory = RestaurantFallbackFactory.class)
@Service
public interface RestaurantFeignClient {

	@GetMapping(value = "/item/id/{id}")
	public ItemFetchDto getItemDto(@PathVariable long id);

}
