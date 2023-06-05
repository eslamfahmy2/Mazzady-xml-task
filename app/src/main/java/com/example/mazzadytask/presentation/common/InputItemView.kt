package com.example.mazzadytask.presentation.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.mazzadytask.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("ViewConstructor")
class InputItemView(
    context: Context,
    tag: String,
    onViewClicked: () -> Unit,
) : LinearLayout(context) {
    init {
        inflate(context, R.layout.input_view, this)
        val clickView: View = findViewById(R.id.inputView)
        clickView.setOnClickListener {
            onViewClicked.invoke()
        }
        findViewById<TextInputEditText>(R.id.inputText)?.let {
            it.hint = tag
            it.tag = tag
        }
          findViewById<TextInputLayout>(R.id.inputLayout)?.hint = tag
    }
}