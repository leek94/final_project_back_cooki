package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Qna { //문의하기
	private Integer qno; //번호
	private String qtitle; //제목
	private String qcontent; //내용
	private Date qdate; //날짜
	private String qreply; //답변
	private String mid; //->회원 아이디
	private Integer cno; //->클래스 번호
	private Date qreplydate; //답변 날짜

	private String mnickname;
	private String ctitle;
	private String mimgoname;
}
