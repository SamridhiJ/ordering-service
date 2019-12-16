package com.eatza.orderingservice.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.eatza.order.config.RestTemplateClient;

@RunWith(MockitoJUnitRunner.class)
public class RestTemplateConfigurationTest {

	@InjectMocks
	private RestTemplateClient restTemplateClient;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void restTemplateTest() {
		assertNotNull(restTemplateClient.restTemplate());
	}

}
