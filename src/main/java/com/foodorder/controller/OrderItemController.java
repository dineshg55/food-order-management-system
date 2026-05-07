package com.foodorder.controller;

import java.util.List;

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

import com.foodorder.dao.OrderItemDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.OrderItem;
import com.foodorder.service.OrderItemService;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<List<OrderItem>>> addItems(@RequestBody List<OrderItem> orderItems){
		return orderItemService.addItem(orderItems);
	}
	
	@PatchMapping("/{quantity}/{id}")
	public ResponseEntity<ResponseStructure<OrderItem>> updateQuantity(@PathVariable Integer quantity,@PathVariable Long id){
		return orderItemService.updateItemQuantity(quantity, id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<OrderItem>> removeItem(@PathVariable Long id){
		return orderItemService.removeItem(id);
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ResponseStructure<List<OrderItem>>> getItemsByOrder(@PathVariable Long orderId){
		return orderItemService.getOrderItems(orderId);
	}
	
}
