package com.mycompany.webapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class PrList {
	private List<RecipeProcess> processes;
	private int rno;
	private int initialLength;
	private int changeLength;

}
