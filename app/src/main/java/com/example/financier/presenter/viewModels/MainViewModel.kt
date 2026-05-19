package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.DiagramItem
import com.example.financier.data.model.OperationEntity
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.model.Report
import com.example.financier.domain.operationUseCases.GetOperationsByCategoryUseCase
import com.example.financier.domain.operationUseCases.GetStatementOperationsUseCase
import com.example.financier.domain.operationUseCases.PatchFeedbackUseCase
import com.example.financier.domain.reportUseCases.GetLastReportUseCase
import com.example.financier.domain.reportUseCases.GetReportUseCase
import com.example.financier.domain.statementUseCases.GetAllStatementsUseCase
import com.example.financier.domain.statementUseCases.GetStatementUseCase
import com.example.financier.domain.statementUseCases.UploadStatementUseCase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import javax.inject.Inject
import kotlin.math.absoluteValue
import androidx.core.content.edit
import java.sql.Timestamp

class MainViewModel @Inject constructor(
    private val getLastReportUseCase: GetLastReportUseCase,
    private val getReportUseCase: GetReportUseCase,
    private val uploadStatementUseCase: UploadStatementUseCase,
    private val sharedPreferences: SharedPreferences,
    private val getAllStatementsUseCase: GetAllStatementsUseCase,
    private val getStatementUseCase: GetStatementUseCase,
    private val getStatementOperationsUseCase: GetStatementOperationsUseCase,
    private val patchFeedbackUseCase: PatchFeedbackUseCase,
    private val getOperationsByCategoryUseCase: GetOperationsByCategoryUseCase
) : ViewModel() {

    fun init()  {
        viewModelScope.launch {
            if(_report.value == null){
                loadLastReport()
            }

            val savedStart = sharedPreferences.getLong("selected_start_date", 0)
            val savedEnd = sharedPreferences.getLong("selected_end_date", 0)

            if (savedStart != 0L && savedEnd != 0L) {
                _selectedDateRange.postValue(listOf(savedStart, savedEnd))
            }
        }
    }

    // === Данные для RecyclerView ===
    private val _diagrams = MutableLiveData<List<DiagramItem>>()
    val diagrams: LiveData<List<DiagramItem>> = _diagrams

    // === Выбранный диапазон дат ===
    private val _selectedDateRange = MutableLiveData<List<Long?>>()
    val selectedDateRange: LiveData<List<Long?>> = _selectedDateRange


    // === Состояние загрузки файла ===
    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> = _uploadState

    // === Для работы с отчётом ===
    private val _report = MutableLiveData<Report?>()
    val report: LiveData<Report?> get() = _report

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val _operations = MutableLiveData<List<OperationEntity>>()
    val operations: LiveData<List<OperationEntity>> get() = _operations

    private val _totalAmount = MutableLiveData<Double>(0.0)
    val totalAmount: LiveData<Double> get() = _totalAmount

//    fun loadOperations(category: String, subcategory: String? = null) {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//
//            getOperationsByCategoryUseCase.invoke(
//                category,
//                _selectedStartDate.value?.time ?: 0,
//                _selectedEndDate.value?.time ?: 0
//            ).collectLatest { list ->
//                val filteredList = if (!subcategory.isNullOrBlank() && subcategory != "Без подкатегории") {
//                    list.filter { it.subcategory == subcategory }
//                } else {
//                    list
//                }
//
//                _operations.value = filteredList
//                _totalAmount.value = filteredList.sumOf { it.amount }
//                _uiState.value = UiState.Success // или отдельный Success без report
//            }
//        }
//    }

    /**
     * Автоматически загружает тестовую выписку при открытии MainFragment
     */

    fun loadLastReport() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val reportData = getLastReportUseCase.invoke()

            if (reportData != null) {
                _report.value = reportData
                val diagramsList = convertReportToDiagrams(reportData)
                _diagrams.value = diagramsList
                _uiState.value = UiState.Success

                val transactions = getStatementOperationsUseCase.invoke(reportData.statementId, getToken())

                val startDate = Timestamp.valueOf("${transactions?.last()?.operationDate} 00:00:00").time
                val endDate = Timestamp.valueOf("${transactions?.first()?.operationDate} 00:00:00").time

                setDateRange(startDate, endDate)
            } else {
                _diagrams.value = emptyList()
                _uiState.value = UiState.Error("Не удалось загрузить отчёт")
            }
        }
    }

    fun loadReport(statementId: String? = null) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val id = statementId ?: this@MainViewModel.statementId.value
            if (id.isNullOrBlank()) {
                _diagrams.value = emptyList()
                _uiState.value = UiState.Error("Не найден ID выписки")
                return@launch
            }

            val token = getToken()
            val reportData = getReportUseCase.invoke(id, token)

            if (reportData != null) {
                _report.value = reportData
                Log.d("diagramsList", "Вызываю")
                val diagramsList = convertReportToDiagrams(reportData)
                Log.d("diagramsList", "Вызов закончился")
                _diagrams.value = diagramsList
                _uiState.value = UiState.Success

                val transactions = getStatementOperationsUseCase.invoke(reportData.statementId, token)

                val startDate = Timestamp.valueOf("${transactions?.last()?.operationDate} 00:00:00").time
                val endDate = Timestamp.valueOf("${transactions?.first()?.operationDate} 00:00:00").time

                setDateRange(startDate, endDate)
            } else {
                _diagrams.value = emptyList()
                _uiState.value = UiState.Error("Не удалось загрузить отчёт")
            }
        }
    }

    /**
     * Преобразует данные отчёта в список объектов DiagramItem с данными для графиков
     */
    /**
     * Преобразует данные отчёта в список объектов DiagramItem с данными для графиков
     */
    private suspend fun convertReportToDiagrams(report: Report): List<DiagramItem> {
        val list = mutableListOf<DiagramItem>()

        val categories = report.expenseByCategory

        if (categories.isEmpty()) {
            list.add(DiagramItem(id = "empty", title = "Нет данных", isMain = true))
            return list
        }

        // 1. Большая круговая диаграмма
        val pieEntries = categories.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        list.add(
            DiagramItem(
                id = "main_pie",
                title = "Расходы по категориям",
                isMain = true,
                pieData = pieEntries,
                totalAmount = report.totalExpense
            )
        )

        // === ВТОРАЯ ДИАГРАММА: Самые большие траты (Top Merchants) ===
        val topMerchants = report.topMerchants ?: emptyList()
        if (topMerchants.isNotEmpty()) {
            val limitedTop = topMerchants.take(10)

            val topPieEntries = limitedTop.map { merchant ->
                PieEntry(merchant.amount.toFloat(), merchant.merchant)
            }

            list.add(
                DiagramItem(
                    id = "top_merchants",
                    title = "Самые большие траты",
                    isMain = true,
                    pieData = topPieEntries,
                    totalAmount = limitedTop.sumOf { it.amount }
                )
            )
        }

        // 2. Столбчатые диаграммы по подкатегориям
        Log.d("diagramsList", "Столбчатые диаграммы ${categories.toString()}")
        for ((category, _) in categories) {
            val subcategories = getSubcategories(category, report.statementId)
            list.add(
                DiagramItem(
                    id = "bar_$category",
                    title = category.replaceFirstChar { it.uppercaseChar() },
                    subcategoryData = subcategories,
                    categoryName = category
                )
            )
        }

        return list
    }

    private suspend fun getSubcategories(category: String, statementId: String): Map<String, Double> {
        val subcategories = mutableMapOf<String, Double>()
        try {
            val token = getToken()

            val transactions = getStatementOperationsUseCase.invoke(statementId, token)

            transactions?.forEach { transaction ->
                if (transaction.category == category) {
                    val subName = transaction.subcategory ?: "Без подкатегории"
                    subcategories[subName] = (subcategories[subName] ?: 0.0) + transaction.amount.absoluteValue
                }
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Ошибка получения подкатегорий для $category", e)
        }

        return subcategories
    }


    /**
     * Обновление выбранного диапазона дат
     */
    fun setDateRange(startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            _selectedDateRange.postValue(listOf(startDate, endDate))

            sharedPreferences.edit { putLong("selected_start_date", startDate ?: 0) }
            sharedPreferences.edit { putLong("selected_end_date", endDate ?: 0) }
        }
    }

    private val _statementId = MutableLiveData<String?>()
    val statementId: LiveData<String?> get() = _statementId

    private val _statementStatus = MutableLiveData<String?>()
    val statementStatus: LiveData<String?> get() = _statementStatus

    /**
     * Загрузка файла на сервер
     */
    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uploadState.value = UploadState.Loading

            try {
                val token = getToken()
                val uploadedStatement = uploadStatementUseCase(uri, token)

                _statementId.value = uploadedStatement?.statementId?.toString()
                _statementStatus.value = uploadedStatement?.status?.toString()

                _uploadState.value = UploadState.Success("Файл успешно загружен")
                // UiState останется Loading до окончания waitingReport
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Ошибка загрузки файла")
                _uiState.value = UiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun waitingReport(statementId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                while (_statementStatus.value != "report_ready") {
                    val statement = getStatementUseCase(statementId, getToken())

                    Log.d("Обновляю статус", "${statement?.statementId} = ${statement?.status}")
                    if (statement?.status == "report_ready") {
                        _statementStatus.value = "report_ready"
                        break
                    } else if (statement?.status == "failed") {
                        throw Exception("Статус failed")
                    }

                    kotlinx.coroutines.delay(5000)
                }

                // После успешного ожидания — загружаем отчёт
                loadReport(statementId)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Ошибка получения отчета")
                _uiState.value = UiState.Error(e.message ?: "Ошибка получения отчета")
            }
        }
    }

    private fun getToken() : String {
            val token = sharedPreferences.getString("auth_token", null)

            if (token.isNullOrEmpty()) {
                _uiState.value = UiState.Error("Пользователь не авторизован")
                throw Exception("Беда с токеном")
            }
            return token
    }

    private val _isMainPieChart = MutableLiveData<Boolean>(true)
    val isMainPieChart: LiveData<Boolean> get() = _isMainPieChart

    fun toggleMainChartType() {
        Log.d("MainViewModel", isMainPieChart.value.toString())
        _isMainPieChart.value = _isMainPieChart.value?.not() ?: true
        Log.d("MainViewModel", isMainPieChart.value.toString())
    }

    // Состояния загрузки файла
    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val message: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}