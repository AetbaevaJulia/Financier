package com.example.financier.data.model

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry


data class DiagramItem(
    val id: String,
    val title: String,
    val imageRes: Int? = null,
    val isMain: Boolean = false,           // для общей круговой диаграммы
    val pieData: List<PieEntry>? = null,   // для PieChart
    val barData: List<BarEntry>? = null,   // для BarChart
    val categoryName: String? = null
)