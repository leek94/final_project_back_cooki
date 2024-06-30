package com.mycompany.webapp.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CuList {
	private List<Curriculum> curriculums;
	private Integer cno;
}
