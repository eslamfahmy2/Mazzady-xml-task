package com.example.mazzadytask.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mazzadytask.R
import com.example.mazzadytask.networking.dto.Option
import com.google.android.material.bottomsheet.BottomSheetDialog


class OptionsBottomSheet(
    context: Context,
    private val options: List<Option>,
    private val title: String,
    private val onItemClick: (Option, Boolean) -> Unit,
) : BottomSheetDialog(context) {

    val optionsList = options.map { it.name.toString() }
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet)
        val tvOther = findViewById<TextView>(R.id.tvOther)?.apply {
            this.visibility = View.GONE
            this.setOnClickListener {
                val item = options.first().copy(name = "اخري")
                onItemClick.invoke(item, true)
                dismiss()
            }
        }
        tvOther?.visibility = View.VISIBLE
        findViewById<ImageView>(R.id.imgClose)?.setOnClickListener {
            dismiss()
        }
        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, optionsList)
        findViewById<ListView>(R.id.rvItems)?.let {
            it.adapter = arrayAdapter
            it.setOnItemClickListener { adapterView, view, i, l ->
                val item = arrayAdapter.getItem(i)
                options.find { it.name == item }?.let { it1 -> onItemClick.invoke(it1, false) }
                dismiss()
            }
        }
        findViewById<SearchView>(R.id.searchView)?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (optionsList.contains(query)) {
                    arrayAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return false
            }
        })
        findViewById<TextView>(R.id.tvTitle)?.text = title
        /*   etOther?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
               override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                   if (actionId == EditorInfo.IME_ACTION_DONE) {
                       val op = options.first().copy(name = etOther.text.toString())
                       onItemClick.invoke(op)
                       dismiss()
                       return true;
                   }
                   return false;
               }
           })*/
    }
}


class MainCategoriesBottomSheet(
    context: Context,
    private val options: List<String>,
    private val onItemClick: (String) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet)
        findViewById<ImageView>(R.id.imgClose)?.setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tvOther)?.visibility = View.GONE
        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, options)
        findViewById<ListView>(R.id.rvItems)?.let {
            it.adapter = arrayAdapter
            it.setOnItemClickListener { adapterView, view, i, l ->
                val item = arrayAdapter.getItem(i)
                onItemClick.invoke(item ?: "*")
                dismiss()
            }
        }
        findViewById<SearchView>(R.id.searchView)?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (options.contains(query)) {
                    arrayAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return false
            }
        })
    }
}