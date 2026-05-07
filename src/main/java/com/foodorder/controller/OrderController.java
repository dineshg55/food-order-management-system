package com.foodorder.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderStatus;
import com.foodorder.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Order>> placeOrder(@RequestBody Order order){
		return orderService.placeOrder(order);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Order>>> getAllOrder(){
		return orderService.getAllOrders();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Order>> getById(@PathVariable Long id){
		return orderService.getById(id);
	}
	
	@GetMapping("customer/{id}")
	public ResponseEntity<ResponseStructure<List<Order>>> getByCustomer(@PathVariable Long id){
		return orderService.getOrderByCustomer(id);
	}
	
	@PatchMapping("/status/{orderStatus}/{id}")
	public ResponseEntity<ResponseStructure<Order>> updateOrderStatus(@PathVariable OrderStatus orderStatus,@PathVariable Long id){
		return orderService.updateOrderStatus(orderStatus, id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Order>> cancelOrder(@PathVariable Long id){
		return orderService.cancelOrder(id);
	}
	
	@GetMapping("/orderStatus/{orderStatus}")
	public ResponseEntity<ResponseStructure<List<Order>>> getByStatus(@PathVariable OrderStatus orderStatus){
		return orderService.getByStatus(orderStatus);
	}
	
	@GetMapping("date/{date}")
	public ResponseEntity<ResponseStructure<List<Order>>> getByDate(@PathVariable LocalDate date){
		return orderService.getByDate(date);
	}
	
	@GetMapping("/{startPrice}/{endPrice}")
	public ResponseEntity<ResponseStructure<List<Order>>> getByTotalAmountBetween(@PathVariable Double startPrice,@PathVariable Double endPrice){
		return orderService.getByTotalAmountBetween(startPrice, endPrice);
	}
	
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<ResponseStructure<List<Order>>> getByRestaurant(@PathVariable Long id){
		return orderService.getByRestaurant(id);
	}
}
