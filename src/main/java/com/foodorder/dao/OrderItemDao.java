package com.foodorder.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.OrderItem;
import com.foodorder.repository.OrderItemRepository;

@Repository
public class OrderItemDao {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	public List<OrderItem> addItems(List<OrderItem>  orderItems){
		return orderItemRepository.saveAll(orderItems);
	}
	
	public OrderItem updateItemQuantity(OrderItem orderItem) {
		return orderItemRepository.save(orderItem);
	}
	
	public void removeItem(Long id) {
		orderItemRepository.deleteById(id);
	}
	
	public List<OrderItem> getItemsByOrder(Long orderId){
		return orderItemRepository.findByOrder_Id(orderId);
	}
	public Optional<OrderItem> getById(Long id){
		return orderItemRepository.findById(id);
	}

}
