package com.foodorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Payment;
import com.foodorder.entity.PaymentMethod;
import com.foodorder.entity.PaymentStatus;
import com.foodorder.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Payment>> getById(@PathVariable Long id){
		return paymentService.getById(id);
	}
	
	@GetMapping("/paymentStatus/{paymentStatus}")
	public ResponseEntity<ResponseStructure<List<Payment>>> getByPaymentStatus(@PathVariable PaymentStatus paymentStatus){
		return paymentService.getByPaymentStatus(paymentStatus);
	}
	
	@GetMapping("/paymentMethod/{paymentMethod}")
	public ResponseEntity<ResponseStructure<List<Payment>>> getByPaymentMethos(@PathVariable PaymentMethod paymentMethod){
		return paymentService.getByPaymentMethod(paymentMethod);
	}
	
	@PatchMapping("/{paymentStatus}/{id}")
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentStatus(@PathVariable PaymentStatus paymentStatus,@PathVariable Long id){
		return paymentService.updatePaymentStatus(paymentStatus, id);
	}

}
