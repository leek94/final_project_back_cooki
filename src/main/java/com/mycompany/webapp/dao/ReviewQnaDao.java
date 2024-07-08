package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.ClassReview;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.RecipeReview;

@Mapper
public interface ReviewQnaDao {
	
	public int insertQna(Qna qna);
	public List<Qna> selectQnaByCno(int cno);
	public int updateQnaByQno(Qna qna);
	public int deleteQnaByQno(int qno);
	public int updateQreplyByQno(Qna qna);
	public int insertClassReview(ClassReview classReview);
	public List<ClassReview> selectClassReviewByCno(int cno);
	public float selectAvgCrratioByCno(int cno);
	public int updateClassReviewByCrno(ClassReview classReview);
	public int deleteClassReviewByCrno(int crno);
	public int insertRecipeReview(RecipeReview recipeReview);
	public int updateRecipeReview(RecipeReview recipeReview);
	public int deleteRecipeReview(int rrno);
	public List<RecipeReview> selectRecipeReviewsByRno(int rno);
	
}
