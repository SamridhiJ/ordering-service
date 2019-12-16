package com.eatza.orderingservice.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.eatza.order.config.SwaggerConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class SwaggerDocketConfigurationTest {

	@InjectMocks
	private SwaggerConfiguration swaggerConfig;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void docketTest() {
		assertNotNull(swaggerConfig.api());
	}

	@Test
	public void uiconfigurationTest() {
		assertNotNull(swaggerConfig.uiConfig());
	}

}
