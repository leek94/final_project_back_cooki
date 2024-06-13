package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Class { //클래스
	private Integer cno; //번호
	private String ctitle; //제목
	private String ccontent; //내용
	private Integer cpersoncount; //참여자수
	private Integer cprice; //수강료
	private String caddress; //수업장소
	private Date cstartdate; //모집 시작일
	private Date cenddate; //모집 마감일
	private Date cdday; //강의 시작일
	private double ctime; //강의 시간
	private Integer chitcount; //조회수
	private Integer cround; //동일 클래스 기수
	private String memberMid; //->회원 아이디
	private Integer categoryCtno; //->카테고리 번호
	private byte[] cimgdata; //이미지 파일 
	private String cimgtype; //이미지 파일 타입
	private String cimgoname; //이미지 파일 이름
}
