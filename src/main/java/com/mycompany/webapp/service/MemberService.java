package com.mycompany.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Member;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

public List<Career> getCareer(String mid) {
	List<Career> career = memberDao.selectCareerBymid(mid);
	return career;
}
public List<Awards> getAwards(String mid) {
	List<Awards> awards = memberDao.selectAwardsBymid(mid);
	return awards;
}

// 일반 유저가 수강했었던 클래스 가져오기
public List<Classes> getMyPastClass(String mid) {
	List<Classes> classes = memberDao.selectPastClassesByMidCno(mid);
	log.info("class리스트" + classes);
	return classes;
	
}

// 일반 유저가 수강 신청한 클래스 가져오기
public List<Classes> getMyNowClass(String mid) {
	List<Classes> classes = memberDao.selectNowClassesByMidCno(mid);
	log.info("class리스트" + classes);
	return classes;
}

// 에디터가 과거 모집했었던 클래스 가져오기
public List<Classes> getEditorPastClass(String mid) {
	List<Classes> classes = memberDao.selectPastClassesByMid(mid);
	log.info("class리스트" + classes);
	return classes;
}

// 에디터가 현재 모집중인 클래스 가져오기
public List<Classes> getEditorNowClass(String mid) {
	List<Classes> classes = memberDao.selectNowClassesByMid(mid);
	log.info("class리스트" + classes);
	return classes;
}

}