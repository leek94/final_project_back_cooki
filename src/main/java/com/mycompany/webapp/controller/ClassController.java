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
import com.mycompany.webapp.dto.ClassReview;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.CuList;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.dto.Participant;
import com.mycompany.webapp.dto.ParticipantList;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/class")
public class ClassController {
	@Autowired
	private ClassService classService;
	
	
	// 신청 인원을 서버에서 확인
	@GetMapping("/classNowPerson/{cno}")
	public Map<String, Object> classNowPerson(@PathVariable int cno){
		// 현재 인원 수 몇명인 바로 리턴하기 위해서 서버에서 인원 확인
		int nowPerson = classService.getNowPerson(cno);
		Map<String, Object> map = new HashMap<>();
		map.put("nowPerson", nowPerson);
		
		return map;
	}
	
	// 신청인원이 마감되었는지 확인하는 메서드
	@GetMapping("/classOverPerson/{cno}/{cpersoncount}")
	public Map<String, Object> classOverPerson(@PathVariable int cno, @PathVariable int cpersoncount) {
		Map<String, Object> map = new HashMap<>();
		String result = classService.isOverPeople(cno, cpersoncount);
		// 마감인원이 넘었는지 확인
		
		if(result.equals("true")){
			map.put("result", "true");
		} else {
			map.put("result", "false");
		}
		return map;
	}
	
