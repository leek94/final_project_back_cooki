package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Classes;
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
	public void classRegister(Classes classes) {	
		log.info("컨트롤러 classRegister 메소드 실행");
		classService.createClass(classes);
		log.info("컨트롤러 classRegister 클래스 객체 생성");
		
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
