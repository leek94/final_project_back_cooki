package com.mycompany.webapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.ClassDao;
import com.mycompany.webapp.dao.ReviewQnaDao;
import com.mycompany.webapp.dto.ClassItem;
import com.mycompany.webapp.dto.ClassReview;
import com.mycompany.webapp.dto.ClassThumbnail;
import com.mycompany.webapp.dto.Classes;
import com.mycompany.webapp.dto.CuList;
import com.mycompany.webapp.dto.Curriculum;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Participant;
import com.mycompany.webapp.dto.ParticipantList;
import com.mycompany.webapp.dto.Qna;
import com.mycompany.webapp.dto.Search;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassService {

	@Autowired
	private ClassDao classDao;
	
	@Autowired
	private ReviewQnaDao reviewQnaDao;

	// 클래스 기본 정보를 데이터베이스에 저장하기위한 로직
	public void createClass(Classes classes) {
		// <front>에서 axios를 통해 넘겨받지 못한 not null 필드값들을 service에서 설정해줘야 함
		log.info("서비스 createClass 메소드 실행");

		// -------------------- classes insert --------------------
		// 추후에 시큐리티로 받아올 예정
		classDao.insertClass(classes);
		log.info("서비스 createClass insertClass");

		// ------------------- classthumbnail insert --------------------
		// set 해준 클래스 번호 가져오기
		int cno = classes.getCno();

		// <front>에서 axios로 연결된 cthumbnailimgs(배열 형태의 썸네일 파일 모음) 가져오기
		MultipartFile[] fileImgs = classes.getCthumbnailimgs();

		// 파일 배열을 구조분해 해서 파일 객체로 for문을 통해 하나하나 저장
		for (int i = 0; i < fileImgs.length; i++) {
			// 구조분해한 파일 객체를 저장해줄 dto 객체 선언
			ClassThumbnail classThumbnail = new ClassThumbnail();
			// 객체별로 정보를 저장하기 위한 파일 객체 선언
			MultipartFile fileImg = fileImgs[i];
			// 파일 객체에 정보 넣어주기
			classThumbnail.setCtorder(i + 1);
			classThumbnail.setCtimgoname(fileImg.getOriginalFilename());
			classThumbnail.setCtimgtype(fileImg.getContentType());
			try {
				classThumbnail.setCtimgdata(fileImg.getBytes());
			} catch (Exception e) {
			}
			classThumbnail.setCno(cno);

			classDao.insertClassThumbnail(classThumbnail);
		}
		log.info("서비스 createClass insertClassThumbnail");
	}

	// -------------------- classitem insert --------------------
	// 클래스 재료 정보를 데이터베이스에 저장하기위한 로직
	// 클래스 기본 정보와 다르게 <front>에서 객체로 하나하나 들어오기 때문에 바로 insert 해주면 됨

	
	//클래스 재료 정보를 데이터베이스에 저장하기위한 로직
	public void createItem(List<ClassItem> classItems) {
		log.info("서비스 createItem 메소드 실행");
		for(int i=0; i<classItems.size(); i++) {
			classDao.insertItem(classItems.get(i));
		}
		log.info("서비스 createItem insert classItem");
	}


	// -------------------- curriculum insert --------------------
	
	//클래스 커리큘럼 정보를 데이터베이스에 저장하기위한 로직
	public void createCurriculum(CuList cuList) {
		log.info("서비스 createCurriculum 메소드 실행");

		
		// <front>에서 axios로 연결된 cuimg(배열X, <front>에서 하나씩 보내줌) 가져오기
		List<Curriculum> curriculums = cuList.getCurriculums();
		int cno = cuList.getCno();
		for(int i=0; i<curriculums.size(); i++) {
			Curriculum curriculum = curriculums.get(i);
			curriculum.setCno(cno);
			
			MultipartFile fileImg = curriculum.getCuimg();
			
			curriculum.setCuimgoname(fileImg.getOriginalFilename());
			curriculum.setCuimgtype(fileImg.getContentType());
			try {
				curriculum.setCuimgdata(fileImg.getBytes());
			} catch (IOException e) {
			}
			
			classDao.insertCurriculum(curriculum);
		}
		log.info("서비스 createCurriculum insert curriculum");
	}

	// 클래스 디테일 정보 받기
	public Classes getClasses(int cno) {
		Classes classes = classDao.selectByCno(cno);
		return classes;
	}

	public Participant getIsparticipant(Participant participant) {
		return classDao.selectByisParticipant(participant);
	}

	public List<Curriculum> getCurriculumList(int cno) {
		return classDao.selectCurriculumByCno(cno);
	}

	public List<ClassItem> getClassItemList(int cno) {
		return classDao.selectClassItemByCno(cno);
	}

	public ClassThumbnail getThumbnail(ClassThumbnail classThumb) {
		return classDao.selectByClassThumbnail(classThumb);

	}

	public Curriculum getCurriculumimg(Curriculum curriculum) {

		return classDao.selectByCurriculumimg(curriculum);
	}

	public int getThumbimgCount(int cno) {
		return classDao.selectByClassThumbCount(cno);
	}

	public int SetClassApply(Participant participant) {
		return classDao.insertClassApply(participant);
	}

	public int deleteClassApply(Participant participant) {
		return classDao.deleteClassApply(participant);

	}

	// -------------------- classes update --------------------
	public int updateClass(Classes classes) {
		log.info("서비스 updateClass 메소드 실행");
		// 썸네일 제외 data는 update로 진행
		// 썸네일 업데이트는 기존에 있던 data를 delete 하고 새로운 data를 insert 하는 방식으로 진행
	// -------------------- classthumbnail insert --------------------
		MultipartFile[] fileImgs = classes.getCthumbnailimgs();
		// 썸네일 이미지가 들어오는 경우
		if (fileImgs != null) {
			int cno = classes.getCno();
			// 썸네일 이미지 업데이트 1단계: 기존 썸네일 이미지 data 모두 delete
			int deleteResult = classDao.deleteClassThumbnail(cno);
			// 썸네일 이미지 업데이트 2단계: 새로운 썸네일 이미지 data insert
			for (int i = 0; i < fileImgs.length; i++) {
				// 구조분해한 파일 객체를 저장해줄 dto 객체 선언
				ClassThumbnail classThumbnail = new ClassThumbnail();
				// 객체별로 정보를 저장하기 위한 파일 객체 선언
				MultipartFile fileImg = fileImgs[i];
				// 파일 객체에 정보 넣어주기
				classThumbnail.setCtorder(i + 1);
				classThumbnail.setCtimgoname(fileImg.getOriginalFilename());
				classThumbnail.setCtimgtype(fileImg.getContentType());
				try {
					classThumbnail.setCtimgdata(fileImg.getBytes());
				} catch (Exception e) {
				}
				classThumbnail.setCno(cno);
				classDao.insertClassThumbnail(classThumbnail);
			}
		}
		log.info("서비스 updateClass 클래스 기본 정보 업데이트 성공");
		return classDao.updateClassByCno(classes);
	}


	// -------------------- classitem update --------------------

	public void updateItem(List<ClassItem> classItems) {
		log.info("서비스 updateItem 실행");
		int cno = classItems.get(0).getCno();
		// 재료 업데이트 1단계: 기존 재료 data 모두 삭제하기
		int deleteResult = classDao.deleteClassItemByCno(cno);
		log.info("서비스 deleteClassItemByCno");
		// 재료 업데이트 2단계: 새로운 재료 data 생성하기
		for (int i = 0; i < classItems.size(); i++) {
			// cname 저장하기
			ClassItem classItem = classItems.get(i);
			// cino 저장하기
			classItem.setCino(i + 1);
			// cno 저장하기
			// 재료가 모두 delete 되면 cno를 재료 테이블에서 받아올 수 없기 때문에 classes에서 받아온 cno로 넣어줘야함
			classItem.setCno(cno);
			classDao.insertItem(classItem);
		}
		log.info("서비스 updateItem 클래스 재료 정보 업데이트 성공");
	}

	// -------------------- curriculum update --------------------

	public void updateCurriculum(Curriculum curriculum, int cno) {
		log.info("서비스 updateCurriculum 실행");

		int cuOrder = curriculum.getCuorder();
		int cuLength = curriculum.getCulength();
		int cuCount = classDao.selectCurriculumCountBycno(cno);

		// <front>에서 받아온 커리큘럼 번호가 db보다 클 경우
		// -> 같은 부분까지는 update / 초과인 부분은 insert
		if (cuLength > cuCount) {
			log.info("커리큘럼이 기존보다 추가 됨");
			if (cuOrder > cuCount) {
				MultipartFile fileImg = curriculum.getCuimg();
				if (fileImg != null) {
					curriculum.setCuimgoname(fileImg.getOriginalFilename());
					curriculum.setCuimgtype(fileImg.getContentType());
					try {
						curriculum.setCuimgdata(fileImg.getBytes());
					} catch (IOException e) {
					}
				}
				int insertResult = classDao.insertCurriculum(curriculum);
				log.info("커리큘럼 추가 부분 insert 됨");
			} else {
				MultipartFile fileImg = curriculum.getCuimg();
				log.info("커리큘럼 추가 fileImg:", fileImg);
				if (fileImg != null) {
					curriculum.setCuimgoname(fileImg.getOriginalFilename());
					curriculum.setCuimgtype(fileImg.getContentType());
					try {
						curriculum.setCuimgdata(fileImg.getBytes());
					} catch (IOException e) {
					}
				}
				int updateResult = classDao.updateCurriculumByCno(curriculum);
				log.info("커리큘럼 같은 부분 update 됨");
			}
		}

		// <front>에서 받아온 커리큘럼 번호가 db와 같을 경우
		// -> 모두 update
		else if (cuLength == cuCount) {
			log.info("커리큘럼이 기존과 같음");
			// 이미지가 null이 아닐 경우 update
			MultipartFile fileImg = curriculum.getCuimg();

			if (fileImg != null) {
				curriculum.setCuimgoname(fileImg.getOriginalFilename());
				curriculum.setCuimgtype(fileImg.getContentType());
				try {
					curriculum.setCuimgdata(fileImg.getBytes());
				} catch (IOException e) {
				}
			}
			int updateResult = classDao.updateCurriculumByCno(curriculum);
			log.info("서비스 updateCurriculumByCno 클래스 커리큘럼 정보 업데이트 성공");
		}

		// <front>에서 받아온 커리큘럼 번호가 db 보다 작을 경우
		// -> 들어온 부분 모두 update / 줄어든 부분은 delete
		else if (cuLength < cuCount) {
			log.info("커리큘럼이 기존보다 줄어듬");

			MultipartFile fileImg = curriculum.getCuimg();

			if (fileImg != null) {
				curriculum.setCuimgoname(fileImg.getOriginalFilename());
				curriculum.setCuimgtype(fileImg.getContentType());
				try {
					curriculum.setCuimgdata(fileImg.getBytes());
				} catch (IOException e) {
				}
			}
			int updateResult = classDao.updateCurriculumByCno(curriculum);
			log.info("커리큘럼 같은 부분 update 됨");

			if (cuOrder == cuLength) {
				for (int overOrder = cuLength + 1; overOrder <= cuCount; overOrder++) {
					int deleteResult = classDao.deleteCurriculumCountBycuorder(cno, overOrder);
				}
				log.info("커리큘럼 줄어든 부분 delete 됨");
			}
		}
	}


	public String isOverPeople(int cno, int cpersoncount) {
		log.info("마감인원 수 : "+cpersoncount);
		//클래스 신청자가 몇명인지 확인
		int participants = classDao.selectParticipantsCounttByCno(cno);
		log.info("신청 인원 수: " + participants);
		String result = "";
		//신청 인원이 제한 인원보다 많다면(신청마감) false를 리턴하고 신청 가능하면 true를 리턴
		if(participants<cpersoncount) {
			result = "true";
		}else {
			result = "false";
		}
		return result;
	}


	public void updateCurriculum(Curriculum curriculum) {
		log.info("서비스 updateCurriculum 실행");
		
		int cno = curriculum.getCno();
		int cuOrder = curriculum.getCuorder();
		int cuLength = curriculum.getCulength();
		int cuRow = classDao.selectCurriculumCountBycno(cno);
		
		MultipartFile fileImg = curriculum.getCuimg();
		if(fileImg != null) {
			curriculum.setCuimgoname(fileImg.getOriginalFilename());
			curriculum.setCuimgtype(fileImg.getContentType());
			try {
				curriculum.setCuimgdata(fileImg.getBytes());
			} catch (IOException e) {
			}
		}
		
		// <front>에서 받아온 커리큘럼 번호가 db보다 클 경우
		// -> 같은 부분까지는 update / 초과인 부분은 insert
		if (cuLength > cuRow) {
			log.info("커리큘럼이 기존보다 추가 됨");
			if(cuOrder> cuRow) {
				int insertResult = classDao.insertCurriculum(curriculum);
				log.info("커리큘럼 추가 부분 insert 됨");
			} else {
				int updateResult = classDao.updateCurriculumByCno(curriculum);
				log.info("커리큘럼 같은 부분 update 됨");
			}	
		}
		
		// <front>에서 받아온 커리큘럼 번호가 db와 같을 경우
		// -> 모두 update
		else if (cuLength == cuRow) {
			log.info("커리큘럼이 기존과 같음");
			//이미지가 null이 아닐 경우 update
			int updateResult = classDao.updateCurriculumByCno(curriculum);
			log.info("서비스 updateCurriculumByCno 클래스 커리큘럼 정보 업데이트 성공");
		}
		
		// <front>에서 받아온 커리큘럼 번호가 db 보다 작을 경우
		// -> 들어온 부분 모두 update / 줄어든 부분은 delete
		else if (cuLength < cuRow) {
			log.info("커리큘럼이 기존보다 줄어듬");
			int updateResult = classDao.updateCurriculumByCno(curriculum);
			log.info("커리큘럼 같은 부분 update 됨");
			
			if(cuOrder == cuLength) {	
				for(int overOrder=cuLength+1; overOrder<=cuRow; overOrder++) {
					int deleteResult = classDao.deleteCurriculumCountBycuorder(cno, overOrder);
				} 
				log.info("커리큘럼 줄어든 부분 delete 됨");
			}
		}
	}

	public int getNowPerson(int cno) {
		return classDao.selectParticipantsCounttByCno(cno);
	}

	
	// -------------------- Q&A crud --------------------
	
	public void createQna(Qna qna) {
		log.info("서비스 createQna 실행");
		reviewQnaDao.insertQna(qna);
		log.info("서비스 createQna insertQna");
	}

	public int getQnaCount(int cno) {
		return reviewQnaDao.selectQnaCountByCno(cno);
	}
	
	public List<Qna> getQnaList(int cno, Pager pager) {
		log.info("서비스 getQnaList 실행");
		return reviewQnaDao.selectQnaByCnoPager(cno, pager);
	}

	public void updateQna(Qna qna) {
		log.info("서비스 updateQna 실행");
		reviewQnaDao.updateQnaByQno(qna);
	}

	public void deleteQna(int qno) {
		log.info("서비스 deleteQna 실행");
		reviewQnaDao.deleteQnaByQno(qno);
	}


	public void updateQreply(Qna qna) {
		log.info("서비스 updateQreply 실행");
		reviewQnaDao.updateQreplyByQno(qna);
	}	

	public List<Classes> getSearchClasses(Search search, Pager pager) {
		return classDao.selectSearchClass(search, pager);
	}


	// -------------------- ClassReview crud --------------------
	
	public void createClassReview(ClassReview classReview) {
		log.info("서비스 insertClassReview 실행");
		log.info(classReview.toString());
		reviewQnaDao.insertClassReview(classReview);
	}

	public int getReviewCount(int cno) {
		Classes classes = classDao.selectByCno(cno);
		return reviewQnaDao.selectReviewCountByCno(classes);
	}
	
	public List<ClassReview> getClassReviewList(int cno, Pager pager) {
		log.info("서비스 getClassReviewList 실행");
		Classes classes = classDao.selectByCno(cno);
		return reviewQnaDao.selectClassReviewByCnoPager(classes, pager);
	}
	
	public Float getAvgCrratio(int cno) {
		log.info("서비스 getAvgCrratio 실행");
		return reviewQnaDao.selectAvgCrratioByCno(cno);
	}

	public int getCount() {
		return classDao.getCount();
	}

	public void updateClassReview(ClassReview classReview) {
		log.info("서비스 updateClassReview 실행");
		reviewQnaDao.updateClassReviewByCrno(classReview);
		
	}

	public void deleteClassReview(int crno) {
		log.info("서비스 deleteClassReview 실행");
		reviewQnaDao.deleteClassReviewByCrno(crno);
		
	}

	public int getSearchCount(Search search) {
		return classDao.getSearchCount(search);
	}

	public List<Classes> getBestClass(int number) {
		return classDao.selectBestClass(number);
	}

	public void updateChitcount(Classes classes) {
		classDao.updateChitcount(classes);
		
	}

	public List<ParticipantList> getParticipantList(int cno) {
		return classDao.selectParticipantList(cno);
		
	}

	public void updateIsParticipant(Participant participant) {
		classDao.updateParticipant(participant);
	}


	public int insertReopenClass(Classes classes) {
		//이미지가 없을 경우 원래 클래스의 이미지를 받아오기 위해 cno를 저장 
		int cno=classes.getCno();
		//클래스 기본 정보 저장
		classDao.insertClass(classes);
		
		ClassThumbnail classThumbNail= new ClassThumbnail();
		//front에서 받은 list 형태의 사진을 저장해서 하나씩 data에 저장
		MultipartFile[] thumbImg= classes.getCthumbnailimgs();
		//이미지가 바뀌었을 경우
		if(thumbImg!=null) {
			for(int i=0;i<thumbImg.length;i++) {
				classThumbNail.setCno(classes.getCno());
				classThumbNail.setCtorder(i+1);
				classThumbNail.setCtimgoname(thumbImg[i].getOriginalFilename());
				classThumbNail.setCtimgtype(thumbImg[i].getContentType());
				try {
					classThumbNail.setCtimgdata(thumbImg[i].getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				classDao.insertClassThumbnail(classThumbNail);
			}
		//기존의 이미지를 사용할 경우 cno와 ctorder로 data를 찾아와서 다시 열 클래스의 cno에 이미지를 저장
		}else {
			int count= classDao.selectByClassThumbCount(cno);
			for(int i=0;i<count;i++) {
				classThumbNail.setCno(cno);
				classThumbNail.setCtorder(i+1);
				ClassThumbnail thumb=classDao.selectByClassThumbnail(classThumbNail);
				thumb.setCno(classes.getCno());
				classDao.insertClassThumbnail(thumb);
			}	
		}
		return classes.getCno();
	}

	public void insertReCurri(CuList cuList) {
		int initCno=cuList.getInitCno();
		int cno= cuList.getCno();
		int initialLength=cuList.getInitialLength();
		int cuLength=cuList.getNowLength();
		List<Curriculum> cu=cuList.getCurriculums();
		for(int i=0;i<cuLength;i++) {
			Curriculum curri=cu.get(i);
			if(curri.getCuimg()!=null) {
				log.info("1");
				MultipartFile mf= curri.getCuimg();
				curri.setCuimgoname(mf.getOriginalFilename());
				curri.setCuimgtype(mf.getContentType());
				try {
					curri.setCuimgdata(mf.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				curri.setCno(cno);
				curri.setCutitle(curri.getCutitle());
				curri.setCucontent(curri.getCucontent());
				classDao.insertCurriculum(curri);
			}else {
				log.info("2");
				Curriculum originCurri= new Curriculum();
				originCurri.setCno(initCno);
				originCurri.setCuorder(curri.getCuorder());
				Curriculum changeCu=classDao.selectByCurriculumimg(originCurri);
				log.info("cont"+curri.getCucontent());
				changeCu.setCno(cno);
				changeCu.setCutitle(curri.getCutitle());
				changeCu.setCucontent(curri.getCucontent());
				log.info("ccont"+changeCu.getCucontent());
				log.info("insert1");
				classDao.insertCurriculum(changeCu);
				log.info("insert21");
			}
		}
	}

	public int classDelete(int cno) {
		return classDao.deleteClass(cno);
	}

}
