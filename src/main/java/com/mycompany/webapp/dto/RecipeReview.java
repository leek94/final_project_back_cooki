package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RecipeReview { //레시피 리뷰
	private Integer rrno; //번호
	private String rrtitle; //제목
	private String rrcontent; //내용
	private Date rrdate; //작성일
	private String mid; //->회원 아이디
	private Integer rno; //->레시피 번호
	
	private String mnickname;
	private String mimgoname;
}
