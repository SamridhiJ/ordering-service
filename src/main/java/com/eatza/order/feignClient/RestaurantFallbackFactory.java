package com.eatza.order.feignClient;

import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class RestaurantFallbackFactory implements FallbackFactory<RestaurantFeignClient> {

	@Override
	public RestaurantFeignClient create(Throwable cause) {
		return new RestaurantFallback(cause);
	}

}
