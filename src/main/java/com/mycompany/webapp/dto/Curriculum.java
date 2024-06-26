package com.mycompany.webapp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Curriculum { //커리큘럼
	private Integer cuorder; //순서
	private String cutitle; //제목
	private String cucontent; //내용
	private String cuimgoname; //이미지 파일 이름
	private byte[] cuimgdata; //이미지 파일
	private String cuimgtype; //이미지 파일 종류
	private Integer cno; //->클래스 번호
	
	private MultipartFile cuimg;
}
