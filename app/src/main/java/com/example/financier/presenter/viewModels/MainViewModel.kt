package com.example.financier.presenter.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.R
import com.example.financier.data.model.DiagramItem
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class MainViewModel @Inject constructor(
    // Здесь можно добавить ваши репозитории позже
    // private val diagramRepository: DiagramRepository,
    // private val uploadRepository: UploadRepository
) : ViewModel() {

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

    init {
        // Загружаем начальные данные
        loadDiagrams()
    }

    private fun loadDiagrams() {
        // Пока тестовые данные
        val sampleData = listOf(
            DiagramItem(
                id = "1",
                title = "Общие траты",
                imageRes = R.mipmap.ic_launcher
            ),
            DiagramItem(
                id = "2",
                title = "Расходы по категориям",
                imageRes = R.mipmap.ic_launcher
            ),
            DiagramItem(
                id = "3",
                title = "Доходы и расходы",
                imageRes = R.mipmap.ic_launcher
            )
        )

        _diagrams.value = sampleData
    }

    /**
     * Обновление выбранного диапазона дат
     */
    fun setDateRange(startDate: Date, endDate: Date) {
        _selectedStartDate.value = startDate
        _selectedEndDate.value = endDate

        // Здесь можно сделать перезагрузку диаграмм с новым периодом
        // loadDiagramsByDateRange(startDate, endDate)
    }

    /**
     * Загрузка файла на сервер
     */
    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading

            try {
                // TODO: Реальная загрузка через Repository
                // val result = uploadRepository.uploadFile(uri)

                // Имитация загрузки
                kotlinx.coroutines.delay(1500)

                _uploadState.value = UploadState.Success("Файл успешно загружен")

                // После успешной загрузки можно обновить список диаграмм
                // loadDiagrams()

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
}