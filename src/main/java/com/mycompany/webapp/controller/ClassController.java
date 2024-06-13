package com.mycompany.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mycompany.webapp.service.ClassService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ClassController {
	@Autowired
	private ClassService classService;
}
