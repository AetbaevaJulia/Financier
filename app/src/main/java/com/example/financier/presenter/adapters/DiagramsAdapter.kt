package com.example.financier.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financier.data.model.DiagramItem
import com.example.financier.databinding.ItemDiagramsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class DiagramsAdapter : ListAdapter<DiagramItem, DiagramsAdapter.DiagramViewHolder>(DiagramDiffCallback()) {

    class DiagramViewHolder(private val binding: ItemDiagramsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiagramItem) {
            binding.tvChartTitle.text = item.title

            if (item.isMain && item.pieData != null) {
                // Главная круговая диаграмма
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false

                val pieDataSet = PieDataSet(item.pieData, "Категории").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    valueTextSize = 11f
                }

                binding.pieChart.data = PieData(pieDataSet)
                binding.pieChart.description.isEnabled = false
                binding.pieChart.legend.isEnabled = true
                binding.pieChart.setDrawEntryLabels(true)
                binding.pieChart.invalidate()

            } else if (item.barData != null) {
                // Столбчатая диаграмма по категории
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true

                val barDataSet = BarDataSet(item.barData, item.categoryName ?: "")
                barDataSet.color = ColorTemplate.MATERIAL_COLORS[0]

                binding.barChart.data = BarData(barDataSet)
                binding.barChart.description.isEnabled = false
                binding.barChart.legend.isEnabled = false
                binding.barChart.invalidate()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagramViewHolder {
        val binding = ItemDiagramsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiagramViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiagramViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiagramDiffCallback : DiffUtil.ItemCallback<DiagramItem>() {
    override fun areItemsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean = oldItem == newItem
}