package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Qna;

@Mapper
public interface ReviewQnaDao {
	
	public int insertQna(Qna qna);
	public List<Qna> selectQnaByCno(int cno);
	public int updateQnaByQno(Qna qna);
	public int deleteQnaByQno(int qno);
	
}