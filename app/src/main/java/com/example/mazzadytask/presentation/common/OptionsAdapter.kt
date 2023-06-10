package com.example.mazzadytask.presentation.common

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mazzadytask.databinding.InputViewBinding
import com.example.mazzadytask.networking.dto.SubCategoryDto


enum class OptionsViewType {
    NORMAL,
    OTHER
}


class OptionsAdapter(
    private val items: List<SubCategoryDto>,
) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    private lateinit var binding: InputViewBinding
    private lateinit var onViewClicked: (SubCategoryDto, Int, Boolean) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = InputViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


    fun setListener(onViewClick: (SubCategoryDto, Int, Boolean) -> Unit) {
        onViewClicked = onViewClick
    }

    inner class ViewHolder(itemView: InputViewBinding) : RecyclerView.ViewHolder(itemView.root) {

        fun bind(item: SubCategoryDto) {
            binding.apply {
                if (item.isOther) {
                    this.layoutOther.visibility = View.VISIBLE
                    this.layoutOther.hint = "اكتب هنا"
                    this.otherText.setText(item.otherOption ?: "")
                    this.otherText.setOnEditorActionListener(object :
                        TextView.OnEditorActionListener {
                        override fun onEditorAction(
                            p0: TextView?,
                            actionId: Int,
                            p2: KeyEvent?
                        ): Boolean {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                val newOptions =
                                    item.copy(otherOption = this@apply.otherText.text.toString())
                                onViewClicked.invoke(newOptions, items.indexOf(item), true)
                                return true;
                            }
                            return false;
                        }
                    })
                    this.inputText.let {
                        it.hint = item.name
                        it.tag = item.name
                        it.setText("اخري")
                    }

                } else {
                    binding.layoutOther.visibility = View.GONE
                    this.inputText.let {
                        it.hint = item.name
                        it.tag = item.name
                        it.setText(item.selectedOption)
                    }
                }
                this.inputLayout.hint = item.name
                this.inputView.setOnClickListener {
                    this.otherText.clearFocus()
                    onViewClicked.invoke(item, items.indexOf(item), false)
                }
            }

        }
    }
}