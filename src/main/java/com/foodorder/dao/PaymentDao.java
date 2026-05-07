package com.foodorder.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.Payment;
import com.foodorder.entity.PaymentMethod;
import com.foodorder.entity.PaymentStatus;
import com.foodorder.repository.PaymentRepository;

@Repository
public class PaymentDao {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	public Optional<Payment> getById(Long id){
		return paymentRepository.findById(id);
	}
	
	public List<Payment> getByPaymentStatus(PaymentStatus paymentStatus){
		return paymentRepository.findByPaymentStatus(paymentStatus);
	}
	
	public List<Payment> getByPaymentMethod(PaymentMethod paymentMethod){
		return paymentRepository.findByPaymentMethod(paymentMethod);
	}
	
	public Payment updatePaymentStatus(Payment payment) {
		return paymentRepository.save(payment);
	}

}
