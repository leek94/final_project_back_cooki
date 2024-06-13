package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class Awards { //수상내역(에디터)
	private Integer ano; //번호
	private String acontent; //내역
	private String memberMid; //->회원 아이디(이메일 사용)
}
