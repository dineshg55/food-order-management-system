package com.foodorder.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.CustomerDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.Customer;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceAlreadyExistsException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerDao customerDao;
	
	public ResponseEntity<ResponseStructure<Customer>> createCustomer(Customer customer){
		
		if(!(customer.getContact()>=1000000000L && customer.getContact()<=9999999999L))
			throw new ValidationException("Invalid Contact");
		
		if(customer.getEmail()!=null && !customer.getEmail().contains("@"))
			throw new ValidationException("Invalid Email Format");
		
		if(customerDao.existsByContact(customer.getContact()))
			throw new ResourceAlreadyExistsException("Contact Already Exists");
		
		if(customerDao.existsByEmail(customer.getEmail()))
				throw new ResourceAlreadyExistsException("Email Already Exists");
		
		ResponseStructure<Customer> response = new ResponseStructure<Customer>();
		
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setMessage("Customer created successfully");
		response.setData(customerDao.createCustomer(customer));
		
		return new ResponseEntity<ResponseStructure<Customer>>(response,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer(){
		
		List<Customer> customer = customerDao.getAllCustomer();
		
		if(!customer.isEmpty()) {
			ResponseStructure<List<Customer>> response = new ResponseStructure<List<Customer>>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("All customers retrieved successfully");
			response.setData(customer);
			
			return new ResponseEntity<ResponseStructure<List<Customer>>>(response,HttpStatus.OK);
		}	
		else
			throw new ResourceNotFoundException("No Record");
	}
	
	public ResponseEntity<ResponseStructure<Customer>> getById(Long id){
		
		Optional<Customer> opt = customerDao.getById(id);
		
		if(opt.isPresent()) {
			
			ResponseStructure<Customer> response = new ResponseStructure<Customer>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Customer retrieved successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<Customer>>(response,HttpStatus.OK);
		}	
		else
			throw new IdNotFoundException("Customer not found with ID: " + id);
	}
	
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(Customer customer){
		
		if(customer.getId() == null)
		    throw new ValidationException("Customer ID must be provided for update");
		
		Optional<Customer> opt = customerDao.getById(customer.getId());
		
		if(opt.isEmpty()) 
			throw new IdNotFoundException("Customer not found with ID: " + customer.getId());
			
			
			Customer existing = opt.get(); 
			
			if(customer.getEmail() != null &&
					   customerDao.existsByEmail(customer.getEmail()) &&
					   !existing.getEmail().equals(customer.getEmail())) {
					    throw new ResourceAlreadyExistsException("Email already exists");
					}
			if(customer.getEmail()!=null && !customer.getEmail().contains("@"))
				throw new ValidationException("Invalid Email Format");
			
			if(customer.getContact()!=null) {
				
				if(!(customer.getContact()>=1000000000L && customer.getContact()<=9999999999L))
					throw new ValidationException("Invalid Contact");
				
				if(customerDao.existsByContact(customer.getContact()) && !existing.getContact().equals(customer.getContact())) {
						    throw new ResourceAlreadyExistsException("Contact already exists");
						}
			}
			
			if(customer.getName() != null)
			    existing.setName(customer.getName());

			if(customer.getEmail() != null)
			    existing.setEmail(customer.getEmail());

			if(customer.getContact() != null)
			    existing.setContact(customer.getContact());

			if(customer.getAddress() != null)
			    existing.setAddress(customer.getAddress());
			
			ResponseStructure<Customer> response = new ResponseStructure<Customer>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Customer updated successfully");
			response.setData(customerDao.updateCustomer(existing));
			
			return new ResponseEntity<ResponseStructure<Customer>>(response,HttpStatus.OK);
		}
	
	public ResponseEntity<ResponseStructure<Customer>> deleteCustomer(Long id){
		
		Optional<Customer> opt = customerDao.getById(id);
		
		if(opt.isPresent()) {
			
			customerDao.deleteCustomer(id);
			ResponseStructure<Customer> response = new ResponseStructure<Customer>();
			
			response.setStatusCode(HttpStatus.NO_CONTENT.value());
			response.setMessage("Customer deleted successfully");
			response.setData(null);
			
			return new ResponseEntity<ResponseStructure<Customer>>(response,HttpStatus.NO_CONTENT);
		}	
		else
			throw new IdNotFoundException("No Record with Id");
	}
	
public ResponseEntity<ResponseStructure<Customer>> getByContact(Long contact){
		
		Optional<Customer> opt = customerDao.getByContact(contact);
		
		if(opt.isPresent()) {
			
			ResponseStructure<Customer> response = new ResponseStructure<Customer>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Customer retrieved successfully by contact");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<Customer>>(response,HttpStatus.OK);
		}	
		else
			throw new ResourceNotFoundException("Customer not found with contact: " + contact);
	}
	
}
