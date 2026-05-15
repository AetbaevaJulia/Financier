package com.example.financier.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.financier.R
import com.example.financier.appComponent
import com.example.financier.databinding.FragmentRegistrationBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.viewModels.AuthViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class RegisterFragment : Fragment(R.layout.fragment_registration) {

    private val binding: FragmentRegistrationBinding by viewBinding(FragmentRegistrationBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AuthViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeAuthState()
        viewModel.init()
    }

    private fun setupClickListeners() {
        binding.btToRegistration.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.editTextEmail.error = "Введите email"
                    binding.editTextEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.editTextPassword.error = "Введите пароль"
                    binding.editTextPassword.requestFocus()
                }
                password.length < 6 -> {
                    binding.editTextPassword.error = "Пароль должен содержать минимум 6 символов"
                    binding.editTextPassword.requestFocus()
                }
                else -> {
                    viewModel.register(email, password)
                }
            }
        }

        binding.buttonToAuth.setOnClickListener {
            if (findNavController().previousBackStackEntry?.destination?.id == R.id.authFragment)
            {
                findNavController().popBackStack()
            }
            else {
                findNavController().navigate(R.id.action_registerFragment_to_authFragment)
            }
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    showLoading(true)
                }

                is AuthViewModel.AuthState.Success -> {
                    showLoading(false)
                    onRegistrationSuccess(state)
                }

                is AuthViewModel.AuthState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }

                is AuthViewModel.AuthState.Idle -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun onRegistrationSuccess(authResponse: AuthViewModel.AuthState.Success) {
        findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btToRegistration.isEnabled = !isLoading
        binding.btToRegistration.text = if (isLoading) "Загрузка..." else "Зарегистрироваться"
        binding.buttonToAuth.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
        binding.editTextPassword.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

        when {
            message.contains("409", ignoreCase = true) || message.contains("already exists", ignoreCase = true) -> {
                binding.editTextEmail.error = "Пользователь с таким email уже существует"
                binding.editTextEmail.requestFocus()
            }
            message.contains("validation", ignoreCase = true) -> {
                binding.editTextEmail.error = "Некорректный email"
                binding.editTextEmail.requestFocus()
            }
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}