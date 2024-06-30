package com.mycompany.webapp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RecipeProcess { //레시피 과정
	private Integer rporder; //순서
	private String rptitle; //제목
	private String rpcontent; //내용
	private String rpimgoname; //이미지 파일 이름
	private byte[] rpimgdata; //이미지 파일 데이터
	private String rpimgtype; //이미지 파일 타입
	private Integer rno; //->레시피 번호
	
	private MultipartFile rpAttach;
	
}
