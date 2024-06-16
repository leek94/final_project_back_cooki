package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.service.RecipeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/recipe")
public class RecipeController {
	@Autowired
	private RecipeService recipeService;
	
	@GetMapping("/recipeList")
	public void recipeList() {
		
	}
	
	//PathVarialble로 bno 받음
	@GetMapping("/recipeDetail/{bno}")
	public void recipeDetail() {
		
	}
	
	@PostMapping("/recipeRegister")
	public void recipeRegister() {
		
	}
	
	@PutMapping("/recipeUpdate")
	public void recipeUpdate() {
		
	}
	
	//
	@DeleteMapping("/recipeDelete")
	public void recipeDelete() {
		
	}
	
	@PostMapping("/addLike")
	public void addLike() {
		
	}
	
	@DeleteMapping("/deleteLike")
	public void deleteLike() {
		
	}
	
	/*댓글*/
	
	@GetMapping("/reviewList")
	public void reviewList() {
		
	}
	
	@PostMapping("/reviewRegister")
	public void reviewRegister() {
		
	}
	
	@PutMapping("/reviewUpdate/{cno}")
	public void reviewUpdate() {
		
	}
	
	//
	@DeleteMapping("/reviewDelete/{cno}")
	public void reviewDelete() {
		
	}

}
