package com.example.mazzadytask.presentation.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazzadytask.databinding.DetailsFragmentBinding
import com.example.mazzadytask.databinding.RvBiterBinding
import com.example.mazzadytask.databinding.RvBitsBinding
import com.example.mazzadytask.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsFragmentBinding>() {

    override fun getViewBinding() = DetailsFragmentBinding.inflate(layoutInflater)

    override fun setUpViews() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        binding.tvBitter.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tvBitter.adapter = BitterAdapter()

        binding.rbBits.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rbBits.adapter = BitsAdapter()

        return rootView
    }


    class BitterAdapter() : RecyclerView.Adapter<BitterAdapter.ViewHolder>() {
        private lateinit var binding: RvBiterBinding
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = RvBiterBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        }

        override fun getItemCount() = 3
        inner class ViewHolder(itemView: RvBiterBinding) : RecyclerView.ViewHolder(itemView.root)

    }


    class BitsAdapter() : RecyclerView.Adapter<BitsAdapter.ViewHolder>() {
        private lateinit var binding: RvBitsBinding
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = RvBitsBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        }

        override fun getItemCount() = 3
        inner class ViewHolder(itemView: RvBitsBinding) : RecyclerView.ViewHolder(itemView.root)

    }

}


