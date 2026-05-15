package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.DiagramItem
import com.example.financier.data.model.FeedbackRequest
import com.example.financier.data.model.Report
import com.example.financier.domain.operationUseCases.GetStatementOperationsUseCase
import com.example.financier.domain.operationUseCases.PatchFeedbackUseCase
import com.example.financier.domain.statementUseCases.GetAllStatementsUseCase
import com.example.financier.domain.statementUseCases.GetLatestStatementUseCase
import com.example.financier.domain.statementUseCases.GetReportUseCase
import com.example.financier.domain.statementUseCases.GetStatementUseCase
import com.example.financier.domain.statementUseCases.UploadStatementUseCase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Date
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getLatestStatementUseCase: GetLatestStatementUseCase,
    private val getReportUseCase: GetReportUseCase,
    private val uploadStatementUseCase: UploadStatementUseCase,
    private val sharedPreferences: SharedPreferences,
    private val getAllStatementsUseCase: GetAllStatementsUseCase,
    private val getStatementUseCase: GetStatementUseCase,
    private val getStatementOperationsUseCase: GetStatementOperationsUseCase,
    private val patchFeedbackUseCase: PatchFeedbackUseCase
) : ViewModel() {

    fun init()  { //TODO это тестовый блок для тестирования всех route
        viewModelScope.launch {
            Log.d("Тест", "Начало теста")

            val token = sharedPreferences.getString("auth_token", null)

            if (token.isNullOrEmpty()) {
                _uiState.value = UiState.Error("Пользователь не авторизован")
                return@launch
            }
            Log.d("Тест", "Токен: $token")

            Log.d("Тест", "Получаем все выписки")
            val statements = getAllStatementsUseCase(token)
            Log.d("Тест", "Все выписки: ${statements.toString()}")

            Log.d("Тест", "Получаем одну выписку")
            val statementId = "1cb67309-f573-4461-8627-f2262e06e4ef"
            val statement = getStatementUseCase(statementId, token)
            Log.d("Тест", "Выписка: ${statement.toString()}")

            Log.d("Тест", "Получаем операции выписки")
            val operations = getStatementOperationsUseCase(statementId, token)
            Log.d("Тест", "Операции выписки: ${operations.toString()}")

            Log.d("Тест", "Получаем отчёт выписки")
            val report = getReportUseCase(statementId, token)
            Log.d("Тест", "Отчёт: ${report.toString()}")

            Log.d("Тест", "Меняем фидбек по операции")
            val updatedOperation = patchFeedbackUseCase(
                "64f3059c-776d-4ec6-acd0-833c80ad5e9b",
                FeedbackRequest("Тестовый merchant"),
                token
            )
            Log.d("Тест", "Обновлённая операция: ${updatedOperation.toString()}")
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

    init {
        // Загружаем начальные данные
        loadTestStatementAutomatically()
    }

    /**
     * Автоматически загружает тестовую выписку при открытии MainFragment
     */
    private fun loadTestStatementAutomatically() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val token = sharedPreferences.getString("auth_token", null)

            if (token.isNullOrEmpty()) {
                _uiState.value = UiState.Error("Пользователь не авторизован")
                return@launch
            }

            val statement = getLatestStatementUseCase.invoke(token)

            if (statement != null) {
                loadReport(statement.id.toString(), token)
            } else {
                _uiState.value = UiState.Error("Тестовая выписка не найдена")
            }
        }
    }

    private fun loadReport(statementId: String, token: String) {
        viewModelScope.launch {
            val reportData = getReportUseCase.invoke(statementId, token)

            if (reportData != null) {
                _report.value = reportData
                _uiState.value = UiState.Success(reportData)

                // Преобразуем Report в список диаграмм для RecyclerView
                val diagramsList = convertReportToDiagrams(reportData)
                _diagrams.value = diagramsList
            } else {
                _uiState.value = UiState.Error("Не удалось загрузить отчёт")
            }
        }
    }

    /**
     * Преобразует данные отчёта в список объектов DiagramItem с данными для графиков
     */
    private fun convertReportToDiagrams(report: Report): List<DiagramItem> {
        val list = mutableListOf<DiagramItem>()

        // Защита от null
        val categories = report.expenseByCategory ?: emptyMap()

        // 1. Главная круговая диаграмма
        val pieEntries = categories.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        list.add(
            DiagramItem(
                id = "main_total",
                title = "Общие траты",
                isMain = true,
                pieData = pieEntries
            )
        )

        // 2. Диаграммы по категориям
        categories.forEach { (category, amount) ->
            val barEntries = listOf(BarEntry(0f, amount.toFloat()))

            list.add(
                DiagramItem(
                    id = "category_$category",
                    title = category,
                    isMain = false,
                    barData = barEntries,
                    categoryName = category
                )
            )
        }

        // Если нет категорий — добавляем заглушку
        if (list.isEmpty()) {
            list.add(DiagramItem(id = "empty", title = "Нет данных по расходам", isMain = true))
        }

        return list
    }

    /**
     * Обновление выбранного диапазона дат
     */
    fun setDateRange(startDate: Date, endDate: Date) {
        _selectedStartDate.value = startDate
        _selectedEndDate.value = endDate
    }

    /**
     * Загрузка файла на сервер
     */
    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading

            try {
//                kotlinx.coroutines.delay(1500)
                val token = sharedPreferences.getString("auth_token", null)

                if (token.isNullOrEmpty()) {
                    _uiState.value = UiState.Error("Пользователь не авторизован")
                    return@launch
                }

                val uploadedStatement = uploadStatementUseCase(uri, token)
                Log.d("Загруженный statement", uploadedStatement.toString())

                _uploadState.value = UploadState.Success("Файл успешно загружен")
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Ошибка загрузки файла")
            }
        }
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