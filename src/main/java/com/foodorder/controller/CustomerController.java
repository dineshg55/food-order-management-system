package com.foodorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Customer;
import com.foodorder.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer){
		return customerService.createCustomer(customer);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer(){
		return customerService.getAllCustomer();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Customer>> getById(@PathVariable Long id){
		return customerService.getById(id);
	}
	
	@PatchMapping
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@RequestBody Customer customer){
		return customerService.updateCustomer(customer);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Customer>> deleteCustomer(@PathVariable Long id){
		return customerService.deleteCustomer(id);
	}
	
	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<Customer>> getByContact(@PathVariable Long contact){
		return customerService.getByContact(contact);
	}
	

}
