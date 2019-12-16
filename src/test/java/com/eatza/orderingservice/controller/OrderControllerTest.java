package com.eatza.orderingservice.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.eatza.order.controller.OrderController;
import com.eatza.order.dto.OrderRequestDto;
import com.eatza.order.dto.OrderUpdateDto;
import com.eatza.order.dto.OrderUpdateResponseDto;
import com.eatza.order.dto.OrderedItemsDto;
import com.eatza.order.exception.OrderException;
import com.eatza.order.model.Order;
import com.eatza.order.model.OrderedItem;
import com.eatza.order.service.orderservice.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	@InjectMocks
	private OrderController orderController;

	@Mock
	private OrderService orderService;

	private static final String AUTHORIZATION = "t0k3n";
	private static final Long ORDER_ID = 1L;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void placeOrderTest() throws OrderException {

		OrderRequestDto orderRequestDto = getOrderRequest();
		Order order = new Order(1L, "CREATED", 1L);
		Mockito.when(orderService.placeOrder(Mockito.any(OrderRequestDto.class))).thenReturn(order);
		assertEquals(order, orderController.placeOrder(AUTHORIZATION, orderRequestDto).getBody());
	}

	@Test
	public void cancelOrderTest() throws OrderException {
		Mockito.when(orderService.cancelOrder(Mockito.anyLong())).thenReturn(true);
		assertEquals("Order Cancelled Successfully", orderController.cancel(AUTHORIZATION, ORDER_ID).getBody());
	}

	@Test(expected = OrderException.class)
	public void cancelOrderExceptionTest() throws OrderException {
		Mockito.when(orderService.cancelOrder(Mockito.anyLong())).thenReturn(false);
		orderController.cancel(AUTHORIZATION, ORDER_ID);
	}

	@Test
	public void updateOrderTest() throws OrderException {
		OrderRequestDto orderRequestDto = getOrderRequest();
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(orderRequestDto.getCustomerId(),
				orderRequestDto.getRestaurantId(), orderRequestDto.getItems(), 1L);
		OrderUpdateResponseDto orderUpdateResponseDto = getOrderResponse();
		Mockito.when(orderService.updateOrder(Mockito.any(OrderUpdateDto.class))).thenReturn(orderUpdateResponseDto);
		assertEquals(orderUpdateResponseDto, orderController.updateOrder(AUTHORIZATION, orderUpdateDto).getBody());
	}

	@Test
	public void getOrderByIdTest() throws OrderException {
		Order order = new Order(1L, "CREATED", 1L);
		Optional<Order> optionalOrder = Optional.of(order);
		Mockito.when(orderService.getOrderById(Mockito.anyLong())).thenReturn(optionalOrder);
		assertEquals(order, orderController.getOrderById(AUTHORIZATION, ORDER_ID).getBody());
	}

	@Test(expected = OrderException.class)
	public void getOrderByIdExceptionTest() throws OrderException {
		Optional<Order> optionalOrder = Optional.empty();
		Mockito.when(orderService.getOrderById(Mockito.anyLong())).thenReturn(optionalOrder);
		orderController.getOrderById(AUTHORIZATION, ORDER_ID);
	}

	@Test
	public void getOrderAmountTest() throws OrderException {
		double price = 100.23;
		Mockito.when(orderService.getOrderAmountByOrderId(Mockito.anyLong())).thenReturn(price);
		assertEquals(new Double(100.23), orderController.getOrderAmountByOrderId(AUTHORIZATION, ORDER_ID).getBody());
	}

	@Test(expected = OrderException.class)
	public void getOrderAmountExceptionTest() throws OrderException {
		double price = 0;
		Mockito.when(orderService.getOrderAmountByOrderId(Mockito.anyLong())).thenReturn(price);
		orderController.getOrderAmountByOrderId(AUTHORIZATION, ORDER_ID);
	}

	private OrderUpdateResponseDto getOrderResponse() {
		Order order = new Order(1L, "CREATED", 1L);
		OrderedItem orderedItemsDto = new OrderedItem("Noodles", 5, 100, order, 1L);
		List<OrderedItem> orderedItems = new ArrayList<>();
		orderedItems.add(orderedItemsDto);
		OrderUpdateResponseDto orderUpdateResponse = new OrderUpdateResponseDto(1L, 1L, "CANCELLED", 1L, orderedItems);
		return orderUpdateResponse;
	}

	private OrderRequestDto getOrderRequest() {
		OrderRequestDto orderRequestDto = new OrderRequestDto();
		orderRequestDto.setCustomerId(1L);
		orderRequestDto.setRestaurantId(1L);
		OrderedItemsDto orderedItemsDto = new OrderedItemsDto(1, 5);
		List<OrderedItemsDto> orderedItems = new ArrayList<>();
		orderedItems.add(orderedItemsDto);
		orderRequestDto.setItems(orderedItems);
		return orderRequestDto;
	}

}
