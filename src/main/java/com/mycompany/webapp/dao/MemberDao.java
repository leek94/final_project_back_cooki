package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Member;

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

}