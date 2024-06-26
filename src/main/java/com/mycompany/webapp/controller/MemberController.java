package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.mycompany.webapp.security.AppUserDetails;
import com.mycompany.webapp.security.JwtProvider;
import com.mycompany.webapp.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtProvider jwtProvider;
	@PostMapping("/login")
	public Map<String,String> login(@RequestBody Member member) {
		log.info("mid"+member.getMid());
		log.info("mpassword"+member.getMpassword());
		AppUserDetails userDetails = (AppUserDetails)userDetailsService.loadUserByUsername(member.getMid());
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean checkResult = passwordEncoder.matches(member.getMpassword(), userDetails.getMember().getMpassword());
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Map<String, String> map = new HashMap<>();
		if(checkResult) {
			String accessToken=jwtProvider.createAccessToken(member.getMid(), userDetails.getMember().getMrole());
			map.put("result", "success");
			map.put("mid", member.getMid());
			map.put("accessToken", accessToken);
			
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	@PostMapping("/join")
	public Map<String,String> join(@RequestBody Member member) {
		
		//비밀번호 암호화 
		PasswordEncoder passwordEncoder =PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		member.setMenabled(true);
		member.setMrole(member.getMrole());
		memberService.join(member);
		member.setMpassword(null);
		
		Map<String, String> map=new HashMap<>();
		map.put("result", "success");
		map.put("mid", member.getMid());
		
		
		return map;
	}
	@PostMapping("/setCareers")
	public Career setCareers(@RequestBody Career career ) {
		career.setMid(career.getMid());
		log.info("커리어란"+career);
		memberService.setCareer(career);
	
		return career;
	}
	@PostMapping("/setAwards")
	public Awards setAwards(@RequestBody Awards awards) {
		awards.setMid(awards.getMid());
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
