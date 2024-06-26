package com.mycompany.webapp.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Classes { //클래스
	private Integer cno; //번호
	private String ctitle; //제목
	private String ccontent; //내용
	private Integer cpersoncount; //참여자수
	private Integer cprice; //수강료
	private String caddress; //수업장소
	private Date cstartdate; //모집 시작일
	private Date cenddate; //모집 마감일
	private Date cdday; //강의 시작일
	private String cstarttime; //강의 시작 시간
	private String cendtime; //강의 끝나는 시간
	private Integer chitcount; //조회수
	private Integer cround; //동일 클래스 기수
	private String mid; //->회원 아이디 (이메일 사용)
	private Integer ctno; //->카테고리 번호
	
	//category dto 삭제 
	private String ctname; //카테고리 이름
	
	//participant dto 삭제
	private Date pdate; //참여 신청일
	
	private MultipartFile[] cthumbnailimgs; //썸네일 이미지 배열
}
