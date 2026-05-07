package com.foodorder.service;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.CustomerDao;
import com.foodorder.dao.MenuItemDao;
import com.foodorder.dao.OrderDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Customer;
import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.entity.OrderStatus;
import com.foodorder.entity.PaymentStatus;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private MenuItemDao menuItemDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	public ResponseEntity<ResponseStructure<Order>> placeOrder(Order order){

	    // 1. Validate payment
	    if(order.getPayment() == null)
	        throw new ValidationException("Payment details must be provided");

	    if(order.getPayment().getPaymentMethod() == null)
	        throw new ValidationException("Payment method must be specified");

	    if(order.getPayment().getPaymentStatus() == null)
	        throw new ValidationException("Payment status must be specified");

	    if(order.getPayment().getPaymentStatus() != PaymentStatus.COMPLETED)
	        throw new ValidationException("Only completed payments can place an order");

	    // 2. Validate and attach customer
	    if(order.getCustomer() == null || order.getCustomer().getId() == null)
	        throw new ValidationException("Customer ID must be provided");

	    Optional<Customer> customerOpt = customerDao.getById(order.getCustomer().getId());

	    if(customerOpt.isPresent()) {
	        order.setCustomer(customerOpt.get());
	    } else {
	        throw new IdNotFoundException("Customer not found with ID: " + order.getCustomer().getId());
	    }

	    // 3. Validate order items
	    if(order.getOrderItem() == null || order.getOrderItem().isEmpty())
	        throw new ValidationException("At least one order item must be provided");

	    Double totalPrice = 0.0;

	    for(OrderItem orderItem : order.getOrderItem()) {

	        if(orderItem.getMenuItem() == null || orderItem.getMenuItem().getId() == null)
	            throw new ValidationException("MenuItem ID must be provided");

	        if(orderItem.getQuantity() == null || orderItem.getQuantity() <= 0)
	            throw new ValidationException("Quantity must be greater than 0");

	        Optional<MenuItem> menuItemOpt = menuItemDao.getById(orderItem.getMenuItem().getId());

	        if(menuItemOpt.isPresent()) {
	            MenuItem menu = menuItemOpt.get();

	            orderItem.setMenuItem(menu);
	            orderItem.setSubTotal(menu.getPrice() * orderItem.getQuantity());
	            orderItem.setOrder(order);

	            totalPrice += orderItem.getSubTotal();
	        } else {
	            throw new IdNotFoundException(
	                "MenuItem not found with ID: " + orderItem.getMenuItem().getId()
	            );
	        }
	    }

	    // 4. Final order setup
	    order.setTotalAmount(totalPrice);
	    order.setOrderDateTime(LocalDateTime.now());
	    order.setStatus(OrderStatus.ORDERED);

	    // 5. Link payment
	    order.getPayment().setOrder(order);

	    // 6. Save
	    ResponseStructure<Order> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.CREATED.value());
	    response.setMessage("Order created successfully");
	    response.setData(orderDao.placeOrder(order));

	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getAllOrders(){
		List<Order> orders=orderDao.getAllOrder();
		
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		if(orders != null && !orders.isEmpty()) {
			response.setMessage("All orders fetched successfully");
			response.setData(orders);
		}
		else {
			response.setMessage("No orders found");
			response.setData(orders);
		}
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
	}
	
	
	public ResponseEntity<ResponseStructure<Order>> getById(Long id){
		
		if(id == null)
		    throw new ValidationException("Id must be provided");
		
		Optional<Order> opt = orderDao.getById(id);

		if(opt.isPresent()) {
		    ResponseStructure<Order> response = new ResponseStructure<>();
		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Order fetched successfully");
		    response.setData(opt.get());

		    return new ResponseEntity<>(response, HttpStatus.OK);
		}
		else {
		    throw new IdNotFoundException("Order not found with ID: " + id);
		}
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getOrderByCustomer(Long id){
		
		if(id == null)
		    throw new ValidationException("Id must be provided");
		
		if(!customerDao.existsById(id))
		    throw new IdNotFoundException("Customer not found with ID: " + id);
		
		List<Order> orders = orderDao.getByCustomer(id);
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!orders.isEmpty()) {
			response.setMessage("Orders fetched successfully for customer");
			response.setData(orders);
		}
		else {
			response.setMessage("No orders found");
			response.setData(orders);
			
		}
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
		
	}
	
	public  ResponseEntity<ResponseStructure<Order>> updateOrderStatus(OrderStatus orderStatus,Long id){
		
		if(orderStatus == null)
		    throw new ValidationException("Order status must be provided");
		
		if(id == null)
		    throw new ValidationException("Id must be provided");
		
		
		Optional<Order> opt=orderDao.getById(id);
		
		if(opt.isPresent()) {
			Order ord= opt.get();
			if(ord.getStatus() == OrderStatus.CANCELLED)
			    throw new ValidationException("Cannot update a cancelled order");

			if(ord.getStatus() == OrderStatus.SERVED)
			    throw new ValidationException("Delivered order cannot be modified");
			ord.setStatus(orderStatus);			
			ResponseStructure<Order> response = new ResponseStructure<Order>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Orders Status updated Successfully");
			response.setData(orderDao.updateOrder(ord));
			
			return new ResponseEntity<ResponseStructure<Order>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("Order not found with ID: " + id);
		
	}
	
	public ResponseEntity<ResponseStructure<Order>> cancelOrder(Long id){
		
		if(id == null)
		    throw new ValidationException("Id must be provided");
		
		Optional<Order> opt= orderDao.getById(id);
		
		if(opt.isPresent()) {
			Order order = opt.get();
			
			if(order.getStatus() == OrderStatus.CANCELLED)
			    throw new ValidationException("Order is already cancelled");

			if(order.getStatus() != OrderStatus.ORDERED)
			    throw new ValidationException("Only ORDERED status orders can be cancelled");
			
				orderDao.cancelOrder(id);
				
				ResponseStructure<Order> response= new ResponseStructure<Order>();
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Order cancelled successfully");
				response.setData(null);
				
				return new ResponseEntity<ResponseStructure<Order>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("No Order with Id");
		
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getByStatus(OrderStatus orderStatus){
		
		if(orderStatus == null)
		    throw new ValidationException("Order status must be provided");
		
		List<Order> order = orderDao.getByStatus(orderStatus);
		
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!order.isEmpty()) {
			response.setMessage("Orders fetched successfully by status");
			response.setData(order);
		}
		else {
			response.setMessage("No orders found");
			response.setData(order);
		}
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
			
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getByDate(LocalDate date){
		
		if(date == null)
		    throw new ValidationException("Date must be provided");
		
		List<Order> order = orderDao.getByDate(date);
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!order.isEmpty()) {
        	
			response.setMessage("Record Fetched");
			response.setData(order);
			}
		else {
			response.setMessage("No Order Found");
			response.setData(order);
			
		}
		
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
		
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getByTotalAmountBetween(Double startPrice,Double endPrice){
		
		if(startPrice == null || endPrice == null || startPrice > endPrice)
		    throw new ValidationException("Invalid price range");
		
		List<Order> order = orderDao.getByTotalAmountBetween(startPrice, endPrice);
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!order.isEmpty()) {
			response.setMessage("Orders fetched successfully within price range");
			response.setData(order);
			}
		else {
			response.setMessage("No Order Found");
			response.setData(order);
		}
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
		
		
	}
	
	public ResponseEntity<ResponseStructure<List<Order>>> getByRestaurant(Long restaurantId){
		
		if(restaurantId == null)
		    throw new ValidationException("RestaurantId must be provided");
		
		List<Order> order = orderDao.getByRestaurant(restaurantId);
		ResponseStructure<List<Order>> response = new ResponseStructure<List<Order>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!order.isEmpty()) {
			response.setMessage("Orders fetched successfully for restaurant ID: " + restaurantId);
			response.setData(order);
		}
		else
		{
			response.setMessage("No Order Found");
			response.setData(order);
		}
		return new ResponseEntity<ResponseStructure<List<Order>>>(response,HttpStatus.OK);
	}
	
	
}
