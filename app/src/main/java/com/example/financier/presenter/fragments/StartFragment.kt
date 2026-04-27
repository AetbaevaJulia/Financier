package com.example.financier.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentStartBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.viewModels.MainViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}