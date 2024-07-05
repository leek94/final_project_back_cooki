package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.Likes;
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
	public List<RecipeItem> selectRecipeItemListByRno(int rno);
	public List<RecipeProcess> selectRecipeProcessListByRno(int rno);
	public int updateRecipeByRno(Recipe recipe);
	public void deleteRecipeItemByRno(int rno);
	public void updateRecipeProcess(RecipeProcess process);
	public void deleteRecipeProcessByRporder(@Param("rno")int rno, @Param("rporder") int rporder);
	public Likes selectLikesByMidRno(Likes likes);
	public int insertLikes(Likes likes);
	public int deleteLikes(Likes likes);
	public int updateHitcount(int rno);
	public List<Recipe> selectBestRecipe();
	
}
