package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.OperationEntity
import com.example.financier.domain.operationUseCases.GetOperationsByCategoryUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class OperationsInCategoryViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getOperationsByCategoryUseCase: GetOperationsByCategoryUseCase
) : ViewModel() {

    private val _operations = MutableLiveData<List<OperationEntity>>()
    val operations: LiveData<List<OperationEntity>> get() = _operations

    private val _totalAmount = MutableLiveData<Double>(0.0)
    val totalAmount: LiveData<Double> get() = _totalAmount

    fun loadOperations(category: String, subcategory: String? = null) {
        viewModelScope.launch {
            val savedStart = sharedPreferences.getLong("selected_start_date", 0)
            val savedEnd = sharedPreferences.getLong("selected_end_date", 0)

            getOperationsByCategoryUseCase.invoke(category, savedStart, savedEnd).collectLatest { list ->
                val filteredList = if (!subcategory.isNullOrBlank() && subcategory != "Без подкатегории") {
                    list.filter { it.subcategory == subcategory }
                } else {
                    list
                }

                _operations.value = filteredList
                _totalAmount.value = filteredList.sumOf { it.amount }
            }
        }
    }
}