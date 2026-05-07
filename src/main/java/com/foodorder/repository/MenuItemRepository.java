package com.foodorder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodorder.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
	
	public List<MenuItem> findByPriceGreaterThan(Double price);
	
	public Optional<MenuItem> findByItemName(String name);

}
