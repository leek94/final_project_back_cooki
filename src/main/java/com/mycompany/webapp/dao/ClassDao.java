package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Curriculum;

@Mapper
public interface ClassDao {

	public int insertClass(Classes classes);
	public int insertClassThumbnail(ClassThumbnail classThumbnail);
	public int insertItem(ClassItem classItem);
	public void insertCurriculum(Curriculum curriculum);
	public int updateBhitcount(int cno);
	public Classes selectByCno(int cno);
	
}
