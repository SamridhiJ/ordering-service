package com.eatza.orderingservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.eatza.order.dto.ItemFetchDto;
import com.eatza.order.dto.MenuFetchDto;
import com.eatza.order.dto.OrderRequestDto;
import com.eatza.order.dto.OrderUpdateDto;
import com.eatza.order.dto.OrderedItemsDto;
import com.eatza.order.dto.RestaurantFetchDto;
import com.eatza.order.exception.OrderException;
import com.eatza.order.model.Order;
import com.eatza.order.model.OrderedItem;
import com.eatza.order.repository.OrderRepository;
import com.eatza.order.service.itemservice.ItemServiceImpl;
import com.eatza.order.service.orderservice.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ItemServiceImpl itemService;

	@Mock
	private RestTemplate restTemplate;

	OrderRequestDto orderRequest = getOrderRequest(getOrderedItems());
	ItemFetchDto item = getItem(getMenuDetails(getRestaurantDetails()));
	Order order = new Order(orderRequest.getCustomerId(), "CREATED", orderRequest.getRestaurantId());

	@Test
	public void placeOrder_basic() throws OrderException {
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		when(itemService.saveItem(any(OrderedItem.class))).thenReturn(new OrderedItem());
		assertEquals(order, orderService.placeOrder(orderRequest));
	}

	@Test(expected = OrderException.class)
	public void placeOrder_Exception() throws OrderException {
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any()))
				.thenThrow(ResourceAccessException.class);
		assertEquals(order, orderService.placeOrder(orderRequest));
	}

	@Test(expected = OrderException.class)
	public void placeOrder_different_restaurant() throws OrderException {
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(null);
		orderService.placeOrder(orderRequest);

	}

	@Test(expected = OrderException.class)
	public void placeOrder_no_item() throws OrderException {
		RestaurantFetchDto restaurant = getRestaurantDetails();
		restaurant.setId(3L);
		MenuFetchDto menu = getMenuDetails(restaurant);
		menu.setRestaurant(restaurant);

		item.setMenu(menu);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		orderService.placeOrder(orderRequest);

	}

	@Test(expected = OrderException.class)
	public void placeOrder_quantity_zero() throws OrderException {
		OrderedItemsDto orderedItemsDto = getOrderedItems();
		orderedItemsDto.setQuantity(0);
		OrderRequestDto orderRequest = getOrderRequest(orderedItemsDto);
		ItemFetchDto item = getItem(getMenuDetails(getRestaurantDetails()));
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		orderService.placeOrder(orderRequest);
	}

	@Test
	public void cancelOrder_basic() {
		Optional<Order> optionalOrder = Optional.of(order);
		Order placedOrder = order;
		placedOrder.setStatus("CANCELLED");
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(placedOrder);
		assertTrue(orderService.cancelOrder(1L));
	}

	@Test
	public void cancelOrder_false() {
		Optional<Order> order = Optional.empty();
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(order);
		assertFalse(orderService.cancelOrder(1L));
	}

	@Test
	public void getOrderById_basic() {
		Optional<Order> optinalOrder = Optional.of(order);
		when(orderRepository.findById(anyLong())).thenReturn(optinalOrder);
		assertEquals(optinalOrder, orderService.getOrderById(1L));
	}

	@Test
	public void getOrderAmountByOrderId() {
		Optional<Order> optionalOrder = Optional.of(order);
		List<OrderedItem> itemsOrdered = getOrderedItems(optionalOrder);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		Mockito.when(itemService.findbyOrderId(anyLong())).thenReturn(itemsOrdered);
		assertEquals(new Double(300), new Double(orderService.getOrderAmountByOrderId(1L)));
	}

	@Test
	public void getOrderAmountByOrderId_zero() {
		Optional<Order> order = Optional.empty();
		List<OrderedItem> itemsOrdered = new ArrayList<OrderedItem>();
		itemsOrdered.add(new OrderedItem());
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(order);
		assertEquals(new Double(0), new Double(orderService.getOrderAmountByOrderId(1L)));
	}

	@Test(expected = OrderException.class)
	public void updateOrder_orderNotFound() throws OrderException {
		Optional.empty();
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
		orderService.updateOrder(orderUpdateDto);

	}

	@Test(expected = OrderException.class)
	public void updateOrder_quantityLess() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 1L, Arrays.asList(new OrderedItemsDto(1L, 0)), 1L);
		order.setStatus("UPDATED");
		Optional<Order> optinalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(optinalOrder);
		ItemFetchDto item = getItem(getMenuDetails(getRestaurantDetails()));
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		orderService.updateOrder(orderUpdateDto);

	}

	@Test(expected = OrderException.class)
	public void updateOrder_itemnull() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 1L, Arrays.asList(new OrderedItemsDto(1L, 1)), 1L);
		order.setStatus("UPDATED");
		Optional<Order> optinalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optinalOrder);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(null);
		orderService.updateOrder(orderUpdateDto);

	}

	@Test(expected = OrderException.class)
	public void updateOrder_differentRestaurantUpdate() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 1L, Arrays.asList(new OrderedItemsDto(1L, 1)), 1L);
		Optional<Order> optionalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		RestaurantFetchDto restaurantFetchDto = getRestaurantDetails();
		restaurantFetchDto.setId(4L);
		ItemFetchDto item = getItem(getMenuDetails(restaurantFetchDto));
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		orderService.updateOrder(orderUpdateDto);

	}

	@Test
	public void updateOrder_basic() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 1L, Arrays.asList(new OrderedItemsDto(1L, 1)), 1L);
		Order orderReturned = new Order(1L, "UPDATED", 1L);
		orderReturned.setId(1L);
		Optional<Order> optionalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		ItemFetchDto item = getItem(getMenuDetails(getRestaurantDetails()));
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(item);
		Mockito.when(itemService.saveItem(any(OrderedItem.class))).thenReturn(getOrderedItems(optionalOrder).get(0));
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(orderReturned);
		assertEquals(orderReturned.getId(), orderService.updateOrder(orderUpdateDto).getOrderId());
	}

	@Test(expected = OrderException.class)
	public void updateOrder_serviceunavailable() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 1L, Arrays.asList(new OrderedItemsDto(1L, 1)), 1L);
		Order orderReturned = new Order(1L, "UPDATED", 1L);
		orderReturned.setId(1L);
		Optional<Order> optionalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any()))
				.thenThrow(ResourceAccessException.class);
		orderService.updateOrder(orderUpdateDto);
	}

	@Test(expected = OrderException.class)
	public void updateOrder_restaurantDifferent() throws OrderException {
		OrderUpdateDto orderUpdateDto = new OrderUpdateDto(1L, 2L, Arrays.asList(new OrderedItemsDto(1L, 1)), 1L);
		Optional<Order> optionalOrder = Optional.of(order);
		Mockito.when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
		orderService.updateOrder(orderUpdateDto);
	}

	private ItemFetchDto getItem(MenuFetchDto menu) {
		ItemFetchDto item = new ItemFetchDto();
		item.setDescription("Dosa");
		item.setId(1L);
		item.setMenu(menu);
		item.setName("Onion Dosa");
		item.setPrice(110);
		return item;
	}

	private MenuFetchDto getMenuDetails(RestaurantFetchDto restaurant) {
		MenuFetchDto menu = new MenuFetchDto();
		menu.setId(1L);
		menu.setActiveFrom("10");
		menu.setActiveTill("22");
		menu.setRestaurant(restaurant);
		return menu;
	}

	private RestaurantFetchDto getRestaurantDetails() {
		RestaurantFetchDto restaurant = new RestaurantFetchDto();
		restaurant.setBudget(400);
		restaurant.setRating(4.2);
		restaurant.setCuisine("South Indian");
		restaurant.setId(1L);
		restaurant.setName("Vasudev");
		restaurant.setLocation("RR Nagar");
		return restaurant;
	}

	private OrderRequestDto getOrderRequest(OrderedItemsDto orderedItemsDto) {
		OrderRequestDto orderRequest = new OrderRequestDto();
		orderRequest.setCustomerId(1L);
		orderRequest.setRestaurantId(1L);
		orderRequest.setItems(Arrays.asList(orderedItemsDto));
		return orderRequest;
	}

	private OrderedItemsDto getOrderedItems() {
		OrderedItemsDto orderedItemsDto = new OrderedItemsDto();
		orderedItemsDto.setItemId(1L);
		orderedItemsDto.setQuantity(1);
		return orderedItemsDto;
	}

	private List<OrderedItem> getOrderedItems(Optional<Order> optionalOrder) {
		List<OrderedItem> itemsOrdered = new ArrayList<OrderedItem>();
		OrderedItem orderedItem1 = new OrderedItem("Dosa", 2, 100, optionalOrder.get(), 1L);
		OrderedItem orderedItem2 = new OrderedItem("Idly", 2, 50, optionalOrder.get(), 2L);
		itemsOrdered.add(orderedItem1);
		itemsOrdered.add(orderedItem2);
		return itemsOrdered;
	}
}
