package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@PostMapping("/login")
	public void login() {
		
	}
	
	@PostMapping("/join")
	public void join() {
		
	}
	
	// 처음 마이페이지 화면
	@GetMapping("/myProfile")
	public void myProfile() {
		
	}
	
	@PutMapping("/myProfile/update")
	public void profileUpdate() {
		
	}
	
	@PostMapping("/EditorApply")
	public void EditorRegist() {
		
	}
	
	@GetMapping("/myRecipe")
	public void myRecipe() {
		
	}
	
	@GetMapping("/myLikeRecipe")
	public void myLikeRecipe() {
		
	}
	
	@GetMapping("/myQAndA")
	public void myQAndA() {
		
	}
	
	@GetMapping("/myClassHistory")
	public void myClassHistory() {
		
	}
	
	@GetMapping("/myNowClass")
	public void myNowClass() {
		
	}
	
	@GetMapping("/EditorNowRecruit")
	public void EditorNowRecruit() {
		
	}
	
	@GetMapping("/EditorRecruitHistory")
	public void EditorRecruitHistory() {
		
	}
	
	
	
	
	
	
	
	
	
	
}
