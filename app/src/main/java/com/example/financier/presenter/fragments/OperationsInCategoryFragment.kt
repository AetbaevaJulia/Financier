package com.example.financier.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentOperationsInCategoryBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.viewModels.OperationsInCategoryViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class OperationsInCategoryFragment : Fragment(R.layout.fragment_operations_in_category) {

    private val binding: FragmentOperationsInCategoryBinding by viewBinding(FragmentOperationsInCategoryBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OperationsInCategoryViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}