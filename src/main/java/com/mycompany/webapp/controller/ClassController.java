package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.webapp.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/class")
public class ClassController {
	@Autowired
	private ClassService classService;
}
