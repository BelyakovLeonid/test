package com.github.belyakovleonid.feature_recipe_list.domain

import android.util.Log
import com.github.belyakovleonid.core_network_api.model.Result
import com.github.belyakovleonid.feature_recipe_list.domain.model.Recipe
import javax.inject.Inject

class RecipeListInteractor @Inject constructor(
    private val repository: RecipeListRepository
) {

    init {
        Log.d("MyTag", "repos hash = ${repository.hashCode()}")
    }

    suspend fun loadMoreRecipes(): Result<List<Recipe>> {
        return repository.loadRecipes()
    }
}