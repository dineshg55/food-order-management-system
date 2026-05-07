package com.foodorder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodorder.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	public Optional<Customer> findByContact(Long contact);
	
	public Boolean existsByContact(Long contact);
	
	public Boolean existsByEmail(String email);

}
