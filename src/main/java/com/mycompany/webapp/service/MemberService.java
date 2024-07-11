package com.mycompany.webapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dto.Awards;
import com.mycompany.webapp.dto.Career;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.Recipe;

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

	public Member getMember(String mid) {
		return memberDao.selectByMid(mid);
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

	public Member getMyProfile(String mid) {
		return memberDao.selectMyProfile(mid);

	}

	public void updateNickname(Member member) {
		memberDao.updateNickname(member);

	}

	public void updatePassword(Member member) {
		memberDao.updatePassword(member);
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

	public void deleteCareers(String mid) {
		memberDao.deleteCareers(mid);

	}

	public void deleteAwards(String mid) {
		memberDao.deleteAwards(mid);

	}

	// 내가 문의한 댓글 내역 가져오기
	public List<Qna> getMyQna(String mid) {
		List<Qna> qna = memberDao.selectQnaByMid(mid);
		log.info("qna리스트: " + qna);
		log.info("qna리스트: " + qna.size());
		return qna;
	}

	public void updateimage(Member member) {
		log.info("서비스 updateimage");

		if (member.getMattach() != null && !member.getMattach().isEmpty()) {
			MultipartFile fileImg = member.getMattach();
			// 파일 이름을 설정
			member.setMimgoname(fileImg.getOriginalFilename());
			// 파일 종류를 설정
			member.setMimgtype(fileImg.getContentType());
			try {
				// 파일 데이터를 설정
				member.setMimgdata(fileImg.getBytes());
			} catch (IOException e) {
			}
		}
		// DB에 저장
		memberDao.updateimage(member);

		// 파일 배열을 구조분해 해서 파일
	}

	public void deleteImg(String mid) {
		memberDao.deleteImg(mid);
	}

	public void updateMrole(Member member) {
		memberDao.updateMrole(member);
	}

	public void updateMphonenum(Member member) {
		memberDao.updateMphonenum(member);
		
	}

	public Member getMemberBynameAndPhonenum(Member member) {
		return memberDao.selectBynameAndPhonenum(member);
		
	}

	public List<Recipe> getMyRecipe(String mid) {
		List<Recipe> recipeList = memberDao.selectRecipeByMid(mid);
	
		for(Recipe recipe : recipeList) {
			int rno = recipe.getRno();
			int likesCount = memberDao.selectLikesCountByRno(rno);
			recipe.setLikecount(likesCount);
		}
		log.info("recipe리스트: " + recipeList.size());
		log.info("recipe리스트" + recipeList);
		return recipeList;
	}
	
	public List<Recipe> getLikeRecipe(String mid) {
		List<Recipe> likeRecipeList = memberDao.selectRecipeByMidLikes(mid);
		//레시피 번호에 맞는 좋아요수를 레시피리스트의 인덱스마다 저장해주기
		for(Recipe recipe : likeRecipeList) {
			int rno = recipe.getRno();
			int likesCount = memberDao.selectLikesCountByRno(rno);
			recipe.setLikecount(likesCount);
		}
		return likeRecipeList;
	}

	public String checkMid(String mid) {
		
		return memberDao.checkMid(mid);
	}
}