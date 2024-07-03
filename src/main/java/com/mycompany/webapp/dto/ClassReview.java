package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClassReview { //클래스 리뷰
	private Integer crno; //번호
	private String crtitle; //제목
	private String crcontent; //내용
	private Integer crratio; //별점
	private Date crdate; //작성일
	private Integer cno; //->클래스 번호
	private String mid; //->회원 아이디
}
