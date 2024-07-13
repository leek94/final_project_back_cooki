package com.mycompany.webapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.Search;
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
		Map<String, String> map = new HashMap<>();
		//AppUserDetailsService에서 재정의 된 메소드에서 mid로 멤버에 대한 정보를 가져옴 
		try {
			AppUserDetails userDetails = (AppUserDetails)userDetailsService.loadUserByUsername(member.getMid());
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			//사용자에게 입력 받은 비밀번호와 데이터 베이스에 있는 비밀번호를 비교 
			boolean checkResult = passwordEncoder.matches(member.getMpassword(), userDetails.getMember().getMpassword());
			//권한 부여 
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			 
			if(checkResult) {
				//jwtprovider에서 토큰 생성
				String accessToken=jwtProvider.createAccessToken(member.getMid(), userDetails.getMember().getMrole());
				//map을 사용해 결과값과 아이디 토큰을 전달 
				map.put("result", "success");
				map.put("mid", member.getMid());
				map.put("accessToken", accessToken);
				map.put("mrole", userDetails.getMember().getMrole());
				map.put("mimgoname", userDetails.getMember().getMimgoname());
				map.put("mnickname", userDetails.getMember().getMnickname());
			}else {
				map.put("result", "fail");
			}
		}catch ( UsernameNotFoundException e){
			log.info("서버에 등록되지 않은 mid이다");
			
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
	
	//return된 값을 front로 다시 전달해 줄 필요가 없기 때문에 void로 설정
	@PostMapping("/setCareers")
		public void setCareers(@RequestBody Career career) {
		memberService.setCareer(career);
	}
	
	@DeleteMapping("/careers/{mid}")
	public void careers(@PathVariable String mid) {
		memberService.deleteCareers(mid);
		log.info("커리어 삭제");
	}
	
	@PostMapping("/setAwards")
	public void setAwards(@RequestBody Awards awards) {
		memberService.setAwards(awards);
	}
	
	@DeleteMapping("/awards/{mid}")
	public void awards(@PathVariable String mid) {
		memberService.deleteAwards(mid);
		log.info("수상경력 삭제");
	}
	
	@GetMapping("/getCreatroInfo/{cno}")
	public Map<String, Object> getCreatroInfo(@PathVariable int cno){
		Classes classes = classService.getClasses(cno);
		String mnickname= classes.getMnickname();
		String mid= classes.getMid();
		String mimgoname = classes.getMimgoname();
		List<Career> career = memberService.getCareer(mid);
		List<Awards> awards = memberService.getAwards(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("mid", mid);
		map.put("mimgoname", mimgoname);
		map.put("mnickname", mnickname);
		map.put("career", career);
		map.put("awards", awards);
		return map;
	}
	
	// 처음 마이페이지 화면
	@GetMapping("/myProfile/{mid}")
	public Map<String, Object> myProfile(@PathVariable String mid) {
		Member member = memberService.getMyProfile(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("member", member);
		return map;
	}
	
	// 에디터 마이페이지 화면
	@GetMapping("/editorProfile/{mid}/{mrole}")
	public Map<String, Object> editorProfile(@PathVariable String mid, @PathVariable String mrole){
		List<Career> career = null;
		List<Awards> awards = null;
		
		if(mrole.equals("ROLE_EDITOR")) {
			career = memberService.getCareer(mid);
			awards = memberService.getAwards(mid);
		}
		Map<String, Object> map = new HashMap<>();
		
		map.put("career", career);
		map.put("awards", awards);
		
		return map;
	}	
	
	// 마이페이지 닉네임 업데이트
	@PutMapping("/updateNickname")
	public void updateNickname(@RequestBody Member member) {
		memberService.updateNickname(member);
	}
	
	// 마이페이지 패스워드 업데이트
	@PutMapping("/updatePassword")
	public void updatePassword(@RequestBody Member member) {
		// 비밀번호 인코딩하여 저장
		PasswordEncoder passwordEncoder =PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		
		memberService.updatePassword(member);
	}
	
	@PostMapping("/updateImg")
	public void updateImg(Member member) {
		memberService.updateimage(member); 
	}
	
	@GetMapping("/mattach/{mid}")
	public void mattach(@PathVariable String mid, HttpServletResponse response) {
		Member member = memberService.getMember(mid);
		log.info(mid);
			try {
				String fileName = new String(member.getMimgoname().getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("content-Disposition", "attachment; filename=\"" + fileName + "\"");
				response.setContentType(member.getMimgtype());
				OutputStream os;
				os = response.getOutputStream();
				os.write(member.getMimgdata());
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	@PutMapping("/updateMrole/{mid}")
	public void updateMrole(@PathVariable String mid) {
		// 객체 생성 및 에디터로 값 변경
		Member member = new Member();
		member.setMrole("ROLE_EDITOR");
		member.setMid(mid);
		
		memberService.updateMrole(member);
		log.info("에디터로 값 변경 성공");
	}
	
	@PutMapping("/updatePhonenum")
	public void updatePhonenum(@RequestBody Member member) {
		log.info("멤버 전화번호"+ member.getMphonenum());
		
		memberService.updateMphonenum(member);
		
	}
	
	@PutMapping("deleteImg/{mid}")
	public void deleteImg(@PathVariable String mid) {
		// 이름은 delete지만 update로 값을 null로 변경할 예정
		memberService.deleteImg(mid);
		
	}
	
	@PutMapping("/myProfile/update")
	public void profileUpdate() {
		
	}
	
	@PostMapping("/EditorApply")
	public void EditorRegist() {
		
	}
	
	@GetMapping("/myRecipe")
	public Map<String, Object> myRecipe(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getMyrecipeTotalCount(mid);
		Pager pager = new Pager(12, 5, totalCount, pageNo);
		List<Recipe> myRecipeList = memberService.getMyRecipe(mid,pager);
		Map<String, Object> map = new HashMap<>();
		map.put("myRecipeList", myRecipeList);
		map.put("pager", pager);
		return map;
	}
	
	@GetMapping("/myLikeRecipe")
	public Map<String, Object> myLikeRecipe(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getMyLikeRecipeTotalCount(mid);
		Pager pager = new Pager(12, 5, totalCount, pageNo);
		List<Recipe> myLikeRecipe = memberService.getLikeRecipe(mid,pager);
		Map<String, Object> map = new HashMap<>();
		map.put("myLikeRecipe", myLikeRecipe);
		map.put("pager", pager);
		return map;
	}
	
	@GetMapping("/myQAndA")
	public Map<String, Object> myQAndA(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getMyQAndATotalCount(mid);
		Pager pager = new Pager(8, 5, totalCount, pageNo);
		List<Qna> myQnaList = memberService.getMyQna(mid, pager);
		Map<String, Object> map = new HashMap<>();
		map.put("myQnaList", myQnaList);
		map.put("pager", pager);
		return map;
	}
	
	@GetMapping("/myClassHistory")
	public Map<String, Object> myClassHistory(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getMyClassHistoryTotalCount(mid);
		Pager pager = new Pager(12, 5, totalCount, pageNo);
		List<Classes> myClassList = memberService.getMyPastClass(mid, pager);
		//List<ClassThumbnail> myClassThumbnailList = memberService.getMyClassThumbnail(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("myClassList", myClassList);
		map.put("pager", pager);
		log.info("컨트롤러 myClassHistory 내가 수강했던 클래스 리스트 받아옴");
		return map;
	}
	
	@GetMapping("/myNowClass")
	public Map<String, Object> myNowClass(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getMyNowClassTotalCount(mid);
		Pager pager = new Pager(12, 5, totalCount, pageNo);
		List<Classes> myClassList = memberService.getMyNowClass(mid,pager);
		//List<ClassThumbnail> myClassThumbnailList = memberService.getMyClassThumbnail(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("myClassList", myClassList);
		map.put("pager", pager);
		log.info("컨트롤러 myNowClass 내가 수강 신청한 클래스 리스트 받아옴");
		return map;
	}
	
	@GetMapping("/editorNowRecruit")
	public Map<String, Object> editorNowRecruit(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getEditorNowRecruitTotalCount(mid);
		Pager pager = new Pager(8, 5, totalCount, pageNo);
		List<Classes> myClassList = memberService.getEditorNowClass(mid, pager);
		//List<ClassThumbnail> myClassThumbnailList = memberService.getMyClassThumbnail(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("myClassList", myClassList);
		map.put("pager", pager);
		log.info("컨트롤러 editorNowRecruit 내가 모집하고 있는 클래스 리스트 받아옴");
		return map;
	}
	
	@GetMapping("/editorRecruitHistory")
	public Map<String, Object> editorRecruitHistory(@RequestParam(defaultValue = "1") int pageNo, @RequestParam String mid) {
		int totalCount = memberService.getEditorRecruitHistoryTotalCount(mid);
		Pager pager = new Pager(8, 5, totalCount, pageNo);
		List<Classes> myClassList = memberService.getEditorPastClass(mid,pager);
		//List<ClassThumbnail> myClassThumbnailList = memberService.getMyClassThumbnail(mid);
		Map<String, Object> map = new HashMap<>();
		map.put("myClassList", myClassList);
		map.put("pager", pager);
		log.info("컨트롤러 editorRecruitHistory 에디터 모집했던 클래스 리스트 받아옴");
		return map;
	}
	
	// 아이디 찾기
	@PostMapping("/searchId")
	public Map<String, Object> searchId(@RequestBody Member member) {
		log.info("아이디 확인" + member.getMname());
		log.info("아이디 확인" + member.getMphonenum());
		
		// 이름과 전화번호를 통해서 mid를 받아와서 전달해줌
		Member memberSaved = memberService.getMemberBynameAndPhonenum(member);
		Map<String, Object> map = new HashMap<>();
		
		if(memberSaved == null) {
			log.info("값이 없음");
			map.put("memberSaved", null);
		} else {
			log.info("값이 있음"+memberSaved.getMid());
			map.put("memberSaved", memberSaved);
		}
		
		return map;
	}
	 
	// 비밀번호 찾기
	@PostMapping("/searchPw") 
	public Map<String, Object> searchPw(@RequestBody Member member) {
		log.info("비밀번호 확인" + member.getMid());
		
		Member memberSaved = memberService.getMember(member.getMid());
		Map<String, Object> map = new HashMap<>();
		
		if(memberSaved == null) {
			log.info("값이 없음");
			map.put("memberSaved", null);
		} else {
			log.info("값이 있음"+memberSaved.getMid());
			map.put("memberSaved", memberSaved);
		}
		
		return map;
	}
	
	// 아이디 중복검사
	@GetMapping("/idCheck/{mid}")
	public Map<String, Object> idCheck(@PathVariable String mid) {
		log.info("아이디 확인" + mid);
		
		String midSaved = memberService.checkMid(mid);
		log.info("로그 확인: " + midSaved);
		
		Map<String, Object> map = new HashMap<>();
	
		map.put("mid", midSaved);
		
		return map;
	}

}
