package com.foodorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.dto.ResponseStructure;
import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Restaurant;
import com.foodorder.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Restaurant>> createRestaurent(@RequestBody Restaurant restaurent){
		return restaurantService.createRestaurant(restaurent);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getAllRestaurent(){
		return restaurantService.getAllRestaurant();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Restaurant>> getById(@PathVariable Long id){
		return restaurantService.getById(id);
	}
	
	@PatchMapping
	public ResponseEntity<ResponseStructure<Restaurant>> updateRestaurent(@RequestBody Restaurant restaurent){
		return restaurantService.updateRestaurant(restaurent);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Restaurant>> deleteRestaurent(@PathVariable Long id){
		return restaurantService.deleteRestaurant(id);
		
	}
	
	@GetMapping("/location/{location}")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByLocation(@PathVariable String location){
		return restaurantService.getByLocation(location);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<ResponseStructure<List<Restaurant>>> getByName(@PathVariable String name){
		return restaurantService.getByName(name);
	}
	
	@GetMapping("/menuItem/{id}")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getMenuItem(@PathVariable Long id){
		return restaurantService.getMenuItem(id);
	}
	
	@GetMapping("/ascending/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Restaurant>>> getByPaginationAscending(@PathVariable int pageNumber,int pageSize,String field){
		return restaurantService.getByPaginationAscending(pageNumber, pageSize, field);
	}
	
	@GetMapping("/descending/{pageNumber}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<Restaurant>>> getByPaginationDescending(@PathVariable int pageNumber,int pageSize,String field){
		return restaurantService.getByPaginationDescending(pageNumber, pageSize, field);
	}
}
