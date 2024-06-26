package com.mycompany.webapp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.ClassDao;
import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Curriculum;

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

}
