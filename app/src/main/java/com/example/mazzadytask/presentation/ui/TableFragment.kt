package com.example.mazzadytask.presentation.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazzadytask.R
import com.example.mazzadytask.databinding.RvItemBinding
import com.example.mazzadytask.databinding.TableFragmentBinding
import com.example.mazzadytask.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TableFragment : BaseFragment<TableFragmentBinding>() {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun getViewBinding() = TableFragmentBinding.inflate(layoutInflater)

    override fun setUpViews() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        binding.rvTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTable.adapter = TableAdapter(mainViewModel.getTableData())
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_tableFragment_to_detailsFragment)
        }
        return rootView
    }

    class TableAdapter(
        private val items: List<Pair<String, String?>>,
    ) :
        RecyclerView.Adapter<TableAdapter.ViewHolder>() {
        private lateinit var binding: RvItemBinding
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = RvItemBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position], position)
        }

        override fun getItemCount() = items.size
        inner class ViewHolder(itemView: RvItemBinding) : RecyclerView.ViewHolder(itemView.root) {
            fun bind(item: Pair<String, String?>, position: Int) {
                binding.apply {
                    this.tvKey.text = item.first
                    this.tvTitle.text = item.second
                }
            }
        }

    }

}


