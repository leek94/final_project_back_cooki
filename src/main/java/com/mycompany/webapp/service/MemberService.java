package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Member;

@Service
public class MemberService {
   @Autowired
   private MemberDao memberDao;
   
   public void join(Member member) {
	   memberDao.insert(member);
   }

   public void setCareer(Career career) {
	   memberDao.insertCareer(career);
	
   }

public void setAwards(Awards awards) {
	memberDao.insertAwards(awards);
	
}
}