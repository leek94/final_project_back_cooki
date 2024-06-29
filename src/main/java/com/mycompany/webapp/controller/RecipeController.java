package com.mycompany.webapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.RecipeItem;
import com.mycompany.webapp.dto.RecipeProcess;
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
	
	//레시피 기본정보 등록폼
	@PostMapping("/recipeRegister")
	public Map<String, Object> recipeRegister(Recipe recipe) {
		//레시피 content에 들어가는 이미지 불러오기
		MultipartFile recipeImg = recipe.getRAttach();
		recipe.setRimgoname(recipeImg.getOriginalFilename());
		recipe.setRimgtype(recipeImg.getContentType());
		try {
			recipe.setRimgdata(recipeImg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		recipeService.insertRecipe(recipe);
		//레시피 아이템과 프로세스 등록을 위해 rno 값을 리턴
		int rno = recipe.getRno();
		Map<String, Object> map = new HashMap<>();
		map.put("rno",rno);
		return map;
	}
	//레시피 아이템 등록
	@PostMapping("/recipeItemRegister")
	public void recipeItemRegister(@RequestBody List<RecipeItem> recipeItem) {
		recipeService.insertRecipeItems(recipeItem);
	}
	//레시피 프로세스만 등록
	@PostMapping("/recipeProcessRegister")
	public void recipeProcessRegister(RecipeProcess recipeProcess) {
		MultipartFile rpimg= recipeProcess.getRpAttach();
		recipeProcess.setRpimgoname(rpimg.getOriginalFilename());
		recipeProcess.setRpimgtype(rpimg.getContentType());
		try {
			recipeProcess.setRpimgdata(rpimg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		recipeService.insertRecipeProcess(recipeProcess);
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
