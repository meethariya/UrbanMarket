/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.urbanmarket.orderservice.dto.RequestCartDto;
import com.urbanmarket.orderservice.dto.RequestOrderDto;
import com.urbanmarket.orderservice.dto.ResponseCartDto;
import com.urbanmarket.orderservice.dto.ResponseOrderDto;
import com.urbanmarket.orderservice.exception.DataNotFoundException;
import com.urbanmarket.orderservice.model.Cart;
import com.urbanmarket.orderservice.model.Order;
import com.urbanmarket.orderservice.repository.CartRepositoy;
import com.urbanmarket.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for order and cart
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final CartRepositoy cartRepositoy;

	private final OrderRepository orderRepository;

	private final ModelMapper modelMapper;

	@Value("${server.port}")
	String port;

	/**
	 * Creates or updates cart for a customer
	 * 
	 * @param cartDto requestCartDto
	 * @return responseCartDto of updated/new cart
	 */
	public ResponseCartDto createUpdateCart(RequestCartDto cartDto) {
		log.info("ORDERSERVICE: Create/Update Cart @PORT: " + port);

		Optional<Cart> customerId = cartRepositoy.findByCustomerId(cartDto.getCustomerId());
		Cart cart;
		if (customerId.isPresent()) {
			// cart for customer exists. Update it
			cart = customerId.get();
			cart.setItems(cartDto.getItems());
		} else {
			// create new cart
			cart = requestToModel(cartDto);
		}
		return modelToResponse(cartRepositoy.save(cart));
	}

	/**
	 * Get cart list of a customer
	 * 
	 * @param customerId id
	 * @return responseCartDto.
	 */
	public ResponseCartDto getCustomerCart(long customerId) {
		log.info("ORDERSERVICE: Get Cart by customerId @PORT: " + port);

		return modelToResponse(cartRepositoy.findByCustomerId(customerId).orElseThrow(
				() -> new DataNotFoundException("Cart is not created for customer with id: " + customerId)));
	}

	/**
	 * Delete cart of a customer.
	 * 
	 * @param customerId id
	 */
	public void deleteCart(long customerId) {
		log.info("ORDERSERVICE: Delete Cart by customerId @PORT: " + port);

		cartRepositoy.deleteByCustomerId(customerId);
	}

	/**
	 * Get all orders
	 * 
	 * @return list of responseOrderDto
	 */
	public List<ResponseOrderDto> getOrder() {
		log.info("ORDERSERVICE: Get all orders @PORT: " + port);

		return orderRepository.findAll().stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get all orders placed by customer
	 * 
	 * @param id customerId
	 * @return list of responseOrderDto
	 */
	public List<ResponseOrderDto> getOrderOfCustomer(long id) {
		log.info("ORDERSERVICE: Get orders of a customer @PORT: " + port);

		return orderRepository.findByCustomerId(id).stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get order by id
	 * 
	 * @param id UUID
	 * @return responseOrderDto
	 */
	public ResponseOrderDto getOrderById(UUID id) {
		log.info("ORDERSERVICE: Get order by id @PORT: " + port);

		return modelToResponse(orderRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("No order exists with id: " + id)));
	}

	/**
	 * Place an order.
	 * 
	 * @param orderDto requestOrderDto
	 * @return created responseOrderDto
	 */
	public ResponseOrderDto createOrder(RequestOrderDto orderDto) {
		log.info("ORDERSERVICE: Create order @PORT: " + port);

		return modelToResponse(orderRepository.save(requestToModel(orderDto)));
	}

	/**
	 * Converts request cart dto to model
	 * 
	 * @param cartDto requestCartDto
	 * @return cart model
	 */
	private Cart requestToModel(RequestCartDto cartDto) {
		return modelMapper.map(cartDto, Cart.class);
	}

	/**
	 * Converts model cart to response cart dto
	 * 
	 * @param cart model
	 * @return responseCartDto
	 */
	private ResponseCartDto modelToResponse(Cart cart) {
		return modelMapper.map(cart, ResponseCartDto.class);
	}

	/**
	 * Converts request order dto to model
	 * 
	 * @param orderDto requestOrderDto
	 * @return order model
	 */
	private Order requestToModel(RequestOrderDto orderDto) {
		Order order = modelMapper.map(orderDto, Order.class);
		// TODO: replace static transaction id by making request to payment service
		return order;
	}

	/**
	 * Converts model order to response order dto
	 * 
	 * @param order model
	 * @return responseOrderDto
	 */
	private ResponseOrderDto modelToResponse(Order order) {
		return modelMapper.map(order, ResponseOrderDto.class);
	}
}
