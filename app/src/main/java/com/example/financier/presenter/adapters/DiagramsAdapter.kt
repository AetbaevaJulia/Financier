package com.example.financier.presenter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financier.databinding.ItemDiagramsBinding
import com.example.financier.data.model.DiagramItem

class DiagramsAdapter : ListAdapter<DiagramItem, DiagramsAdapter.DiagramViewHolder>(DiagramDiffCallback()) {

    class DiagramViewHolder(private val binding: ItemDiagramsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiagramItem) {
            binding.textForDiagram.text = item.title

            item.imageRes?.let {
                binding.imageForDiagrams.setImageResource(it)
            }
            // Если будет Glide/Coil:
            // item.imageUrl?.let { url ->
            //     Glide.with(binding.root).load(url).into(binding.imageForDiagrams)
            // }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagramViewHolder {
        val binding = ItemDiagramsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiagramViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiagramViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiagramDiffCallback : DiffUtil.ItemCallback<DiagramItem>() {
    override fun areItemsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean =
        oldItem == newItem
}