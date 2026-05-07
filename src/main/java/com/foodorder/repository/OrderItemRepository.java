package com.foodorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodorder.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	public List<OrderItem> findByOrder_Id(Long orderId); 

}
