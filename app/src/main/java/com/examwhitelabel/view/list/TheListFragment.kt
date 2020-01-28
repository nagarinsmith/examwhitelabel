package com.examwhitelabel.view.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.examwhitelabel.R
import com.examwhitelabel.TheListBinding
import com.examwhitelabel.view.shared.TheBaseFragment
import com.examwhitelabel.view.shared.observeNonNull

class TheListFragment : TheBaseFragment<TheListBinding, TheListViewModel>(R.layout.fragment_list, TheListViewModel::class) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val theItemListAdapter = TheItemListAdapter { item -> findNavController().navigate(TheListFragmentDirections.toDetail(item)) }
        binding.itemList.adapter = theItemListAdapter
        binding.itemList.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.addItem.setOnClickListener { findNavController().navigate(TheListFragmentDirections.toAddOrEdit()) }
        viewModel.itemList.observeNonNull(viewLifecycleOwner, theItemListAdapter::submitList)
    }
}
