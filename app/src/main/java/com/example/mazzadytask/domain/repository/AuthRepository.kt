package com.example.mazzadytask.domain.repository


import com.example.mazzadytask.domain.state.StateData
import com.example.mazzadytask.networking.dto.Categories
import com.example.mazzadytask.networking.dto.OptionsDto
import com.example.mazzadytask.networking.dto.SubCategoryDto
import com.example.mazzadytask.networking.wrappers.unwrapResponse
import com.testapp.data.networking.AppServices
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val appServices: AppServices
) {

    suspend fun getCategories(): StateData<Categories> {
        val categoriesResponse = unwrapResponse(appServices.getAllCategories())
        return StateData<Categories>().success(categoriesResponse)
    }

    suspend fun getSubCategories(catId: Int): StateData<List<SubCategoryDto>> {
        val subCategoriesResponse = unwrapResponse(appServices.getSubCategories(catId))
        return StateData<List<SubCategoryDto>>().success(subCategoriesResponse)
    }

    suspend fun getOptions(option: Int): StateData<List<SubCategoryDto>> {
        val optionsResponse = unwrapResponse(appServices.getOptions(option))
        return StateData<List<SubCategoryDto>>().success(optionsResponse)
    }
}