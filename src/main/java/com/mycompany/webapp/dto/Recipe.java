package com.mycompany.webapp.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Recipe { //레시피
	private Integer rno; //번호
	private String rtitle; //제목
	private String rcontent; //내용
	private Integer rhitcount; //조회수
	private Date rdate; //등록 날짜
	private String mid; //->회원 아이디
	private byte[] rimgdata; //이미지 파일 데이터
	private String rimgtype; //이미지 파일 타입
	private String rimgoname; //이미지 파일 이릅
	private Integer ctno; //카테고리 번호
	
	private MultipartFile rAttach;
	
	//category dto 삭제 
	private String ctname; //카테고리 이름
	private Boolean islike;
	private int reviewcount;
	private int likecount;
	private String mnickname;
}
