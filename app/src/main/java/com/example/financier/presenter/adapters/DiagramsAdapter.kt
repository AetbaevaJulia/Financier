package com.example.financier.presenter.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financier.data.model.DiagramItem
import com.example.financier.databinding.ItemDiagramsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class DiagramsAdapter(
    private val onCategoryClick: (category: String, subcategory: String?) -> Unit
) : ListAdapter<DiagramItem, DiagramsAdapter.DiagramViewHolder>(DiagramDiffCallback()) {

    var isMainPieChart: Boolean = true

    class DiagramViewHolder(private val binding: ItemDiagramsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiagramItem, isMainPie: Boolean, onClick: (String, String?) -> Unit) {
            binding.tvChartTitle.text = item.title

            val chartColors: List<Int> = listOf(
                0xFF558EDE.toInt(), 0xFFED6154.toInt(), 0xFFAAB1B9.toInt(),
                0xFFA5C26E.toInt(), 0xFF9074BE.toInt(), 0xFFFFAA40.toInt(),
                0xFF4DB6C9.toInt(), 0xFFEEA0C3.toInt(), 0xFF91687A.toInt(),
                0xFF45867A.toInt(), 0xFF9B4A08.toInt()
            )

            // === Главные диаграммы (переключаемые) ===
            if (item.isMain && item.pieData != null) {

                val isTopMerchants = item.id == "top_merchants"

                if (isMainPie) {
                    // ==================== PIE CHART ====================
                    binding.pieChart.isVisible = true
                    binding.barChart.isVisible = false

                    val pieDataSet = PieDataSet(item.pieData, "").apply {
                        colors = chartColors
                        selectionShift = 8f
                        valueTextSize = 13f
                        valueTextColor = Color.WHITE
                        valueFormatter = PercentFormatter(binding.pieChart)
                    }

                    val pieData = PieData(pieDataSet)

                    binding.pieChart.apply {
                        data = pieData
                        isDrawHoleEnabled = true
                        setHoleColor(Color.WHITE)
                        description.isEnabled = false
                        holeRadius = 52f
                        transparentCircleRadius = 58f

                        setCenterTextSize(15f)
                        centerText = "Всего\n${item.totalAmount?.toInt() ?: ""} ₽"

                        setDrawEntryLabels(false)

                        legend.apply {
                            isEnabled = true
                            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                            orientation = Legend.LegendOrientation.HORIZONTAL
                            isWordWrapEnabled = true
                            textSize = 13f
                            formSize = 11f
                            xEntrySpace = 12f
                            yEntrySpace = 8f
                        }

                        // Убираем кликабельность у "Самые большие траты"
                        if (isTopMerchants) {
                            setOnChartValueSelectedListener(null)
                        } else {
                            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                override fun onValueSelected(e: Entry?, h: Highlight?) {
                                    if (e is PieEntry) {
                                        onClick(e.label, null)
                                    }
                                }
                                override fun onNothingSelected() {}
                            })
                        }

                        invalidate()
                    }

                } else {
                    binding.pieChart.isVisible = false
                    binding.barChart.isVisible = true

                    val entries = ArrayList<BarEntry>()
                    val labels = ArrayList<String>()

                    item.pieData.forEachIndexed { index, pieEntry ->
                        labels.add(pieEntry.label)
                        entries.add(BarEntry(index.toFloat(), pieEntry.value))
                    }

                    val barDataSet = BarDataSet(entries, "").apply {
                        colors = chartColors
                        valueTextSize = 11f
                        valueTextColor = Color.BLACK
                    }

                    val barData = BarData(barDataSet).apply { barWidth = 0.65f }

                    binding.barChart.apply {
                        data = barData
                        description.isEnabled = false
                        legend.isEnabled = false

                        // === Настройки X-оси ===
                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            valueFormatter = IndexAxisValueFormatter(labels)
                            granularity = 1f
                            setDrawGridLines(false)
                            textSize = 10f
                            labelRotationAngle = -35f
                            setLabelCount(labels.size, false)
                            setAvoidFirstLastClipping(true)
                            isGranularityEnabled = true

                        }

                        axisRight.isEnabled = false
                        axisLeft.apply {
                            valueFormatter = DefaultValueFormatter(0)
                            setDrawGridLines(true)
                        }

                        setDrawValueAboveBar(true)
                        extraBottomOffset = 55f        // увеличил отступ для двухстрочных подписей

                        // === Горизонтальный скролл ===
                        isDragEnabled = true
                        setScaleEnabled(true)
                        isScaleXEnabled = true
                        isScaleYEnabled = false
                        setVisibleXRangeMaximum(6f)    // видно максимум 6 столбцов за раз
                        setVisibleXRangeMinimum(3f)

                        // Кликабельность
                        if (item.id == "top_merchants") {
                            setOnChartValueSelectedListener(null)
                        } else {
                            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                override fun onValueSelected(e: Entry?, h: Highlight?) {
                                    if (e != null) {
                                        val index = e.x.toInt()
                                        if (index in labels.indices) {
                                            onClick(labels[index], null)
                                        }
                                    }
                                }
                                override fun onNothingSelected() {}
                            })
                        }

                        invalidate()
                    }
                }

            }
            // === Столбчатые диаграммы по подкатегориям (старый код) ===
            else if (item.subcategoryData != null) {
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true

                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()

                val sorted = item.subcategoryData.entries.sortedByDescending { it.value }

                sorted.forEachIndexed { index, (name, value) ->
                    labels.add(name)
                    entries.add(BarEntry(index.toFloat(), value.toFloat()))
                }

                val barDataSet = BarDataSet(entries, "").apply {
                    colors = chartColors
                    valueTextSize = 11f
                    valueTextColor = Color.BLACK
                }

                val barData = BarData(barDataSet).apply { barWidth = 0.65f }

                binding.barChart.apply {
                    data = barData
                    description.isEnabled = false
                    legend.isEnabled = false

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        valueFormatter = IndexAxisValueFormatter(labels)
                        granularity = 1f
                        setDrawGridLines(false)
                        textSize = 11f
                    }

                    axisRight.isEnabled = false
                    axisLeft.apply {
                        valueFormatter = DefaultValueFormatter(0)
                        setDrawGridLines(true)
                    }

                    setDrawValueAboveBar(true)
                    setExtraBottomOffset(20f)

                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e != null) {
                                val index = e.x.toInt()
                                if (index in sorted.indices) {
                                    val subcat = sorted[index].key
                                    onClick(item.categoryName ?: "", subcat)
                                }
                            }
                        }
                        override fun onNothingSelected() {}
                    })

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
        val item = getItem(position)
        holder.bind(item, isMainPieChart, onCategoryClick)
    }
}

class DiagramDiffCallback : DiffUtil.ItemCallback<DiagramItem>() {
    override fun areItemsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: DiagramItem, newItem: DiagramItem): Boolean = oldItem == newItem
}