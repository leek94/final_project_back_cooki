package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class Search {
	private String searchTitle;
	private String searchText;
	private int searchSort;
}
