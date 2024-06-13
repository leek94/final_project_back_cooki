package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class Like {
	private String memberMid; //->회원 아이디
	private Integer recipeRno; //->레시피 번호
}
