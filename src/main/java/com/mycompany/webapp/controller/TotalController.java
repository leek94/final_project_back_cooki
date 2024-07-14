package com.mycompany.webapp.controller;


import java.util.Collections;
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
@RequestMapping("/total")
//레시피와 클래스에 대한 정보를 동시에 가지고 오는 controller
public class TotalController {
	@Autowired
	private ClassService classService;
	@Autowired
	private RecipeService recipeService;
	

	@PostMapping("/ClassSearch")
	public Map<String,Object> ClassSearch(@RequestBody Search search, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "12") int perPage) {
		int totalRows =classService.getSearchCount(search);
		log.info("갯수"+totalRows);
		//페이저 객체 생성
		Pager pager = new Pager(perPage, 5, totalRows, pageNo);
		List<Classes> searchClasses = classService.getSearchClasses(search, pager);
		for(Classes classes : searchClasses) {
			int cno = classes.getCno();
			Float ratio = classService.getAvgCrratio(cno);
			classes.setCrratio(ratio == null? 0 : ratio);
			Integer count = classService.getReviewCount(cno);
			classes.setReviewCount( count == null? 0 : count);
		}
		if(search.getSearchSort() == 2) {
			log.info("sadasdas");
			Collections.sort(searchClasses, (p1, p2) -> Integer.compare(p2.getReviewCount(), p1.getReviewCount()));
		}
		if(search.getSearchSort()==3) {
			Collections.sort(searchClasses, (p1, p2) -> Float.compare(p2.getCrratio(), p1.getCrratio()));
		}
	
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
	public Map<String,Object> bestClassesRecipe(@RequestParam int number) {
		List<Classes> classes = classService.getBestClass(number);
		List<Recipe> recipe = recipeService.getBestRecipe(number);
		
		Map<String, Object> map = new HashMap<>();
		map.put("classes", classes);
		map.put("recipe", recipe);
		return map;
	}
		
}
