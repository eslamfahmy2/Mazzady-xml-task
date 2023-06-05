package com.example.mazzadytask.presentation.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mazzadytask.databinding.InputViewBinding
import com.example.mazzadytask.networking.dto.SubCategoryDto

class OptionsAdapter(
    itemsIn: List<SubCategoryDto>,
) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    private lateinit var binding: InputViewBinding
    private var items: List<SubCategoryDto> = itemsIn
    private lateinit var onViewClicked: (SubCategoryDto, Int) -> Unit

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


    fun setListener(onViewClick: (SubCategoryDto, Int) -> Unit) {
        onViewClicked = onViewClick
    }

    inner class ViewHolder(itemView: InputViewBinding) : RecyclerView.ViewHolder(itemView.root) {

        fun bind(item: SubCategoryDto) {
            binding.apply {
                this.inputText.let {
                    it.hint = item.name
                    it.tag = item.name
                    it.setText(item.selectedOption)
                }
                this.inputLayout.hint = item.name
                this.inputView.setOnClickListener {
                    onViewClicked.invoke(item, items.indexOf(item))
                }
            }
        }
    }
}