package com.example.mazzadytask.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazzadytask.domain.repository.AuthRepository
import com.example.mazzadytask.networking.dto.*
import com.example.mazzadytask.presentation.utils.StateLiveData
import com.example.mazzadytask.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {


    val categoriesScreenEvent = StateLiveData<Categories>()
    val optionsEvent = StateLiveData<List<SubCategoryDto>>()
    val optionsChildEvent = StateLiveData<List<SubCategoryDto>>()

    private val categories: MutableList<Category> = mutableListOf()
    private val options: MutableList<SubCategoryDto> = mutableListOf()

    private var selectedMainCategory: Category? = null
    private var selectedSubCategory: Children? = null

    fun startGetCategories() {
        viewModelScope.safeLaunch(categoriesScreenEvent) {
            categoriesScreenEvent.value = authRepository.getCategories()
            categoriesScreenEvent.value?.data?.let {
                categories.clear()
                categories.addAll(it.categories ?: listOf())
            }
        }
    }

    fun getMainCategories(): List<String> {
        return categories.map { it.name.toString() }
    }

    fun setSelectedCategory(id: String) {
        selectedMainCategory = categories.firstOrNull() { it.name == id }
        selectedSubCategory = null
        options.clear()
    }

    fun getSubCategories(): List<String> {
        return selectedMainCategory?.children?.map { it.name.toString() } ?: listOf()
    }

    fun setSubCategory(text: String) {
        selectedSubCategory = selectedMainCategory?.children?.find { it.name == text }
        selectedSubCategory?.id?.let {
            startGetOptions(it)
        }
    }

    private fun startGetOptions(id: Int) {
        viewModelScope.safeLaunch(optionsEvent) {
            optionsEvent.value = authRepository.getSubCategories(id)
            optionsEvent.value?.data?.let {
                options.clear()
                options.addAll(it)
            }
        }
    }

    fun setOption(subCategoryDto: SubCategoryDto, option: Option, index: Int) {
        options[index].selectedOption = option.name ?: "-"
        if (option.child == true) {
            val parentId = if (subCategoryDto.parentId == null)
                subCategoryDto.id else
                subCategoryDto.parentId
            val parentOption = if (subCategoryDto.parentOption == null)
                subCategoryDto.selectedOption else
                subCategoryDto.parentOption
            startGetChildOptions(option.id, index, parentId, parentOption)
        }
    }

    private fun startGetChildOptions(id: Int, index: Int, parentId: Int?, parentOption: String?) {
        viewModelScope.safeLaunch(optionsChildEvent) {
            optionsChildEvent.value = authRepository.getOptions(id)
            optionsChildEvent.value?.data?.let { list ->
                val inIndex = index + 1
                options.removeAll { it.parentId == parentId && it.parentOption != parentOption }
                list.forEach {
                    it.parentId = parentId
                    it.parentOption = parentOption
                }
                options.addAll(inIndex, list)
            }
        }
    }


    fun getOptions(): MutableList<SubCategoryDto> {
        return options
    }

    fun getTableData(): List<Pair<String, String?>> {
        val listOfPairs = listOf<Pair<String, String?>>().toMutableList()
        val cat = Pair("Category", selectedMainCategory?.name)
        val sub = Pair("Sub Category", selectedSubCategory?.name)
        listOfPairs.add(cat)
        listOfPairs.add(sub)
        options.forEach {
            val option = Pair(it.name ?: "", it.selectedOption)
            listOfPairs.add(option)
        }
        return listOfPairs.toList()
    }

    fun clearOptions() {
        options.clear()
    }

}