package com.example.financier.presenter.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    private val diagramsAdapter = DiagramsAdapter { category, subcategory ->
        val action = MainFragmentDirections.actionMainFragmentToOperationsInCategoryFragment(
            category = category,
            subcategory = subcategory
        )
        findNavController().navigate(action)
    }

    private var loadingDialog: androidx.appcompat.app.AlertDialog? = null

    private fun showLoading(show: Boolean) {
        if (show) {
            if (loadingDialog == null) {
                val builder = MaterialAlertDialogBuilder(requireContext())
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_loading, null)

                builder.setView(view)
                    .setCancelable(false)

                loadingDialog = builder.create()
            }
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

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

        if (savedInstanceState == null) {
            viewModel.init()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerDiagrams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diagramsAdapter
        }
    }

    private fun setupDatePicker() {
        binding.dateText.text = "Выбрать даты"
        binding.dateText.setOnClickListener {
            showDateRangePicker()
        }
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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UiState.Loading -> showLoading(true)
                else -> showLoading(false)
            }
        }

        // Главная логика отображения
        viewModel.diagrams.observe(viewLifecycleOwner) { list ->
            val hasData = !list.isNullOrEmpty()

            binding.recyclerDiagrams.visibility = if (hasData) View.VISIBLE else View.GONE
            binding.dateText.visibility = if (hasData) View.VISIBLE else View.GONE
            binding.emptyState.visibility = if (hasData) View.GONE else View.VISIBLE

            if (hasData) {
                diagramsAdapter.submitList(list)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.UiState.Loading -> showLoading(true)
                else -> showLoading(false)
            }
        }

        viewModel.statementId.observe(viewLifecycleOwner) { id ->
            if (!id.isNullOrBlank()) {
                viewModel.waitingReport(id)
            }
        }

        viewModel.statementStatus.observe(viewLifecycleOwner) { status ->
            if (status == "report_ready") {
                viewModel.loadReport()
            }
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}