package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Participant { //수강 참여자
	private String memberMid; //->회원 아이디
	private Integer classCno; //->클래스 번호
	private Date pdate; //수강 등록일
}
