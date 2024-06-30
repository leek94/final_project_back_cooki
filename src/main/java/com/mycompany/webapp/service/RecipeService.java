package com.mycompany.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.RecipeDao;
import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.RecipeItem;
import com.mycompany.webapp.dto.RecipeProcess;

@Service
public class RecipeService {
	@Autowired RecipeDao recipeDao;
	
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

}
