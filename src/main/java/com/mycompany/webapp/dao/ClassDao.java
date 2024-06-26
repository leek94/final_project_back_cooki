package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;

@Mapper
public interface ClassDao {

	public int insertClass(Classes classes);
	public int insertClassThumbnail(ClassThumbnail classThumbnail);
	
}
