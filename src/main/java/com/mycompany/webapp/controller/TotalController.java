package com.mycompany.webapp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//레시피와 클래스에 대한 정보를 동시에 가지고 오는 controller
public class TotalController {
	@RequestMapping("/")
	public String home() {
		return "restapi";
	}
}
