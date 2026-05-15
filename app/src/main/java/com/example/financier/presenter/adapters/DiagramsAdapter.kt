package com.example.financier.presenter.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financier.data.model.DiagramItem
import com.example.financier.databinding.ItemDiagramsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class DiagramsAdapter : ListAdapter<DiagramItem, DiagramsAdapter.DiagramViewHolder>(DiagramDiffCallback()) {

    class DiagramViewHolder(private val binding: ItemDiagramsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiagramItem) {
            binding.tvChartTitle.text = item.title

            if (item.isMain && item.pieData != null) {
                // Круговая диаграмма
                binding.barChart.isVisible = false

                val pieDataSet = PieDataSet(item.pieData, "").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    sliceSpace = 3f
                    selectionShift = 5f
                    valueTextSize = 12f
                    valueTextColor = Color.WHITE
                }

                val pieData = PieData(pieDataSet).apply {
                    setDrawValues(true)
                }

            } else if (item.barData != null) {
                // Столбчатая диаграмма
                binding.barChart.isVisible = true

                val barDataSet = BarDataSet(item.barData, item.categoryName ?: item.title).apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    valueTextSize = 12f
                    valueTextColor = Color.BLACK
                }

                val barData = BarData(barDataSet).apply {
                    barWidth = 0.7f
                }

                binding.barChart.apply {
                    data = barData
                    description.isEnabled = false
                    legend.isEnabled = false
                    setDrawGridBackground(false)
                    setDrawBorders(false)

                    xAxis.isEnabled = false
                    axisRight.isEnabled = false
                    axisLeft.apply {
                        valueFormatter = DefaultValueFormatter(0)
                        setDrawGridLines(true)
                    }

                    setDrawValueAboveBar(true)
                    setExtraOffsets(0f, 0f, 0f, 10f)
                    invalidate()
                }
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
    override fun areItemsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean =
        oldItem == newItem
}