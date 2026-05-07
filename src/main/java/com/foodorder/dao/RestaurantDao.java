package com.foodorder.dao;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.MenuItem;
import com.foodorder.entity.Restaurant;
import com.foodorder.repository.RestaurantRepository;

@Repository
public class RestaurantDao {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	public Restaurant creataRestaurent(Restaurant  restaurent) {
		return restaurantRepository.save(restaurent);
	}
	
	public List<Restaurant> getAllRestaurent(){
		return restaurantRepository.findAll();
	}
	
	public Optional<Restaurant> getById(Long id){
		return restaurantRepository.findById(id);
	}
	
	public Restaurant updateRestaurent(Restaurant restaurent) {
		return restaurantRepository.save(restaurent);
	}
	
	public void deleteRestaurent(Long id) {
		restaurantRepository.deleteById(id);
	}
	
	public List<Restaurant> getByLocation(String location){
		return restaurantRepository.findByLocation(location);
	}
	
	public List<Restaurant> getByName(String name){
		return restaurantRepository.findByName(name);
	}
	
	public Page<Restaurant> getByPaginationAscending(int PageNumber,int PageSize,String field){
		return restaurantRepository.findAll(PageRequest.of(PageNumber, PageSize, Sort.by(field).ascending()));
	}
	
	public Page<Restaurant> getByPaginationDescending(int PageNumber,int PageSize,String field){
		return restaurantRepository.findAll(PageRequest.of(PageNumber, PageSize, Sort.by(field).descending()));
	}
	
	public Boolean existsById(Long id) {
		return restaurantRepository.existsById(id);
	}

}
