package com.mycompany.webapp.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.Search;
import com.mycompany.webapp.service.ClassService;
import com.mycompany.webapp.service.RecipeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//레시피와 클래스에 대한 정보를 동시에 가지고 오는 controller
public class TotalController {
	@Autowired
	private ClassService classService;
	@Autowired
	private RecipeService recipeService;
	
	@RequestMapping("/")
	public String home() {
		return "restapi";
	}
	
	@PostMapping("/ClassSearch")
	public Map<String,Object> ClassSearch(@RequestBody Search search, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "12") int perPage) {
		int totalRows =classService.getSearchCount(search);
		log.info("갯수"+totalRows);
		//페이저 객체 생성
		Pager pager = new Pager(perPage, 5, totalRows, pageNo);
		List<Classes> searchClasses = classService.getSearchClasses(search, pager);
		Map<String, Object> map = new HashMap<>();
		map.put("searchClass", searchClasses);
		map.put("pager", pager);
		return map;
	}
	
	@GetMapping("/recipeSearch")
	public void recipeSearch() {
		
	}
	
	@PostMapping("/getTotalCount")
	public Map<String, Integer> getTotalCount(@RequestBody Search search){
		int searchClasses = classService.getSearchCount(search);
		int searchRecipes= recipeService.getTotalCount(search);
		Map<String, Integer> map = new HashMap<>();
		map.put("searchClasses", searchClasses);
		map.put("searchRecipes", searchRecipes);
		return map;
	}
	
	@GetMapping("/bestClassesRecipe")
	public Map<String,Object> bestClasses() {
		List<Classes> classes = classService.getBestClass();
		List<Recipe> recipe = recipeService.getBestRecipe();
		
		Map<String, Object> map = new HashMap<>();
		map.put("classes", classes);
		map.put("recipe", recipe);
		return map;
	}
		
}
