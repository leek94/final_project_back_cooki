package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ClassDao;

@Service
public class ClassService {
	
	@Autowired
	private ClassDao classDao;
	
	
}
