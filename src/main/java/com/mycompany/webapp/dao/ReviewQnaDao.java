package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.ClassReview;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.RecipeReview;

@Mapper
public interface ReviewQnaDao {
	
	public int insertQna(Qna qna);
	public int selectQnaCountByCno(Classes classes);
	public List<Qna> selectQnaByCnoPager(Classes classes, @Param("pager") Pager pager);
	public int updateQnaByQno(Qna qna);
	public int deleteQnaByQno(int qno);
	public int updateQreplyByQno(Qna qna);
	public int insertClassReview(ClassReview classReview);
	public int selectReviewCountByCno(Classes classes);
	public List<ClassReview> selectClassReviewByCnoPager(Classes classes, Pager pager);
	public Float selectAvgCrratioByCno(Classes classes);
	public int updateClassReviewByCrno(ClassReview classReview);
	public int deleteClassReviewByCrno(int crno);
	public int insertRecipeReview(RecipeReview recipeReview);
	public int updateRecipeReview(RecipeReview recipeReview);
	public int deleteRecipeReview(int rrno);
	public List<RecipeReview> selectRecipeReviewsByRno(int rno, Pager pager);
	public int selectReviewCount(int rno);

	
}
