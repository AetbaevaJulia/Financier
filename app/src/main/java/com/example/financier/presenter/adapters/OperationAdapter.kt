package com.example.financier.presenter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financier.data.mappers.toRequest
import com.example.financier.data.model.OperationEntity
import com.example.financier.databinding.ItemListOperationsBinding
import java.text.SimpleDateFormat
import java.util.*

class OperationsAdapter : ListAdapter<OperationEntity, OperationsAdapter.OperationViewHolder>(OperationDiffCallback()) {

    class OperationViewHolder(private val binding: ItemListOperationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OperationEntity) {
            binding.tvTitle.text = item.normalizedDescription
            binding.tvAmount.text = "${item.amount} ₽"

            binding.tvDate.text = item.toRequest().operationDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val binding = ItemListOperationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class OperationDiffCallback : DiffUtil.ItemCallback<OperationEntity>() {
    override fun areItemsTheSame(oldItem: OperationEntity, newItem: OperationEntity): Boolean = oldItem.operationId == newItem.operationId
    override fun areContentsTheSame(oldItem: OperationEntity, newItem: OperationEntity): Boolean = oldItem == newItem
}