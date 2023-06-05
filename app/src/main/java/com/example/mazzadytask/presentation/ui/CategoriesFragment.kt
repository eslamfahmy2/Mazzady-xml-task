package com.example.mazzadytask.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mazzadytask.R
import com.example.mazzadytask.databinding.CategoriesFragmentBinding
import com.example.mazzadytask.networking.dto.SubCategoryDto
import com.example.mazzadytask.presentation.common.BaseFragment
import com.example.mazzadytask.presentation.common.BottomOptionsSheet
import com.example.mazzadytask.presentation.common.BottomSheet
import com.example.mazzadytask.presentation.common.showDialog
import com.example.mazzadytask.presentation.utils.ObserverCallbacks
import com.example.mazzadytask.presentation.utils.OptionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<CategoriesFragmentBinding>() {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private lateinit var optionsAdapter: OptionsAdapter

    override fun getViewBinding() = CategoriesFragmentBinding.inflate(layoutInflater)

    private val listener = { item: SubCategoryDto, index: Int ->
        BottomOptionsSheet(
            context = requireContext(),
            options = item.options ?: listOf(),
            title = item.name
        ) { option ->
            mainViewModel.setOption(item, option, index)
            initOptionAdapter()
        }.show()
    }

    private fun initOptionAdapter() {
        optionsAdapter = OptionsAdapter(mainViewModel.getOptions())
        optionsAdapter.setListener(listener)
        binding.rvOptions.adapter = optionsAdapter
    }

    override fun screenDataObservers(): List<ObserverCallbacks<*>> {
        return listOf(
            ObserverCallbacks(
                mainViewModel.categoriesScreenEvent,
                success = {
                    binding.main.visibility = View.VISIBLE
                    binding.btnTry.visibility = View.GONE
                    binding.inputViewMain.setOnClickListener {
                        BottomSheet(
                            requireContext(),
                            mainViewModel.getMainCategories()
                        ) { value ->
                            binding.inputTextMain.setText(value)
                            mainViewModel.setSelectedCategory(value)
                            binding.inputTextSub.setText("")
                            mainViewModel.clearOptions()
                        }.show()
                    }
                    binding.inputViewSub.setOnClickListener {
                        BottomSheet(
                            requireContext(),
                            mainViewModel.getSubCategories()
                        ) { value ->
                            binding.inputTextSub.setText(value)
                            mainViewModel.setSubCategory(value)
                        }.show()
                    }
                },
                { error ->
                    binding.main.visibility = View.GONE
                    binding.btnTry.visibility = View.VISIBLE
                    val errorMessage = error?.message
                    if (errorMessage.isNullOrEmpty()) {
                        showDialog(getString(R.string.unknown_error))
                    } else {
                        showDialog(errorMessage)
                    }
                }
            ),

            ObserverCallbacks(
                mainViewModel.optionsEvent,
                success = { data ->
                    data?.let {
                        initOptionAdapter()
                    }
                },
                { error ->
                    val errorMessage = error?.message
                    if (errorMessage.isNullOrEmpty()) {
                        showDialog(getString(R.string.unknown_error))
                    } else {
                        showDialog(errorMessage)
                    }
                }
            ),


            ObserverCallbacks(
                mainViewModel.optionsChildEvent,
                success = { data ->
                    data?.let {
                        initOptionAdapter()
                    }
                },
                { error ->
                    val errorMessage = error?.message
                    if (errorMessage.isNullOrEmpty()) {
                        showDialog(getString(R.string.unknown_error))
                    } else {
                        showDialog(errorMessage)
                    }
                }
            )
        )
    }

    override fun setUpViews() {
        progressView = binding.progressView
        mainViewModel.startGetCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        binding.rvOptions.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_tableFragment)
        }
        binding.btnTry.setOnClickListener {
            mainViewModel.startGetCategories()
        }
        return rootView
    }
}






