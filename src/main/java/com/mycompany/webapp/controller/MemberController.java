package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.security.AppUserDetails;
import com.mycompany.webapp.security.JwtProvider;
import com.mycompany.webapp.service.ClassService;
import com.mycompany.webapp.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private ClassService classService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtProvider jwtProvider;
	@PostMapping("/login")
	public Map<String,String> login(@RequestBody Member member) {
		//AppUserDetailsService에서 재정의 된 메소드에서 mid로 멤버에 대한 정보를 가져옴 
		AppUserDetails userDetails = (AppUserDetails)userDetailsService.loadUserByUsername(member.getMid());
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//사용자에게 입력 받은 비밀번호와 데이터 베이스에 있는 비밀번호를 비교 
		boolean checkResult = passwordEncoder.matches(member.getMpassword(), userDetails.getMember().getMpassword());
		//권한 부여 
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Map<String, String> map = new HashMap<>();
		if(checkResult) {
			//jwtprovider에서 토큰 생성
			String accessToken=jwtProvider.createAccessToken(member.getMid(), userDetails.getMember().getMrole());
			//map을 사용해 결과값과 아이디 토큰을 전달 
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
		//회원가잆 시 role을 받아오기(에디터로 가입하기 버튼을 누를 시 ROLE_EDITOR로 설정)
		member.setMrole(member.getMrole());
		memberService.join(member);
		//비밀번호는 사용자에게 보이지 않게 하기 위함
		member.setMpassword(null);
		//career와 awards를 받아오기 위해 결과와 mid를 map 형태로 전달 
		Map<String, String> map=new HashMap<>();
		map.put("result", "success");
		//에디터로 가입 시 career와 awards의 값을 저장하기 위해 mid 값을 전달 해줌
		map.put("mid", member.getMid());
		return map;
	}
	
	@PostMapping("/setCareers")
	//return된 값을 front로 다시 전달해 줄 필요가 없기 때문에 void로 설정
	public void setCareers(@RequestBody Career career ) {
		career.setMid(career.getMid());
		//커리어 값 insert
		memberService.setCareer(career);
	}
	
	@PostMapping("/setAwards")
	public void setAwards(@RequestBody Awards awards) {
		awards.setMid(awards.getMid());
		memberService.setAwards(awards);
	}
	
	@GetMapping("/getCreatroInfo/{cno}")
	public Map<String, Object> getCreatroInfo(@PathVariable int cno){
		log.info("cno"+cno);
		Classes classes = classService.getClasses(cno);
		String mnickname= classes.getMnickname();
		String mid= classes.getMid();
		log.info("mid"+mid);
		List<Career> career = memberService.getCareer(mid);
		List<Awards> awards = memberService.getAwards(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("mnickname", mnickname);
		map.put("career", career);
		map.put("awards", awards);
		return map;
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
