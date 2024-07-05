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
	
	@PostMapping("/ClassSearch")
	public Map<String,Object> ClassSearch(@RequestBody Search search, @RequestParam(defaultValue = "1") int pageNo) {
		
		int totalRows =classService.getSearchCount(search);
		log.info("갯수"+totalRows);
		//페이저 객체 생성
		Pager pager = new Pager(12, 5, totalRows, pageNo);
		List<Classes> searchClasses = classService.getSearchClasses(search, pager);
		Map<String, Object> map = new HashMap<>();
		map.put("searchClass", searchClasses);
		map.put("pager", pager);
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
