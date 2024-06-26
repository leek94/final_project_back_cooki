package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/class")
public class ClassController {
	@Autowired
	private ClassService classService;
	
	@GetMapping("/classList")
	public void classList() {
		
	}
	
	//PathVarialble로 bno 받음
	@GetMapping("/classDetail/{cno}")
	public void classDetail() {
		
	}
	
	@PostMapping("/classRegister")
	public Map<String, Integer> classRegister(Classes classes) {	
		log.info("컨트롤러 classRegister 메소드 실행");
		classService.createClass(classes);
		log.info("컨트롤러 classRegister 클래스 객체 생성");
		
		//cno를 <front>로 전달하기 위해 map에 JSON 객체 형태로 저장해서 보내줌
		Map<String, Integer> map = new HashMap<>();
		//map("key", value) -> <front> response.data.cno(=key) -> 저장된 cno(value) 번호 알 수 있음
		map.put("cno", classes.getCno());
		log.info("map 값"+ map.toString());
		return map;
	}
	
	@PostMapping("/itemRegister")
	public void itemRegister(ClassItem classItem) {
		log.info("컨트롤러 itemRegister 메소드 실행");
		classService.createItem(classItem);
		log.info("컨트롤러 itemRegister 클래스아이템 객체 생성");
	}
	
	@PostMapping("/curriculumRegister")
	public void curriculumRegister(Curriculum curriculum) {
		log.info("컨트롤러 curriculumRegister 메소드 실행");
		classService.createCurriculum(curriculum);
		log.info("컨트롤러 curriculumRegister 커리큘럼 객체 생성");
	}
	
	@PutMapping("/classUpdate")
	public void classUpdate() {
		
	}
	
	// 삭제하기 고민중 - 모집 7일전까지만 삭제 가능하게
	
	/*댓글*/
	
	@GetMapping("/reviewList")
	public void reviewList() {
		
	}
	
	@PostMapping("/reviewRegister")
	public void reviewRegister() {
		
	}
	
	@PutMapping("/reviewUpdate/{rno}")
	public void reviewUpdate() {
		
	}
	
	//
	@DeleteMapping("/reviewDelete/{rno}")
	public void reviewDelete() {
		
	}
	
	@GetMapping("/qnaReviewList")
	public void qnaReviewList() {
		
	}
	
	@PostMapping("/qnaReviewRegister")
	public void qnaReviewRegister() {
		
	}
	
	@PutMapping("/qnaReviewUpdate/{qno}")
	public void qnaReviewUpdate() {
		
	}
	
	//
	@DeleteMapping("/qnaReviewDelete/{qno}")
	public void qnaReviewDelete() {
		
	}
	
	// 사진 다운로드

}
