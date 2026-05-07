package com.foodorder.service;

import java.awt.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.MenuItemDao;
import com.foodorder.dao.RestaurantDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Restaurant;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;

@Service
public class MenuItemService {
	
	@Autowired
	private MenuItemDao menuItemDao;
	
	@Autowired
	private RestaurantDao restaurentDao;
	
	public ResponseEntity<ResponseStructure<List<MenuItem>>> addMenuItems(List<MenuItem> menuItem){
		
		for(MenuItem menu:menuItem) {
			if(menu.getPrice() == null || menu.getPrice() <= 0)
			    throw new ValidationException("Price must be greater than 0");
			
			if(menu.getRestaurent() == null || menu.getRestaurent().getId() == null)
			    throw new ValidationException("Restaurant ID must be provided");
			
			Optional<Restaurant> opt = restaurentDao.getById(menu.getRestaurent().getId());

			if(opt.isPresent()) {
			    Restaurant res = opt.get();   // get actual DB object
			    menu.setRestaurent(res);      // attach real object
			} else {
			    throw new IdNotFoundException("Restaurant not found");
			}
		}
		
		ResponseStructure<List<MenuItem>> response= new ResponseStructure<List<MenuItem>>();
		
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setMessage("Menu items created successfully");
		response.setData(menuItemDao.addMenuItems(menuItem));
		
		return new ResponseEntity<ResponseStructure<List<MenuItem>>>(response,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getAllMenuItem(){
		
		List<MenuItem> menu=menuItemDao.getAllMenuItems();
		if(!menu.isEmpty()) {
			
			ResponseStructure<List<MenuItem>> response=new ResponseStructure<List<MenuItem>>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("All menu items fetched successfully");
			response.setData(menu);
			
			return new ResponseEntity<ResponseStructure<List<MenuItem>>>(response,HttpStatus.OK);
		}
		else
			throw new ResourceNotFoundException("Resource Not Found");
	}
	
	public ResponseEntity<ResponseStructure<MenuItem>> getById(Long id){
		Optional<MenuItem> opt = menuItemDao.getById(id);
		
		if(opt.isPresent()) {
			ResponseStructure<MenuItem> response=new ResponseStructure<MenuItem>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Menu item fetched successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<MenuItem>>(response,HttpStatus.OK);
			
		}
		else
			throw new IdNotFoundException("No Record with Id");
	}
	
	
	public ResponseEntity<ResponseStructure<MenuItem>> updateMenuItem(MenuItem menuItem){
		if(menuItem.getId()==null)
			throw new IdNotFoundException("Id Not Found");
		
		Optional<MenuItem> opt = menuItemDao.getById(menuItem.getId());
		if(opt.isPresent()) {
			MenuItem existing = opt.get();

			if(menuItem.getItemName() != null)
			    existing.setItemName(menuItem.getItemName());

			if(menuItem.getPrice() != null)
			    existing.setPrice(menuItem.getPrice());

			if(menuItem.getAvailability() != null)
			    existing.setAvailability(menuItem.getAvailability());

			menuItemDao.updateMenuItem(existing);
			ResponseStructure<MenuItem> response = new ResponseStructure<MenuItem>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Menu item updated successfully");
			response.setData(existing);
			
			return new ResponseEntity<ResponseStructure<MenuItem>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("No Record with Id");
	}
	
	public ResponseEntity<ResponseStructure<MenuItem>> deleteMenuItem(Long id){
		 Optional<MenuItem> opt= menuItemDao.getById(id);
		 
		 if(opt.isPresent()) {
			 menuItemDao.deleteMenuItem(id);
			 ResponseStructure<MenuItem> response= new ResponseStructure<MenuItem>();
			 response.setStatusCode(HttpStatus.OK.value());
			 response.setMessage("Menu item deleted successfully");
			 response.setData(null);
			 
			 return new ResponseEntity<ResponseStructure<MenuItem>>(response,HttpStatus.OK);
		 }
		 else
			 throw new IdNotFoundException("No Record with Id");
	}
	
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getByPriceGreaterThan(Double price){
		
		List<MenuItem> menu= menuItemDao.getItemsGreaterThan(price);
		if(!menu.isEmpty()) {
			ResponseStructure<List<MenuItem>> response=new ResponseStructure<List<MenuItem>>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Menu items with price greater than " + price + " fetched successfully");
			response.setData(menu);
			
			return new ResponseEntity<ResponseStructure<List<MenuItem>>>(response,HttpStatus.OK);
			
		}
		else
			throw new ResourceNotFoundException("Resource Not Found");
	}
	
	public ResponseEntity<ResponseStructure<MenuItem>> getByName(String name){
		Optional<MenuItem> opt = menuItemDao.getByName(name);
		
		if(opt.isPresent()) {
			ResponseStructure<MenuItem> response=new ResponseStructure<MenuItem>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Menu item fetched by name successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<MenuItem>>(response,HttpStatus.OK);
			
		}
		else
			throw new ResourceNotFoundException("Resource Not Found");
	}
	

}
