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


    //screen Events
    val categoriesScreenEvent = StateLiveData<Categories>()
    val optionsEvent = StateLiveData<List<SubCategoryDto>>()
    val optionsChildEvent = StateLiveData<List<SubCategoryDto>>()

    //data holders
    private val categories: MutableList<Category> = mutableListOf()
    private val options: MutableList<SubCategoryDto> = mutableListOf()

    private var selectedMainCategory: Category? = null
    private var selectedSubCategory: Children? = null

    //get main categories
    fun startGetCategories() {
        viewModelScope.safeLaunch(categoriesScreenEvent) {
            categoriesScreenEvent.value = authRepository.getCategories()
            categoriesScreenEvent.value?.data?.let {
                categories.clear()
                categories.addAll(it.categories ?: listOf())
            }
        }
    }

    //set category
    fun setSelectedCategory(id: String) {
        selectedMainCategory = categories.firstOrNull() { it.name == id }
        //clear sub options
        selectedSubCategory = null
        clearOptions()
    }

    //set subcategory
    fun setSubCategory(text: String) {
        selectedSubCategory = selectedMainCategory?.children?.find { it.name == text }
        selectedSubCategory?.id?.let {
            startGetOptions(it)
        }
    }

    //get options
    private fun startGetOptions(id: Int) {
        viewModelScope.safeLaunch(optionsEvent) {
            optionsEvent.value = authRepository.getSubCategories(id)
            optionsEvent.value?.data?.let {
                options.clear()
                options.addAll(it)
            }
        }
    }

    //get options child data
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

    //set selected option
    fun setOption(subCategoryDto: SubCategoryDto, option: Option, index: Int, isOther: Boolean) {
        options[index].isOther = isOther
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

    //set other option value
    fun setOtherOption(item: SubCategoryDto, index: Int) {
        options[index].isOther = item.isOther
        options[index].selectedOption = item.selectedOption
        options[index].otherOption = item.otherOption
    }

    //return categories data
    fun getMainCategories(): List<String> {
        return categories.map { it.name.toString() }
    }

    //return sub  categories data
    fun getSubCategories(): List<String> {
        return selectedMainCategory?.children?.map { it.name.toString() } ?: listOf()
    }

    //return options data
    fun getOptions(): MutableList<SubCategoryDto> {
        return options
    }

    //return table data
    fun getTableData(): List<Pair<String, String?>> {
        val listOfPairs = listOf<Pair<String, String?>>().toMutableList()
        val cat = Pair("فئة", selectedMainCategory?.name)
        val sub = Pair("فئة فرعية", selectedSubCategory?.name)
        listOfPairs.add(cat)
        listOfPairs.add(sub)
        options.forEach {
            val option =
                Pair(
                    it.name?:"",
                    if (it.isOther) it.otherOption ?: "" else it.selectedOption ?: ""
                )
            listOfPairs.add(option)
        }
        return listOfPairs.toList()
    }

    //clear options
    fun clearOptions() {
        options.clear()
    }

}