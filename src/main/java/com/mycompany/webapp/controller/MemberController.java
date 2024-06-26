package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Member;
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
	public Member join(@RequestBody Member member) {
		//비밀번호 암호화 
		PasswordEncoder passwordEncoder =PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		member.setMenabled(true);
		member.setMrole(member.getMrole());
		memberService.join(member);
		member.setMpassword(null);
		return member;
	}
	@PostMapping("/setCareers")
	public Career setCareers(@RequestBody Career career,Authentication authentication ) {
		/*
		 * AppUserDetails userDetails =(AppUserDetails) authentication.getPrincipal();
		 * Member member = userDetails.getMember(); String mid =
		 * authentication.getName();
		 */
		career.setMid("angel1004@naver.com");
		memberService.setCareer(career);
		
		return career;
	}
	@PostMapping("/setAwards")
	public Awards setAwards(@RequestBody Awards awards,Authentication authentication ) {
		/*
		 * AppUserDetails userDetails =(AppUserDetails) authentication.getPrincipal();
		 * Member member = userDetails.getMember(); String mid =
		 * authentication.getName();
		 */
		awards.setMid("angel1004@naver.com");
		memberService.setAwards(awards);
		
		return awards;
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
