package com.foodorder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodorder.dao.RestaurantDao;
import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Restaurant;
import com.foodorder.exception.IdNotFoundException;
import com.foodorder.exception.ResourceNotFoundException;
import com.foodorder.exception.ValidationException;

@Service
public class RestaurantService {
	
	@Autowired
	private RestaurantDao restaurentDao;
	
	public ResponseEntity<ResponseStructure<Restaurant>> createRestaurant(Restaurant restaurent){
		

		if(restaurent.getName() == null || restaurent.getName().isBlank())
		    throw new ValidationException("Restaurant name is required");

		if(restaurent.getLocation() == null || restaurent.getLocation().isBlank())
		    throw new ValidationException("Location is required");
		
		ResponseStructure<Restaurant> response = new ResponseStructure<Restaurant>();
		
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setMessage("Restaurant created successfully");
		response.setData(restaurentDao.creataRestaurent(restaurent));
		
		return new ResponseEntity<ResponseStructure<Restaurant>>(response,HttpStatus.CREATED);
		
	}
	
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getAllRestaurant(){
		
		List<Restaurant> res=restaurentDao.getAllRestaurent();
		if(!res.isEmpty()) {
		ResponseStructure<List<Restaurant>> response= new ResponseStructure<List<Restaurant>>();
		
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("All restaurants retrieved successfully");
		response.setData(res);
		
		return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response,HttpStatus.OK);
		}
		else
			throw new ResourceNotFoundException("No restaurants found");
		
	}
	
	public ResponseEntity<ResponseStructure<Restaurant>> getById(Long id){
		
		Optional<Restaurant> opt = restaurentDao.getById(id);
		
		if(opt.isPresent()) {
			ResponseStructure<Restaurant> response = new ResponseStructure<Restaurant>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurant retrieved successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<ResponseStructure<Restaurant>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("Restaurant not found with ID: " + id);
	}
	
	public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurant(Restaurant restaurent){
		
		if(restaurent.getId()==null)
			throw new IdNotFoundException("Id Not Found");
		
		if(restaurent.getName() == null || restaurent.getName().isBlank())
		    throw new ValidationException("Restaurant name is required");

		if(restaurent.getLocation() == null || restaurent.getLocation().isBlank())
		    throw new ValidationException("Location is required");
		
		Optional<Restaurant> opt = restaurentDao.getById(restaurent.getId());
		if(opt.isPresent()) {
			ResponseStructure<Restaurant> response = new ResponseStructure<Restaurant>();
			
			Restaurant existing = opt.get();

			if(restaurent.getName() != null)
			    existing.setName(restaurent.getName());

			if(restaurent.getLocation() != null)
			    existing.setLocation(restaurent.getLocation());

			Restaurant updated = restaurentDao.updateRestaurent(existing);
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurant updated successfully");
			response.setData(updated);
			
			return new ResponseEntity<ResponseStructure<Restaurant>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("Restaurant not found with ID: " + restaurent.getId());
	}
	
	public ResponseEntity<ResponseStructure<Restaurant>> deleteRestaurant(Long id){
		
		Optional<Restaurant> opt= restaurentDao.getById(id);
		if(opt.isPresent()) {
			restaurentDao.deleteRestaurent(id);
			
			ResponseStructure<Restaurant> response = new ResponseStructure<Restaurant>();
			response.setStatusCode(HttpStatus.NO_CONTENT.value());
			response.setMessage("Restaurant deleted successfully");
			response.setData(null);
			
			return new ResponseEntity<ResponseStructure<Restaurant>>(response,HttpStatus.NO_CONTENT);
		}
		else
			throw new IdNotFoundException("Restaurant not found with ID: " + id);
	}
	
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByLocation(String location){
		
		List<Restaurant> res = restaurentDao.getByLocation(location);
		
		if(!res.isEmpty()) {
			ResponseStructure<List<Restaurant>> response = new ResponseStructure<List<Restaurant>>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurants retrieved successfully for location: " + location);
			response.setData(res);
			
			return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response,HttpStatus.OK);
		}
		else
			throw new ResourceNotFoundException("No restaurants found for location: " + location);
	}
	
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByName(String name){
		
		List<Restaurant> res = restaurentDao.getByName(name);
		
		if(!res.isEmpty()) {
			ResponseStructure<List<Restaurant>> response = new ResponseStructure<List<Restaurant>>();
			
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurants retrieved successfully for name: " + name);
			response.setData(res);
			
			return new ResponseEntity<ResponseStructure<List<Restaurant>>>(response,HttpStatus.OK);
		}
		else
			throw new ResourceNotFoundException("No restaurants found with name: " + name);
		
	}
	
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getMenuItem(Long id){
		
		Optional<Restaurant> opt = restaurentDao.getById(id);
		
		if(opt.isPresent()) {
			ResponseStructure<List<MenuItem>> response = new ResponseStructure<List<MenuItem>>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Menu items retrieved successfully for restaurant ID: " + id);
			response.setData(opt.get().getMenuItem());
			
			return new ResponseEntity<ResponseStructure<List<MenuItem>>>(response,HttpStatus.OK);
		}
		else
			throw new IdNotFoundException("Restaurant not found with ID: " + id);
	}
	
	public ResponseEntity<ResponseStructure<Page<Restaurant>>> getByPaginationAscending(int PageNumber,int PageSize,String field){
		
		Page<Restaurant> page = restaurentDao.getByPaginationAscending(PageNumber, PageSize, field);
		
			ResponseStructure<Page<Restaurant>> response = new ResponseStructure<Page<Restaurant>>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurants retrieved successfully (ascending order by " + field + ")");
			response.setData(page);
			
			return new ResponseEntity<ResponseStructure<Page<Restaurant>>>(response,HttpStatus.OK);
		
	}
	
	public ResponseEntity<ResponseStructure<Page<Restaurant>>> getByPaginationDescending(int PageNumber,int PageSize,String field){
		
		Page<Restaurant> page = restaurentDao.getByPaginationDescending(PageNumber, PageSize, field);
			
			ResponseStructure<Page<Restaurant>> response = new ResponseStructure<Page<Restaurant>>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Restaurants retrieved successfully (descending order by " + field + ")");
			response.setData(page);
			
			return new ResponseEntity<ResponseStructure<Page<Restaurant>>>(response,HttpStatus.OK);
		}
	
}
