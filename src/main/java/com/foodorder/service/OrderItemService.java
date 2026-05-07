package com.foodorder.service;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.MenuItemDao;
import com.foodorder.dao.OrderDao;
import com.foodorder.dao.OrderItemDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;

@Service
public class OrderItemService {
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private MenuItemDao menuItemDao;
	
	public ResponseEntity<ResponseStructure<List<OrderItem>>> addItem(List<OrderItem> orderItems){

	    for(OrderItem item : orderItems){
	    	
	        if(item.getQuantity() == null || item.getQuantity() < 1)
	            throw new ValidationException("Quantity must be at least 1");

	        if(item.getOrder() == null || item.getOrder().getId() == null)
	            throw new ValidationException("Order ID must be provided");

	        Optional<Order> orderOpt = orderDao.getById(item.getOrder().getId());

	        if(!orderOpt.isPresent())
	            throw new IdNotFoundException("Order not found with ID: " + item.getOrder().getId());

	        Order order = orderOpt.get();

	        if(item.getMenuItem() == null || item.getMenuItem().getId() == null)
	            throw new ValidationException("MenuItem ID must be provided");

	        Optional<MenuItem> menuOpt = menuItemDao.getById(item.getMenuItem().getId());

	        if(!menuOpt.isPresent())
	            throw new IdNotFoundException("MenuItem not found with ID: " + item.getMenuItem().getId());

	        MenuItem menu = menuOpt.get();
	        item.setMenuItem(menu);
	        item.setOrder(order);

	        Double subTotal = menu.getPrice() * item.getQuantity();
	        item.setSubTotal(subTotal);

	        Double total = order.getTotalAmount();
	        if(total == null) {
	            total = 0.0;
	        }

	        total = total + subTotal;
	        order.setTotalAmount(total);

	        orderDao.updateOrder(order);
	    }

	    ResponseStructure<List<OrderItem>> response = new ResponseStructure<List<OrderItem>>();
	    response.setStatusCode(HttpStatus.CREATED.value());
	    response.setMessage("Order items added successfully");
	    response.setData(orderItemDao.addItems(orderItems));

	    return new ResponseEntity<ResponseStructure<List<OrderItem>>>(response, HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<OrderItem>> updateItemQuantity(Integer quantity, Long id){
		
		if(quantity == null|| quantity< 1)
			throw new ValidationException("Quantity must be greater than 0");
		
		if(id==null)
			throw new ValidationException("Id must be provided");
	
		Optional<OrderItem> opt = orderItemDao.getById(id);
		
		if(!opt.isPresent())
				throw new IdNotFoundException("No orderItem with Id "+ id );
		
			OrderItem orderItem=opt.get();
			if(orderItem.getOrder()==null)
				throw new ValidationException("No order associated with orderItem ID: "+ id);
			
			Order order=orderItem.getOrder();
			
			if(orderItem.getMenuItem() == null)
			    throw new ValidationException("MenuItem not found for this order item");

			Double price = orderItem.getMenuItem().getPrice();
			
//			sd
			Double orderTotal = order.getTotalAmount();
			if(orderTotal == null ) {
			    orderTotal = 0.0;
			}

			Double oldSubTotal = orderItem.getSubTotal();
			if(oldSubTotal == null) {
			    oldSubTotal = 0.0;
			}

			Double newSubTotal = price * quantity;

			orderTotal = orderTotal - oldSubTotal + newSubTotal;
			if(orderTotal<0) {
				orderTotal=0.0;
			}
			
			orderItem.setQuantity(quantity);
			orderItem.setSubTotal(newSubTotal);
			order.setTotalAmount(orderTotal);
			orderDao.updateOrder(order);
			
			ResponseStructure<OrderItem> response = new ResponseStructure<OrderItem>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Quantity Updated for the OrderItem");
			response.setData(orderItemDao.updateItemQuantity(orderItem));
			
			return new ResponseEntity<ResponseStructure<OrderItem>>(response,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<OrderItem>> removeItem(Long id){
		
		if(id==null)
			throw new ValidationException("Id must be provided");
		
		Optional<OrderItem> opt = orderItemDao.getById(id);
		
		if(!opt.isPresent())
			throw new IdNotFoundException("No OrderItem with Id "+ id);
		
			OrderItem orderItem = opt.get();
			
			if(orderItem.getOrder()==null)
				throw new ValidationException("No order associated with orderItem ID: " + id);
			
			Order order = orderItem.getOrder();
			
			Double orderTotal=order.getTotalAmount();
			if(orderTotal==null) {
				orderTotal=0.0;
			}
			
			Double subTotal=orderItem.getSubTotal();
			if(subTotal==null) {
				subTotal=0.0;
			}
			
			Double newTotal=orderTotal-subTotal;
			
			order.setTotalAmount(newTotal);
			orderDao.updateOrder(order);
			
			orderItemDao.removeItem(id);
			
			ResponseStructure<OrderItem> response = new ResponseStructure<OrderItem>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("OrderItem Removed ");
			response.setData(null);
			
			return new ResponseEntity<ResponseStructure<OrderItem>>(response,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<List<OrderItem>>> getOrderItems(Long orderId){
		
		if(orderId==null)
			throw new ValidationException("OrderId must be provided");
		
		Optional<Order> orderOpt = orderDao.getById(orderId);
		if(!orderOpt.isPresent())
			throw new ResourceNotFoundException("No Order with Id "+ orderId);
		
		List<OrderItem> items = orderItemDao.getItemsByOrder(orderId);

		if(items == null || items.isEmpty()) {
		    throw new ResourceNotFoundException("No OrderItems found for Order ID: " + orderId);
		}
		
		ResponseStructure<List<OrderItem>> response = new ResponseStructure<List<OrderItem>>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("OrderItems with OrderId is Successfully Fetched");
		response.setData(items);
		
		return new ResponseEntity<ResponseStructure<List<OrderItem>>>(response,HttpStatus.OK);
	}
	
}
