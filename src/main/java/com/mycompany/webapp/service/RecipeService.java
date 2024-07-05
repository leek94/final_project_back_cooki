package com.mycompany.webapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dao.RecipeDao;
import com.mycompany.webapp.dto.Likes;
import com.mycompany.webapp.dto.PrList;
import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.RecipeItem;
import com.mycompany.webapp.dto.RecipeProcess;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class RecipeService {
	@Autowired 
	RecipeDao recipeDao;
	
	public int insertRecipe(Recipe recipe) {
		return recipeDao.insertRecipe(recipe);
	}
	
	public void insertRecipeItems(List<RecipeItem> recipeItem) {
		//리스트 형태의 레시피 아이템을 (RecipeIem dto)타입으로 하나씩 insert 해줌
		for(RecipeItem items : recipeItem) {
			recipeDao.insertRecipItems(items);
		}
	}
	
	public int insertRecipeProcess(RecipeProcess recipeProcess) {
		return recipeDao.insertRecipeProcess(recipeProcess);
		
	}

	public Recipe getRecipe(int rno) {
		return recipeDao.selectRecipeByRno(rno);
	}

	public RecipeProcess getRecipeProcess(RecipeProcess rp) {
		return recipeDao.selectRecipeProcessByRnoRporder(rp);
	}

	public List<RecipeItem> getRecipeItemList(int rno) {
		return recipeDao.selectRecipeItemListByRno(rno);
	}

	public List<RecipeProcess> getRecipeProcessList(int rno) {
		return recipeDao.selectRecipeProcessListByRno(rno);
	}
	public int updateRecipe(Recipe recipe) {
		return recipeDao.updateRecipeByRno(recipe);
	}

	public void updateRecipeItem(List<RecipeItem> recipeItem) {
		//delete는 리스트의 모든 값을 삭제 해야 하므로 rno 값만 가져오기
		int rno = recipeItem.get(0).getRno();
		recipeDao.deleteRecipeItemByRno(rno);
		//리스트 형태로 받아온 item의 값을 하나씩 insert 해주기
		for(RecipeItem Items: recipeItem) {
			recipeDao.insertRecipItems(Items);
		}
	}

	public void updateRecipeProcess(PrList prList) {
		//prList에 존재하는 recipe item list를 recipeProcess라는 변수에 저장
		List<RecipeProcess> recipeProcess = prList.getProcesses();
		//수정하기 전의 process의 길이를 변수에 저장
		int initLength= prList.getInitialLength();
		int changeLength= prList.getChangeLength();
		//front에서 rno를 recipeProcess의 필드에 값을 저장하지 않았기 때문에 
		//변수로 선언해 for문으로  recipeProcess를 하나씩 업데이트 할 때 값을 같이 전달
		int rno = prList.getRno();
		for(int i=0; i<recipeProcess.size(); i++) {
			//리스트 형태의 recipeProcess를 객체로 빼내기
			RecipeProcess process = recipeProcess.get(i);
			process.setRno(rno);
			int rpOrder = process.getRporder();
			MultipartFile rpimg= process.getRpAttach();
			
			if(rpimg!=null) {
				process.setRpimgoname(rpimg.getOriginalFilename());
				process.setRpimgtype(rpimg.getContentType());
				try {
					process.setRpimgdata(rpimg.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//수정되기 전의 process의 길이와 레시피의 순서를 비교해 
			//만약 수정되기 전의 process의 길이가 더 길 경우
			//(ex 수정 전의 길이가 6이고 rpOrder가 5까지 존재 한다면 5번째까지 값은 update 해주고 6번 째 값은 insert 해준다)
			if(initLength>=rpOrder) {
				
				recipeDao.updateRecipeProcess(process);
			} else {
				recipeDao.insertRecipeProcess(process);
			}
		}
		if(initLength>changeLength) {
			for(int i=changeLength+1;i<=initLength;i++) {
				recipeDao.deleteRecipeProcessByRporder(rno,i);
			}
		}
	}

	public Likes getIsLike(Likes likes) {
		return recipeDao.selectLikesByMidRno(likes);
	}

	public int createLike(Likes likes) {
		return recipeDao.insertLikes(likes);
	}

	public int deleteLike(Likes likes) {
		return recipeDao.deleteLikes(likes);
	}

	public int increaseHitcount(int rno) {
		return recipeDao.updateHitcount(rno);
	}

	public List<Recipe> getBestRecipe() {
		return recipeDao.selectBestRecipe();
	}


}
