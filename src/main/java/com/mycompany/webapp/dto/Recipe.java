package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Recipe { //레시피
	private Integer rno; //번호
	private String rtitle; //제목
	private String rcontent; //내용
	private Integer rhitcount; //조회수
	private Date rdate; //등록 날짜
	private String memberMid; //->회원 아이디
	private byte[] rimgdata; //이미지 파일 데이터
	private String rimgtype; //이미지 파일 타입
	private String rimgoname; //이미지 파일 이릅
	private Integer categoryCtno; //카테고리 번호
}
