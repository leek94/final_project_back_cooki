package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class ClassThumbnail { //클래스 썸네일
	private Integer ctorder; //순서
	private String ctimgoname; //이미지 파일 이름
	private byte[] ctimgdata; //이미지 파일
	private String ctimgtype; //이미지 파일 종류
	private Integer cno; //->클래스 번호
}
