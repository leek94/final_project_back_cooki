package com.mycompany.webapp.dao;

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


}