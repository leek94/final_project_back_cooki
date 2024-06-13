package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class Career { //경력(에디터)
	private Integer cano; //번호
	private String cacontent; //내역
	private String memberMid; //->회원 아이디
}
