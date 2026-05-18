package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.DiagramItem
import com.example.financier.data.model.OperationResponse
import com.example.financier.data.model.Report
import com.example.financier.domain.operationUseCases.GetStatementOperationsUseCase
import com.example.financier.domain.operationUseCases.PatchFeedbackUseCase
import com.example.financier.domain.statementUseCases.GetAllStatementsUseCase
import com.example.financier.domain.statementUseCases.GetLastReportUseCase
import com.example.financier.domain.statementUseCases.GetReportUseCase
import com.example.financier.domain.statementUseCases.GetStatementUseCase
import com.example.financier.domain.statementUseCases.UploadStatementUseCase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import javax.inject.Inject
import kotlin.math.absoluteValue

class MainViewModel @Inject constructor(
    private val getLastReportUseCase: GetLastReportUseCase,
    private val getReportUseCase: GetReportUseCase,
    private val uploadStatementUseCase: UploadStatementUseCase,
    private val sharedPreferences: SharedPreferences,
    private val getAllStatementsUseCase: GetAllStatementsUseCase,
    private val getStatementUseCase: GetStatementUseCase,
    private val getStatementOperationsUseCase: GetStatementOperationsUseCase,
    private val patchFeedbackUseCase: PatchFeedbackUseCase
) : ViewModel() {

    fun init()  {
        viewModelScope.launch {
            if(_report.value == null){
                loadLastReport()
            }
        }
    }

    // === Данные для RecyclerView ===
    private val _diagrams = MutableLiveData<List<DiagramItem>>()
    val diagrams: LiveData<List<DiagramItem>> = _diagrams

    // === Выбранный диапазон дат ===
    private val _selectedStartDate = MutableLiveData<Date>()
    val selectedStartDate: LiveData<Date> = _selectedStartDate

    private val _selectedEndDate = MutableLiveData<Date>()
    val selectedEndDate: LiveData<Date> = _selectedEndDate

    // === Состояние загрузки файла ===
    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> = _uploadState

    // === Новые поля для работы с отчётом ===
    private val _report = MutableLiveData<Report?>()
    val report: LiveData<Report?> get() = _report

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    /**
     * Автоматически загружает тестовую выписку при открытии MainFragment
     */

    fun loadLastReport(){
        viewModelScope.launch {
            val reportData = getLastReportUseCase.invoke()

            if (reportData != null) {
                _report.postValue(reportData)
                _uiState.value = UiState.Success(reportData)

                val diagramsList = convertReportToDiagrams(reportData)
                _diagrams.postValue(diagramsList)
            } else {
                _uiState.value = UiState.Error("Не удалось загрузить отчёт")
            }
        }
    }

    fun loadReport(statementId: String? = null) {
        viewModelScope.launch {
            val id = statementId ?: this@MainViewModel.statementId.value
            if (id.isNullOrBlank()) {
                _uiState.value = UiState.Error("Не найден ID выписки")
                return@launch
            }

            val token = getToken()
            val reportData = getReportUseCase.invoke(id, token)

            Log.d("Загруженный репорт", reportData.toString())

            if (reportData != null) {
                _report.postValue(reportData)
                _uiState.value = UiState.Success(reportData)

                val diagramsList = convertReportToDiagrams(reportData)  // теперь suspend
                _diagrams.postValue(diagramsList)
            } else {
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

        val categories = report.expenseByCategory ?: emptyMap()

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

        // 2. Столбчатые диаграммы по подкатегориям
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
    fun setDateRange(startDate: Date, endDate: Date) {
        _selectedStartDate.value = startDate
        _selectedEndDate.value = endDate
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
            _uploadState.value = UploadState.Loading

            try {
//                kotlinx.coroutines.delay(1500)
                val token = getToken()

                val uploadedStatement = uploadStatementUseCase(uri, token)
                Log.d("Загруженный statement", uploadedStatement.toString())

                _statementId.postValue(uploadedStatement?.statementId.toString())
                _statementStatus.postValue(uploadedStatement?.status.toString())

                _uploadState.value = UploadState.Success("Файл успешно загружен")
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Ошибка загрузки файла")
            }
        }
    }

    fun waitingReport(statementId : String){
        viewModelScope.launch {
            try{
                while (_statementStatus.value != "report_ready") {
                    val statement = getStatementUseCase(statementId, getToken())
                    if (statement?.status == "report_ready" && _statementStatus.value != "report_ready") {
                        _statementStatus.postValue("report_ready")
                    }
                    else if (statement?.status == "failed") {
                        throw Exception("статус failed")
                    }
                    kotlinx.coroutines.delay(3000)
                }
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Ошибка получения отчета")
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

    // Состояния загрузки файла
    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val message: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val report: Report) : UiState()
        data class Error(val message: String) : UiState()
    }
}