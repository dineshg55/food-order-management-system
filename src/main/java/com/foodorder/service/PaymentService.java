package com.foodorder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.OrderDao;
import com.foodorder.dao.PaymentDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderStatus;
import com.foodorder.entity.Payment;
import com.foodorder.entity.PaymentMethod;
import com.foodorder.entity.PaymentStatus;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;
import com.foodorder.repository.PaymentRepository;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private OrderDao orderDao;
	
	public ResponseEntity<ResponseStructure<Payment>> getById(Long id){
		
		if(id==null)
			throw new ValidationException("Id must be Provided");
		
		Optional<Payment> opt = paymentDao.getById(id);
		
		if(!opt.isPresent())
			throw new IdNotFoundException("No Payment with Id "+ id);
		
			ResponseStructure<Payment> response = new ResponseStructure<Payment>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Payment Fetched Successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<Payment>>(response,HttpStatus.OK);
			
	}
	
	public ResponseEntity<ResponseStructure<List<Payment>>> getByPaymentStatus(PaymentStatus paymentStatus){
		
		if(paymentStatus==null)
			throw new ValidationException("PaymentStatus must be provided");
		
		List<Payment> payment = paymentDao.getByPaymentStatus(paymentStatus);
		
		ResponseStructure<List<Payment>> response = new ResponseStructure<List<Payment>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!payment.isEmpty()) {
			response.setMessage("Payment Fetched Successfully");
			response.setData(payment);
		}
		else {
			response.setMessage("No Payment with PaymentStatus "+ paymentStatus);
			response.setData(payment);
			
		}
		
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> getByPaymentMethod(PaymentMethod paymentMethod){
		
		if(paymentMethod==null)
			throw new ValidationException("PaymentMethod must be provided");
		
		List<Payment> payment = paymentDao.getByPaymentMethod(paymentMethod);
		
		ResponseStructure<List<Payment>> response = new ResponseStructure<List<Payment>>();
		response.setStatusCode(HttpStatus.OK.value());
		
		if(!payment.isEmpty()) {
			response.setMessage("Payment Fetched Successfully");
			response.setData(payment);
		}
		else {
			response.setMessage("No Payment with PaymentMethod "+ paymentMethod);
		}
		
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentStatus(PaymentStatus paymentStatus, Long id){

	    if(paymentStatus == null)
	        throw new ValidationException("PaymentStatus must be provided");

	    if(id == null)
	        throw new ValidationException("Id must be provided");

	    Optional<Payment> opt = paymentDao.getById(id);

	    if(!opt.isPresent())
	        throw new IdNotFoundException("No Payment with Id " + id);

	    Payment payment = opt.get();
	    Order order = payment.getOrder();

	    if(order == null)
	        throw new ValidationException("Payment is not linked to any order");

	    if(paymentStatus == PaymentStatus.REFUNDED){

	        if(payment.getPaymentStatus() != PaymentStatus.COMPLETED)
	            throw new ValidationException("Only completed payments can be refunded");

	        if(order.getStatus() != OrderStatus.CANCELLED)
	            throw new ValidationException("Refund allowed only if order is cancelled");

	    } 
	    else {
	        throw new ValidationException("Only refund operation is allowed");
	    }

	    payment.setPaymentStatus(paymentStatus);

	    ResponseStructure<Payment> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Payment refunded successfully");
	    response.setData(paymentDao.updatePaymentStatus(payment));

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
