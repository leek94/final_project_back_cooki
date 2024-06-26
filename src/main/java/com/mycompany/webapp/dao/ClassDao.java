package com.mycompany.webapp.dao;

import java.util.List;

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
	public List<Curriculum> selectCurriculumByCno(int cno);
	public List<ClassItem> selectClassItemByCno(int cno);
	
}
