package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class RecipeItem { //레시피 재료
	private Integer rino; //번호
	private String riname; //이름
	private Integer recipeRno; //->레시피 번호
}