	@GetMapping("/classDetail/{cno}")
	public Map<String, Object> classDetail(@PathVariable int cno) {
		Map<String, Object> map = new HashMap<>();
		Classes classes = classService.getClasses(cno);
		classes.setChitcount(classes.getChitcount()+1);
		classService.updateChitcount(classes);
		map.put("classes", classes);
		
		return map;
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
	
	// 신청했는지 아닌지 확인하는 메서드 - 나중에 secured를 붙여서 로그인 페이지로 보낼 예정
	@GetMapping("/isParticipant/{cno}") 
	public Map<String, Object> isParticipant(@PathVariable int cno, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		if(authentication == null) {
			map.put("result", "backToLogin");
		} else {
			String mid = authentication.getName();
			Participant participant = new Participant();
			participant.setCno(cno);
			participant.setMid(mid);
			Participant isParticipant = classService.getIsparticipant(participant);
			
			if(isParticipant != null) {
				// 테이블에 값이 있으면 실패
				map.put("result", "false");
			} else {
				// 테이블에 값이 없으면 성공
				map.put("result", "success");
			}
		}
		return map;
	}
	
	//클래스 신청 여부 받아오기 (단순 문자열이나 숫자를 받을 때는 requestparam을 사용해야 함)
	@PostMapping("/classApply")
	public Map<String, Object> classApply(@RequestParam int cno, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		if(authentication == null) { // 로그인 안했을 경우 로그인 페이지로 던짐
			log.info("로그인 없이 리턴");
			map.put("result", "backToLogin");
		} else {  // 로그인 했을 경우
			String mid = authentication.getName(); // 로그인한 사람의 mid 값을 받아옴
			Participant participant = new Participant();
			participant.setCno(cno);
			participant.setMid(mid);
			classService.SetClassApply(participant); // cno, mid를 파라미터로 값 저장
			map.put("result", "success");
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
		classService.createClass(classes);
		
		//cno를 <front>로 전달하기 위해 map에 JSON 객체 형태로 저장해서 보내줌
		Map<String, Integer> map = new HashMap<>();
		//map("key", value) -> <front> response.data.cno(=key) -> 저장된 cno(value) 번호 알 수 있음
		map.put("cno", classes.getCno());
		//log.info("map 값"+ map.toString());
		return map;
	}
	
	@PostMapping("/itemRegister")
	public void itemRegister(@RequestBody List<ClassItem> classItems) {
		classService.createItem(classItems);
	}
	
	@PostMapping("/curriculumRegister")
	public void curriculumRegister(CuList cuList) {
		classService.createCurriculum(cuList);
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
		classService.updateClass(classes);
	}
	
	@PutMapping("/itemUpdate")
	public void itemUpdate(@RequestBody List<ClassItem> classItems) {
		classService.updateItem(classItems); 
	}
	
	@PutMapping("/curriculumUpdate")
	public void curriculumUpdate(Curriculum curriculum) {
		classService.updateCurriculum(curriculum);
	}
	
	// ----------------------------------- review -----------------------------------
	
	
	@GetMapping("/reviewList/{cno}")
	public Map<String, Object> reviewList(@PathVariable int cno, Authentication authentication) {
		log.info("컨트롤러 classReviewList 메소드 실행");
	    List<ClassReview> classReviewList = classService.getClassReviewList(cno);
	    Float avgCrratio = classService.getAvgCrratio(cno);
	    log.info("컨트롤러 avgCrratio 받아옴: " + avgCrratio);
	    Map<String, Object> map = new HashMap<>();
	    map.put("classReviewList", classReviewList);
	    map.put("avgCrratio", avgCrratio);
	    log.info("컨트롤러 classReviewList 받아옴");
	    return map;
	}
	
	@PostMapping("/reviewRegister")
	public void reviewRegister(@RequestBody ClassReview classReview, Authentication authentication) {
		log.info("컨트롤러 reviewRegister 메소드 실행");
		String mid = authentication.getName();
		classReview.setMid(mid);
		classService.createClassReview(classReview);
		log.info("컨트롤러 reviewRegister 클래스리뷰 객체 생성");
	}
	
	@PutMapping("/reviewUpdate")
	public void reviewUpdate(@RequestBody ClassReview classReview) {
		log.info("컨트롤러 reviewUpdate 메소드 실행");
		log.info("별점 확인: " + classReview);
		classService.updateClassReview(classReview);
		log.info("컨트롤러 reviewUpdate 클래스 리뷰 정보 업데이트");
	}
	
	//
	@DeleteMapping("/reviewDelete/{crno}")
	public void reviewDelete(@PathVariable int crno) {
		log.info("컨트롤러 reviewDelete 메소드 실행");
		classService.deleteClassReview(crno);
		log.info("컨트롤러 reviewDelete 클래스 리뷰 삭제");
		
		
	}
	
	
	// ----------------------------------- Q&A -----------------------------------
	
	@GetMapping("/qnaList/{cno}")
	public Map<String, Object> qnaList(@PathVariable int cno, Authentication authentication) {
		log.info("컨트롤러 qnaList 메소드 실행");
		//qna 목록을 list 형식으로 받아서 <front>로 넘겨줌
		List<Qna> qnaList = classService.getQnaList(cno);
		Map<String, Object> map = new HashMap<>();
		map.put("qnaList", qnaList);
		log.info("컨트롤러 qnaList 받아옴");
		return map;
	}
	
	@PostMapping("/qnaRegister")
	public void qnaRegister(@RequestBody Qna qna, Authentication authentication) {
		//json 형태의 dto 객체를 <front>에서 받아오기 위해서 @RequestBody 어노테이션 필요함
		log.info("컨트롤러 qnaRegister 메소드 실행");
		String mid = authentication.getName();
		qna.setMid(mid);
		classService.createQna(qna);
		log.info("컨트롤러 qnaRegister qna 객체 생성");
	}
	
	@PutMapping("/qnaUpdate")
	public void qnaUpdate(@RequestBody Qna qna) {
		log.info("컨트롤러 qnaUpdate 메소드 실행");
		classService.updateQna(qna);
		log.info("컨트롤러 qnaUpdate 클래스 Q&A 정보 업데이트");
	}
	
	//
	@DeleteMapping("/qnaDelete/{qno}")
	public void qnaDelete(@PathVariable int qno) {
		log.info("컨트롤러 qnaDelete 메소드 실행");
		classService.deleteQna(qno);
		log.info("컨트롤러 qnaDelete 클래스 Q&A 삭제");
	}
	
	@PutMapping("/qreplyUpdate") 
	public void qreplyUpdate(@RequestBody Qna qna) {
		log.info("컨트롤러 qreplyUpdate 메소드 실행");
		classService.updateQreply(qna);
		log.info("컨트롤러 qnaUpdate 클래스 Q&A qrely 정보 업데이트");
	}
	
	@GetMapping("/getParticipantList/{cno}")
	public Map<String, Object> getParticipantList(@PathVariable int cno) {
		log.info("cno값 확인"+ cno);
		List<ParticipantList> participantList = classService.getParticipantList(cno);
		log.info("리스트 값 받기"+ participantList);
		
		for(ParticipantList pl: participantList) {
			log.info("isparticipant 초기값: "+ pl.getIsParticipant());
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("participantList", participantList);
		return map;
		
	}
	
	@PutMapping("/updateParticipant")
	public void updateParticipant(@RequestBody Participant participant) {
		log.info("출석 확인: " + participant.getCno());
		log.info("출석 확인: " + participant.getMid());
		log.info("출석 확인: " + participant.getIsParticipant());
		
		classService.updateIsParticipant(participant);
		
		
	}
	
	// 사진 다운로드

}
