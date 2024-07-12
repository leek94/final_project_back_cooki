package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Participant { //수상내역(에디터)
	private String mid; // 회원 아이디(이메일 사용)
	private int cno; // 클래스 넘버
	private Date pdate; // 신청 날짜, 시간
	private char isParticipant; // 출석 여부
}
