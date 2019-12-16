package com.eatza.orderingservice.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.eatza.order.controller.JwtAuthenticationController;
import com.eatza.order.dto.UserDto;
import com.eatza.order.service.authenticationservice.JwtAuthenticationService;

@RunWith(MockitoJUnitRunner.class)
public class JwtControllerTest {

	@InjectMocks
	private JwtAuthenticationController jwtAuthController;

	@Mock
	private JwtAuthenticationService jwtAuthService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	private UserDto getLoginCredentials() {
		UserDto userDto = new UserDto();
		userDto.setPassword("password");
		userDto.setUsername("root");
		return userDto;
	}

	@Test
	public void loginTest() throws Exception {
		String token = "t0k3n";
		Mockito.when(jwtAuthService.authenticateUser(Mockito.any(UserDto.class))).thenReturn(token);
		assertEquals(token, jwtAuthController.login(getLoginCredentials()).getBody());
	}
}
