package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.ClassDao;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassService {
	
	@Autowired
	private ClassDao classDao;

	public void createClass(Classes classes) {
		log.info("서비스 createClass 메소드 실행");
		classes.setMid("test123@naver.com");
		classes.setCtno(1);
		classes.setCround(1);
		classDao.insertClass(classes);
		log.info("서비스 createClass insertClass");
		
		//set 해준 클래스 번호 가져오기
		int cno = classes.getCno();
		
		//front에서 가져온 썸네일 파일 모음(배열) 가져오기
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
	

	
}
