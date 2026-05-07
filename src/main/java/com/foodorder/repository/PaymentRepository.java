package com.foodorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodorder.entity.Payment;
import com.foodorder.entity.PaymentMethod;
import com.foodorder.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	public List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
	
	public List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
	
	
}
