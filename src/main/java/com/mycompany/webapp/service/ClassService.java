package com.mycompany.webapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.ClassDao;
import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.dto.Participant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassService {
	
	@Autowired
	private ClassDao classDao;

	//클래스 기본 정보를 데이터베이스에 저장하기위한 로직
	public void createClass(Classes classes) {
		//<front>에서 axios를 통해 넘겨받지 못한 not null 필드값들을 service에서 설정해줘야 함
		log.info("서비스 createClass 메소드 실행");
		classes.setMid("test123@naver.com");
		classes.setCtno(1);
		classes.setCround(1);
		classDao.insertClass(classes);
		log.info("서비스 createClass insertClass");
		
		//set 해준 클래스 번호 가져오기
		int cno = classes.getCno();
		
		//<front>에서 axios로 연결된 cthumbnailimgs(배열 형태의 썸네일 파일 모음) 가져오기
		MultipartFile[] fileImgs = classes.getCthumbnailimgs();
		
		//파일 배열을 구조분해 해서 파일 객체로 for문을 통해 하나하나 저장 
		for(int i=0; i<fileImgs.length; i++) {
			//구조분해한 파일 객체를 저장해줄 dto 객체 선언
			ClassThumbnail classThumbnail = new ClassThumbnail();
			//객체별로 정보를 저장하기 위한 파일 객체 선언
			MultipartFile fileImg = fileImgs[i];
			//파일 객체에 정보 넣어주기
			classThumbnail.setCtorder(i+1);
			classThumbnail.setCtimgoname(fileImg.getOriginalFilename());
			classThumbnail.setCtimgtype(fileImg.getContentType());
			try {
				classThumbnail.setCtimgdata(fileImg.getBytes());
			} catch (Exception e) {
			}
			classThumbnail.setCno(cno);
			
			classDao.insertClassThumbnail(classThumbnail);
		}
		log.info("서비스 createClass insertClassThumbnail");	
	}
	
	//클래스 재료 정보를 데이터베이스에 저장하기위한 로직
	public void createItem(ClassItem classItem) {
		log.info("서비스 createItem 메소드 실행");
		classDao.insertItem(classItem);
		log.info("서비스 createItem insert classItem");
	}
	
	//클래스 커리큘럼 정보를 데이터베이스에 저장하기위한 로직
	public void createCurriculum(Curriculum curriculum) {
		log.info("서비스 createCurriculum 메소드 실행");
		
		//<front>에서 axios로 연결된 cuimg(배열X, <front>에서 하나씩 보내줌) 가져오기
		MultipartFile fileImg = curriculum.getCuimg();
		
		curriculum.setCuimgoname(fileImg.getOriginalFilename());
		curriculum.setCuimgtype(fileImg.getContentType());
		try {
			curriculum.setCuimgdata(fileImg.getBytes());
		} catch (IOException e) {
		}
		
		classDao.insertCurriculum(curriculum);
		log.info("서비스 createCurriculum insert curriculum");
	}
	//클래스 디테일 정보 받기 
	public Classes getClasses(int cno) {
		classDao.updateBhitcount(cno);
		Classes classes = classDao.selectByCno(cno);
		return classes;
	}
	
	public Participant getIsparticipant(Participant participant) {
		return classDao.selectByisParticipant(participant);
	}

	public List<Curriculum> getCurriculumList(int cno) {
		return classDao.selectCurriculumByCno(cno);
	}

	
	public List<ClassItem> getClassItemList(int cno) {
		return classDao.selectClassItemByCno(cno);
	}

	public ClassThumbnail getThumbnail(ClassThumbnail classThumb) {
		return classDao.selectByClassThumbnail(classThumb);
		
	}

	public Curriculum getCurriculumimg(Curriculum curriculum) {
		
		return classDao.selectByCurriculumimg(curriculum);
	}

	public int getThumbimgCount(int cno) {
		return classDao.selectByClassThumbCount(cno);
	}

	public int SetClassApply(Participant participant) {
		return classDao.insertClassApply(participant);
	}

	public int deleteClassApply(Participant participant) {
		return classDao.deleteClassApply(participant);
		
	}


	public int updateClass(Classes classes) {
		log.info("서비스 updateClass 메소드 실행");
		classes.setMid("test123@naver.com");
		// 썸네일 제외 data는 update로 진행
		// 썸네일 업데이트는 기존에 있던 data를 delete 하고 새로운 data를 insert 하는 방식으로 진행
		MultipartFile[] fileImgs = classes.getCthumbnailimgs();
		// 썸네일 이미지가 들어오는 경우
		if (fileImgs != null) {
			int cno = classes.getCno();
			// 썸네일 이미지 업데이트 1단계: 기존 썸네일 이미지 data 모두 delete
			int deleteResult = classDao.deleteClassThumbnail(cno);
			// 썸네일 이미지 업데이트 2단계: 새로운 썸네일 이미지 data insert
			for(int i=0; i<fileImgs.length; i++) {
				//구조분해한 파일 객체를 저장해줄 dto 객체 선언
				ClassThumbnail classThumbnail = new ClassThumbnail();
				//객체별로 정보를 저장하기 위한 파일 객체 선언
				MultipartFile fileImg = fileImgs[i];
				//파일 객체에 정보 넣어주기
				classThumbnail.setCtorder(i+1);
				classThumbnail.setCtimgoname(fileImg.getOriginalFilename());
				classThumbnail.setCtimgtype(fileImg.getContentType());
				try {
					classThumbnail.setCtimgdata(fileImg.getBytes());
				} catch (Exception e) {
				}
				classThumbnail.setCno(cno);
				classDao.insertClassThumbnail(classThumbnail);
			}
		}
		log.info("서비스 updateClass 클래스 기본 정보 업데이트 성공");
		return classDao.updateClassByCno(classes);
	}

	public void updateItem(List<ClassItem> classItems, int cno) {
		log.info("서비스 updateItem 실행");
		
		// 재료 업데이트 1단계: 기존 재료 data 모두 삭제하기
		int deleteResult = classDao.deleteClassItemByCno(cno);
		log.info("서비스 deleteClassItemByCno");
		
		// 재료 업데이트 2단계: 새로운 재료 data 생성하기
		for(int i=0; i<classItems.size(); i++) {
			//cname 저장하기
			ClassItem classItem = classItems.get(i);
			//cino 저장하기
			classItem.setCino(i+1);
			//cno 저장하기
			//재료가 모두 delete 되면 cno를 재료 테이블에서 받아올 수 없기 때문에 classes에서 받아온 cno로 넣어줘야함
			classItem.setCno(cno);	
			classDao.insertItem(classItem);
			log.info("서비스 updateItem 클래스 재료 정보 업데이트 성공");
		}
	}

	public boolean isOverPeople(int cno, int cpersoncount) {
		int participants = classDao.selectCpersoncountByCno(cno);
		if(participants<=cpersoncount) {
			return false;
		}else {
			return true;
		}
	}

	public void updateCurriculum(Curriculum curriculum, int cno) {
		log.info("서비스 updateCurriculum 실행");
		
		//이미지가 null이 아닐 경우 update
		MultipartFile fileImg = curriculum.getCuimg();
		
		if(fileImg != null) {
			curriculum.setCuimgoname(fileImg.getOriginalFilename());
			curriculum.setCuimgtype(fileImg.getContentType());
			try {
				curriculum.setCuimgdata(fileImg.getBytes());
			} catch (IOException e) {
			}
		}
		int updateResult = classDao.updateCurriculumByCno(curriculum);
		log.info("서비스 updateCurriculumByCno 클래스 커리큘럼 정보 업데이트 성공");
		

		
		//int deleteResult = classDao.deleteCurriculumByCno(cno);
		//log.info("서비스 updateCurriculum deleteCurriculumByCno 커리큘럼 정보 삭제 완료");
		
	}
}
