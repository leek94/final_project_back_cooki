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
	
	//PathVarialble로 bno 받음
	@GetMapping("/classDetail/{cno}")
	public Map<String, Object> classDetail(@PathVariable int cno, Authentication authentication) {
		//로그인 시 신청 여부를 판단하여 가져오기
		if(authentication == null) {
			 
			return null;
		} else {
			String mid = authentication.getName();
			// 클래스 값 받아오기
			Classes classes = classService.getClasses(cno);
			//클래스 인원 마감 여부 받아오기
			Map<String,Object> overPeople = classService.isOverPeople(cno, classes.getCpersoncount());
			
			Map<String, Object> map = new HashMap<>();
			log.info("lf"+overPeople.get("result"));
			
			if(overPeople.get("result").equals(false) ) {
				log.info("실행실패");
				map.put("result", "fail");
			} else {
				log.info("실행성공");
				map.put("result","success");
			}
			log.info("ee"+overPeople.get("participants"));
			// 클래스 신청 여부 가져오기 
			Participant participant= new Participant();
			participant.setCno(cno);
			participant.setMid(mid);
			Participant isParticipant = classService.getIsparticipant(participant);
			map.put("classes", classes);
			// 신청했는지 여부
			map.put("isParticipant", isParticipant);
			map.put("participants", overPeople.get("participants"));
			return map;
		}
		
	}
	
	//클래스 신청 여부 받아오기 (단순 문자열이나 숫자를 받을 때는 requestparam을 사용해야 함)
	@PostMapping("/classApply")
	public Map<String, Object> classApply(@RequestParam int cno, @RequestParam int cpersoncount, Authentication authentication) {
		Map<String,Object> overPeople = classService.isOverPeople(cno, cpersoncount);
		String mid = authentication.getName();
		Participant participant = new Participant();
		participant.setCno(cno);
		participant.setMid(mid);
		Map<String, Object> map = new HashMap<>();
		if(overPeople.get("result").equals(false)) {
			map.put("result", "fail");
		} else {
			map.put("result","success");
			int ClassApply=classService.SetClassApply(participant);
			map.put("classApply",ClassApply);
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
	
	//클래스 써메니일 갯수 받아오기
	@GetMapping("/getThumbimgCount/{cno}")
	public int getThumbimgCount(@PathVariable int cno) {
		int cont = classService.getThumbimgCount(cno);
		return  cont;
	}
	
	//클래스 썸네일 이미지 다운로드
	@GetMapping("/thumbattach/{cno}/{ctorder}")
	public void downloadThumb(@PathVariable int cno, @PathVariable int ctorder, HttpServletResponse response) {
		log.info("아무거");
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
	public void itemRegister(@RequestBody List<ClassItem> classItems) {
		log.info("컨트롤러 itemRegister 메소드 실행");
		classService.createItem(classItems);
		log.info("컨트롤러 itemRegister 클래스아이템 객체 생성");
	}
	
	@PostMapping("/curriculumRegister")
	public void curriculumRegister(Curriculum curriculum) {
		log.info("컨트롤러 curriculumRegister 메소드 실행");
		classService.createCurriculum(curriculum);
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
