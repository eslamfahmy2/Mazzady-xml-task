package com.example.mazzadytask.presentation.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.mazzadytask.R
import com.example.mazzadytask.networking.dto.SubCategoryDto

class CustomArrayAdapter(
    context: Context,
    private val layoutResource: Int = 0,
    private val values: List<SubCategoryDto>
) : ArrayAdapter<SubCategoryDto>(context, layoutResource, values) {

    override fun getItem(position: Int): SubCategoryDto = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(parent, layoutResource)
        return bindData(getItem(position), view)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(
            parent,
            R.layout.input_view
        )
        return bindData(getItem(position), view)
    }

    private fun createViewFromResource(
        parent: ViewGroup,
        layoutResource: Int
    ): View {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        return view
    }

    private fun bindData(value: SubCategoryDto, view: View): View {
        // view.findViewById<TextView>(R.id.inputText).text = value.name ?: "-"
        return view
    }
}