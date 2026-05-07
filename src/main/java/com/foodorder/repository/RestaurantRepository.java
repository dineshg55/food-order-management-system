package com.foodorder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodorder.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	
	public List<Restaurant> findByLocation(String location);
	
	public List<Restaurant> findByName(String name);
	

}
