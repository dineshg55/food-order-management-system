package com.foodorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.foodorder.service.MenuItemService;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {
	
	@Autowired
	private MenuItemService menuItemService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<List<MenuItem>>> addMenuItems(@RequestBody List<MenuItem> menuItems){
		return menuItemService.addMenuItems(menuItems);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getAllMenuItems(){
		return menuItemService.getAllMenuItem();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<MenuItem>> getById(@PathVariable Long id){
		return menuItemService.getById(id);
	}
	
	@PatchMapping
	public ResponseEntity<ResponseStructure<MenuItem>> updateMenuItem(@RequestBody MenuItem menuItem){
		return menuItemService.updateMenuItem(menuItem);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<MenuItem>> deleteMenuItem(@PathVariable Long id){
		return menuItemService.deleteMenuItem(id);
	}
	
	@GetMapping("/priceGreaterThan/{price}")
	public ResponseEntity<ResponseStructure<List<MenuItem>>> getByPriceGreaterThan(@PathVariable Double price){
		return menuItemService.getByPriceGreaterThan(price);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<ResponseStructure<MenuItem>> getByName(@PathVariable String name){
		return menuItemService.getByName(name);
	}
	

}
