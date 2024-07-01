package com.mycompany.webapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.CuList;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.dto.Participant;
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
	
	@GetMapping("/classNowPerson/{cno}")
	public Map<String, Object> classNowPerson(@PathVariable int cno){
		log.info("cno 확인:" + cno);
		// 인원수 몇명인 바로 리턴하기 위해서 서버에서 인원 확인
		int nowPerson = classService.getNowPerson(cno);
		log.info("현재 인원 확인"+ nowPerson);
		Map<String, Object> map = new HashMap<>();
		map.put("nowPerson", nowPerson);
		
		return map;
	}
	
	//PathVarialble로 bno 받음
	@GetMapping("/classDetail/{cno}")
	public Map<String, Object> classDetail(@PathVariable int cno, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		log.info("클래스 디테일 실행");
		Classes classes = classService.getClasses(cno);
		map.put("classes", classes);
		//로그인 하지 않은 사용자가 디테일 페이지를 볼 수 있게 한다
		if(authentication == null) {
			log.info("로그인 없이 클래스 디테일 실행");
			map.put("result", "backToLogin");
			
			return map;
		//사용자가 로그인했다면 클래스를 신청했는지 여부를 받아온다
		} else {
			//
			String mid = authentication.getName();
			// 클래스 디테일 정보 가져오기
			
			
			// 클래스 신청 여부를 가져오기 
			Participant participant= new Participant();
			log.info("로그인한 인원 이메일: " + mid);
			participant.setCno(cno);
			participant.setMid(mid);
			Participant isParticipant = classService.getIsparticipant(participant);
			//클래스 인원 마감 여부를 받아오기 위해 cno와 클래스 제한 인원을 매개 변수로 전달
			Map<String,Object> overPeople = classService.isOverPeople(cno, classes.getCpersoncount());
			
			
			//신청마감 여부의 리턴 값이 false일 때 fail
			if(overPeople.get("result").equals(false) ) {
				map.put("result", "fail");
			} else {
				map.put("result","success");
			}
			//클래스 정보와 사용자가 클래스를 신청했는 지 여부와 신청 마감 여부를 map형태로 전달 
			
			map.put("isParticipant", isParticipant);
			map.put("participants", overPeople.get("participants"));
			return map;
		}
		
	}

	//클래스 써메니일 갯수 받아오기
	@GetMapping("/getThumbimgCount/{cno}")
	public int getThumbimgCount(@PathVariable int cno) {
		int cont = classService.getThumbimgCount(cno);
		return  cont;
	}
	
	//클래스 썸네일 이미지 다운로드
	@GetMapping("/thumbattach/{cno}/{ctorder}")
	public void downloadThumb(@PathVariable int cno, @PathVariable int ctorder, HttpServletResponse response) {
		ClassThumbnail classThumb = new ClassThumbnail();
		classThumb.setCno(cno);
		classThumb.setCtorder(ctorder);
		classThumb=classService.getThumbnail(classThumb);
		
		try {
			String fileName = new String(classThumb.getCtimgoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("content-Disposition", "attachment; filename=\""+fileName+"\"");
			response.setContentType(classThumb.getCtimgtype());
			OutputStream os;
			os = response.getOutputStream();
			os.write(classThumb.getCtimgdata());
			os.flush();
			os.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//커리큘럼 이미지 다운로드
	@GetMapping("/curriculumattach/{cno}/{cuorder}")
	public void downloadCurriculum(@PathVariable int cno, @PathVariable int cuorder, HttpServletResponse response) {
		Curriculum curriculum = new Curriculum();
		curriculum.setCno(cno);
		curriculum.setCuorder(cuorder);
		curriculum = classService.getCurriculumimg(curriculum);
		
		try {
			String fileName = new String(curriculum.getCuimgoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("content-Disposition", "attachment; filename=\""+fileName+"\"");
			response.setContentType(curriculum.getCuimgtype());
			OutputStream os;
			os = response.getOutputStream();
			os.write(curriculum.getCuimgdata());
			os.flush();
			os.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//클래스 신청 여부 받아오기 (단순 문자열이나 숫자를 받을 때는 requestparam을 사용해야 함)
	@PostMapping("/classApply")
	public Map<String, Object> classApply(@RequestParam int cno, @RequestParam int cpersoncount, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		log.info("로그인 없이 버튼 클릭");
		// 사용자가 로그인했다면 클래스를 신청했는지 여부를 받아온다
		if (authentication == null) {
			log.info("로그인 없어서 리턴");
			map.put("result", "backToLogin");
		} else {
			// 신청 인원 확인을 위한 로직
			Map<String, Object> overPeople = classService.isOverPeople(cno, cpersoncount);
			String mid = authentication.getName();

			Participant participant = new Participant();
			participant.setCno(cno);
			participant.setMid(mid);

			Participant isParticipant = classService.getIsparticipant(participant);

			
			if (overPeople.get("result").equals(false)) {
				map.put("result", "fail");
			} else {
				if (isParticipant != null) {
					map.put("result", "fail");
					map.put("isParticipant", isParticipant);
				} else {
					map.put("result", "success");
					int ClassApply = classService.SetClassApply(participant);
					map.put("classApply", ClassApply);
				}

			}
		}
		return map;
	}
	
	@DeleteMapping("/deleteClassApply/{cno}")
	public void deleteClassApply(@PathVariable int cno, Authentication authentication) {
		String mid = authentication.getName();
		Participant participant = new Participant();
		
		participant.setCno(cno);
		participant.setMid(mid);
		
		classService.deleteClassApply(participant);
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
		//log.info("map 값"+ map.toString());
		return map;
	}
	
	@PostMapping("/itemRegister")
	public void itemRegister(@RequestBody List<ClassItem> classItems) {
		log.info("컨트롤러 itemRegister 메소드 실행");
		classService.createItem(classItems);
		log.info("컨트롤러 itemRegister 클래스아이템 객체 생성");
	}
	
	@PostMapping("/curriculumRegister")
	public void curriculumRegister(CuList cuList) {
		log.info("컨트롤러 curriculumRegister 메소드 실행");
		classService.createCurriculum(cuList);
		log.info("컨트롤러 curriculumRegister 커리큘럼 객체 생성");
	}
	
	// 삭제하기 고민중 - 모집 7일전까지만 삭제 가능하게
	
	//path variable 방식으로
	@GetMapping("/getCurriculumAndItem/{cno}")
	public Map<String, Object> getCurriculumAndItem(@PathVariable int cno) {
		List<Curriculum> curriculums = classService.getCurriculumList(cno);
		List<ClassItem> classItems = classService.getClassItemList(cno);
		//<front>에 data를 map 타입으로 보내줌
		Map<String, Object> map = new HashMap<>();
		map.put("curriculums", curriculums);
		map.put("classItems", classItems);
		return map;
	}
	
	@PutMapping("/classUpdate")
	public void classUpdate(Classes classes) {
		log.info("컨트롤러 classUpdate 메소드 실행");
		classService.updateClass(classes);
		log.info("컨트롤러 classUpdate 클래스 기본 정보 업데이트");
	}
	
	@PutMapping("/itemUpdate")
	public void itemUpdate(@RequestBody List<ClassItem> classItems) {
		log.info("컨트롤러 itemUpdate 메소드 실행");
		classService.updateItem(classItems); 
		log.info("컨트롤러 itemUpdate 클래스 재료 정보 업데이트");
	}
	
	@PutMapping("/curriculumUpdate")
	public void curriculumUpdate(Curriculum curriculum) {
		log.info("컨트롤러 curriculumUpdate 메소드 실행");
		classService.updateCurriculum(curriculum);
		log.info("컨트롤러 curriculumUpdate 클래스 커리큘럼 정보 업데이트");
	}
	
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
