package com.foodorder.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodorder.entity.Customer;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	public List<Order> findByCustomer_Id(Long customerId);
	
	public List<Order> findByStatus(OrderStatus status);
	
	@Query("SELECT o from Order o WHERE DATE(o.orderDateTime)= :date")
	public List<Order> findByDate(LocalDate date);
	
	public List<Order> findByTotalAmountBetween(Double startPrice ,Double endPrice);
	
	@Query("Select DISTINCT o FROM Order o JOIN o.orderItem oi JOIN oi.menuItem mi WHERE mi.restaurant.id=:restaurantId")
	public List<Order> findByRestaurantId(Long restaurantId);
	
}
