package com.examwhitelabel.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.examwhitelabel.TheItemBinding
import com.examwhitelabel.data.TheItem

class TheItemListAdapter(private val onClick: (item: TheItem) -> Unit) :
    ListAdapter<TheItem, TheItemListAdapter.TheItemViewHolder>(object : DiffUtil.ItemCallback<TheItem>() {
        override fun areItemsTheSame(oldItem: TheItem, newItem: TheItem) = oldItem.id != newItem.id

        override fun areContentsTheSame(oldItem: TheItem, newItem: TheItem) = oldItem != newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheItemViewHolder {
        val binding = TheItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener { binding.itemModel?.let(onClick) }
        return TheItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TheItemViewHolder, position: Int) {
        holder.binding.itemModel = getItem(position)
        holder.binding.executePendingBindings()
    }

    class TheItemViewHolder(val binding: TheItemBinding) : RecyclerView.ViewHolder(binding.root)
}
