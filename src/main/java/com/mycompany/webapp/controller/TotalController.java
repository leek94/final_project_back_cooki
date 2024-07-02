package com.mycompany.webapp.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Search;
import com.mycompany.webapp.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//레시피와 클래스에 대한 정보를 동시에 가지고 오는 controller
public class TotalController {
	@Autowired
	private ClassService classService;
	
	@RequestMapping("/")
	public String home() {
		return "restapi";
	}
	
	@GetMapping("/ClassSearch/{searchTitle}/{searchText}")
	public Map<String,Object> ClassSearch(@PathVariable String searchTitle, @PathVariable String searchText) {
		Search search= new Search();
		search.setSearchText(searchText);
		search.setSearchTitle(searchTitle);
		List<Classes> searchClasses = classService.getSearchClasses(search);
		Map<String, Object> map = new HashMap<>();
		return map;
	}
	
	@GetMapping("/recipeSearch")
	public void recipeSearch() {
		
	}
	
	@GetMapping("/bestClass")
	public void bestClass() {
		
	}
	
	@GetMapping("/bestRecipe")
	public void bestRecipe() {
		
	}
	
	
}
