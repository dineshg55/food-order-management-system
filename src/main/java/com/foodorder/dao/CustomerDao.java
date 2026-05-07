package com.foodorder.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.Customer;
import com.foodorder.repository.CustomerRepository;

@Repository
public class CustomerDao {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public List<Customer> getAllCustomer(){
		return customerRepository.findAll();
	}
	
	public Optional<Customer> getById(Long id){
		return customerRepository.findById(id);
	}
	
	public Customer updateCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}
	
	public Optional<Customer> getByContact(Long contact) {
		return customerRepository.findByContact(contact);
	}
	
	public Boolean existsByContact(Long contact) {
		return customerRepository.existsByContact(contact);
	}
	
	public Boolean existsByEmail(String email) {
		return customerRepository.existsByEmail(email);
	}
	public Boolean existsById(Long id) {
		return customerRepository.existsById(id);
	}

}
