package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Recipe;
import com.mycompany.webapp.dto.RecipeItem;
import com.mycompany.webapp.dto.RecipeProcess;


@Mapper
public interface RecipeDao {
	public int insertRecipe(Recipe recipe);
	public int insertRecipItems(List<RecipeItem> recipeItems);
	public int insertRecipItems(RecipeItem items);
	public int insertRecipeProcess(RecipeProcess recipeProcess);
	public Recipe selectRecipeByRno(int rno);
	public RecipeProcess selectRecipeProcessByRnoRporder(RecipeProcess rp);
	
}
