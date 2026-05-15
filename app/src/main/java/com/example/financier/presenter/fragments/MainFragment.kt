package com.example.financier.presenter.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentMainBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.adapters.DiagramsAdapter
import com.example.financier.presenter.viewModels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.androidbroadcast.vbpd.viewBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val diagramsAdapter = DiagramsAdapter()

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                uploadFileToApi(uri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupDatePicker()
        setupAddNewButton()
        setupObservers()

        viewModel.init() //TODO запуск тестового блока
    }

    private fun setupRecyclerView() {
        binding.recyclerDiagrams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diagramsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupDatePicker() {
        binding.dateText.setOnClickListener {
            showDateRangePicker()
        }

        // Автоматическая установка дат: месяц назад — сегодня
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val startDate = calendar.time

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        binding.dateText.text = "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"
    }

    private fun showDateRangePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker().build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startDate = Date(selection.first ?: System.currentTimeMillis())
            val endDate = Date(selection.second ?: System.currentTimeMillis())

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            binding.dateText.text = "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"

            viewModel.setDateRange(startDate, endDate)
        }

        picker.show(parentFragmentManager, "date_range_picker")
    }

    private fun setupAddNewButton() {
        binding.addNew.setOnClickListener {
            showUploadModal()
        }
    }

    private fun showUploadModal() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Загрузка файла")
            .setMessage("Выберите файл для загрузки в систему")
            .setPositiveButton("Загрузить") { _, _ ->
                openFilePicker()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFileLauncher.launch(intent)
    }

    private fun uploadFileToApi(uri: Uri) {
        viewModel.uploadFile(uri)
    }

    private fun setupObservers() {
        viewModel.diagrams.observe(viewLifecycleOwner) { list ->
            diagramsAdapter.submitList(list)
        }

        viewModel.report.observe(viewLifecycleOwner) { report ->
            report?.let {
                Toast.makeText(requireContext(), "Отчёт загружен (${it.totalExpense.toInt()} ₽ расходов)", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UiState.Loading ->
                    Toast.makeText(requireContext(), "Загрузка тестовой выписки...", Toast.LENGTH_SHORT).show()

                is MainViewModel.UiState.Success ->
                    Toast.makeText(requireContext(), "Тестовая выписка загружена успешно", Toast.LENGTH_SHORT).show()

                is MainViewModel.UiState.Error ->
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UploadState.Loading ->
                    Toast.makeText(requireContext(), "Загрузка файла...", Toast.LENGTH_SHORT).show()

                is MainViewModel.UploadState.Success ->
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()

                is MainViewModel.UploadState.Error -> {
                    Log.d("Ошибка", state.message)
                    Toast.makeText(requireContext(), "Ошибка: ${state.message}", Toast.LENGTH_LONG).show()
                }

                MainViewModel.UploadState.Idle -> TODO()
            }
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}