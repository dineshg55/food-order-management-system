package com.foodorder.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodorder.entity.MenuItem;
import com.foodorder.repository.MenuItemRepository;

@Repository
public class MenuItemDao {
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	public List<MenuItem> addMenuItems(List<MenuItem> menuItems){
		return menuItemRepository.saveAll(menuItems);
	}
	
	public List<MenuItem> getAllMenuItems(){
		return menuItemRepository.findAll();
	}
	
	public Optional<MenuItem> getById(Long id){
		return menuItemRepository.findById(id);
	}
	
	public MenuItem updateMenuItem(MenuItem menuItem) {
		return menuItemRepository.save(menuItem);
	}
	
	public void deleteMenuItem(Long id) {
		menuItemRepository.deleteById(id);
	}
	
	public List<MenuItem> getItemsGreaterThan(Double price){
		return menuItemRepository.findByPriceGreaterThan(price);
	}
	
	public Optional<MenuItem> getByName(String name){
		return menuItemRepository.findByItemName(name);
	}

}
