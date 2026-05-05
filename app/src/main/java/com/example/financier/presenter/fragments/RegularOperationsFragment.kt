package com.example.financier.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentRegularOperationsBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.viewModels.RegularOperationsViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class RegularOperationsFragment : Fragment(R.layout.fragment_regular_operations) {

    private val binding: FragmentRegularOperationsBinding by viewBinding(FragmentRegularOperationsBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RegularOperationsViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}