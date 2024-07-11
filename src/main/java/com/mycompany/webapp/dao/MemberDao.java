package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.Recipe;

@Mapper
public interface MemberDao {
	//career, awards
	
   public int insert(Member member);   
   public Member selectByMid(String mid);   
   public int update(Member member);
   public int insertCareer(Career career);
   public int insertAwards(Awards awards);
   public List<Career> selectCareerBymid(String mid);
   public List<Awards> selectAwardsBymid(String mid);
   public Member selectMyProfile(String mid);
   public void updateNickname(Member member);
   public void updatePassword(Member member);
   public List<Classes> selectPastClassesByMid(String mid);
   public List<Classes> selectNowClassesByMid(String mid);
   public void deleteCareers(String mid);
   public List<Classes> selectPastClassesByMidCno(String mid);
   public List<Classes> selectNowClassesByMidCno(String mid);
   public void deleteAwards(String mid);
   public List<Qna> selectQnaByMid(String mid);
   public void updateimage(Member member);
   public void deleteImg(String mid);
   public void updateMrole(Member member);
   public void updateMphonenum(Member member);
   public Member selectBynameAndPhonenum(Member member);
   public List<Recipe> selectRecipeByMid(String mid);
   public int selectLikesCountByRno(int rno);
   public List<Recipe> selectRecipeByMidLikes(String mid);
   public String checkMid(String mid);
}