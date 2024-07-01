package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.dto.Participant;
import com.mycompany.webapp.dto.Qna;

@Mapper
public interface ClassDao {

	public int insertClass(Classes classes);
	public int insertClassThumbnail(ClassThumbnail classThumbnail);
	public int insertItem(ClassItem classItem);
	public int insertCurriculum(Curriculum curriculum); 
	public int updateBhitcount(int cno);
	public Classes selectByCno(int cno);
	public List<Curriculum> selectCurriculumByCno(int cno);
	public List<ClassItem> selectClassItemByCno(int cno);
	public ClassThumbnail selectByClassThumbnail(ClassThumbnail classThumb);
	public Curriculum selectByCurriculumimg(Curriculum curriculum);
	public int deleteClassThumbnail(int cno);
	public int selectByClassThumbCount(int cno);
	public int insertClassApply(Participant participant);
	public int deleteClassApply(Participant participant);
	public Participant selectByisParticipant(Participant participant);
	public int updateClassByCno(Classes classes);
	public int updateClassItemByCno(ClassItem classItem);
	public int deleteClassItemByCno(int cno);
	public int selectParticipantsCounttByCno(int cno);
	public int deleteCurriculumByCno(int cno);
	public int updateCurriculumByCno(Curriculum curriculum);
	public int selectCurriculumCountBycno(int cno);
	public int deleteCurriculumCountBycuorder(int cno, int cuorder);
	public int insertQna(Qna qna);
	public List<Qna> selectQnaByCno(int cno);
	public int updateQnaByQno(Qna qna);

}
