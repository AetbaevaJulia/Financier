package com.example.financier.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentOperationsInCategoryBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.adapters.OperationsAdapter
import com.example.financier.presenter.viewModels.MainViewModel
import com.example.financier.presenter.viewModels.OperationsInCategoryViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class OperationsInCategoryFragment : Fragment(R.layout.fragment_operations_in_category) {

    private val binding: FragmentOperationsInCategoryBinding by viewBinding(FragmentOperationsInCategoryBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OperationsInCategoryViewModel by viewModels { viewModelFactory }

    private val operationsAdapter = OperationsAdapter()

    private val category: String by lazy {
        arguments?.getString("category") ?: ""
    }

    private val subcategory: String? by lazy {
        arguments?.getString("subcategory")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("OperationsInCategory", "category=$category, subcategory=$subcategory")

        binding.title.text = if (subcategory.isNullOrBlank()) {
            "Расходы в категории \"$category\""
        } else {
            "Расходы в \"$subcategory\""
        }

        setupRecyclerView()
        setupClickListeners()
        observeData()

        if (category.isNotEmpty()) {
            viewModel.loadOperations(category, subcategory)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerOperations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = operationsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeData() {
        viewModel.operations.observe(viewLifecycleOwner) { ops ->
            operationsAdapter.submitList(ops)
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = "${String.format("%.2f", total)} ₽"
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}