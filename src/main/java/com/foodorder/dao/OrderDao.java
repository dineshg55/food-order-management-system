package com.foodorder.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.Order;
import com.foodorder.entity.OrderStatus;
import com.foodorder.repository.OrderRepository;

@Repository
public class OrderDao {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public Order placeOrder(Order order) {
		return  orderRepository.save(order);
	}
	
	public List<Order> getAllOrder(){
		return orderRepository.findAll();
	}
	
	public Optional<Order> getById(Long id){
		return orderRepository.findById(id);
	}
	
	public List<Order> getByCustomer(Long id){
		return orderRepository.findByCustomer_Id(id);
	}
	
	public Order updateOrder(Order order) {
		return  orderRepository.save(order);
	}
	
	public void cancelOrder(Long id) {
		orderRepository.deleteById(id);
	}
	
	public List<Order> getByStatus(OrderStatus status){
		return orderRepository.findByStatus(status);
	}
	
	public List<Order> getByDate(LocalDate date){
		return orderRepository.findByDate(date);
	}
	
	public List<Order> getByTotalAmountBetween(Double startPrice,Double endPrice){
		return orderRepository.findByTotalAmountBetween(startPrice, endPrice);
	}
	
	public List<Order> getByRestaurant(Long restaurantId){
		return orderRepository.findByRestaurantId(restaurantId);
	}
	
	public Boolean existsById(Long id) {
		return orderRepository.existsById(id);
	}
}
