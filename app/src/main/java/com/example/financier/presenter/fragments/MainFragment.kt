package com.example.financier.presenter.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val diagramsAdapter = DiagramsAdapter()

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupDatePicker()
        setupAddNewButton()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerDiagrams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diagramsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.diagrams.observe(viewLifecycleOwner) { list ->
            diagramsAdapter.submitList(list)
        }

        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UploadState.Loading -> {
                    Toast.makeText(requireContext(), "Загрузка файла...", Toast.LENGTH_SHORT).show()
                }
                is MainViewModel.UploadState.Success -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                is MainViewModel.UploadState.Error -> {
                    Toast.makeText(requireContext(), "Ошибка: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun setupDatePicker() {
        binding.dateText.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()

        // Можно задать начальные даты, если нужно
        // val calendar = Calendar.getInstance()
        // builder.setSelection(
        //     Pair(
        //         calendar.timeInMillis,
        //         calendar.timeInMillis + 30 * 24 * 60 * 60 * 1000L // +30 дней
        //     )
        // )

        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first?.let { Date(it) }
            val endDate = selection.second?.let { Date(it) }

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dateRangeText = "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"

            binding.dateText.text = dateRangeText

            // Можно обновить ViewModel
            // viewModel.updateDateRange(startDate, endDate)
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
            type = "*/*"  // Все типы файлов
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFileLauncher.launch(intent)
    }


    // Для выбора файла
    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                uploadFileToApi(uri)
            }
        }
    }

    private fun uploadFileToApi(uri: Uri) {
        // TODO: Реализация отправки файла на API
        viewModel.uploadFile(uri)
    }


    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}