package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class Category { //카테고리(클래스,레시피 공용)
	private Integer ctno; //번호 
	private String ctname; //이름
}
