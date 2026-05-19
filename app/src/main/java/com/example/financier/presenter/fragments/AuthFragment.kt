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
import com.example.financier.databinding.FragmentAuthBinding
import com.example.financier.di.viewModel.ViewModelFactory
import com.example.financier.presenter.viewModels.AuthViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding: FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)

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
        binding.btToAuth.setOnClickListener {
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
                else -> {
                    viewModel.login(email, password)
                }
            }
        }

        binding.buttonToRegistration.setOnClickListener {
            if (findNavController().previousBackStackEntry?.destination?.id == R.id.registerFragment)
            {
                findNavController().popBackStack()
            }
            else {
                findNavController().navigate(R.id.action_authFragment_to_registerFragment)
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
                    onLoginSuccess(state)
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

    private fun onLoginSuccess(authResponse: AuthViewModel.AuthState.Success) {
        findNavController().navigate(R.id.action_authFragment_to_mainFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btToAuth.isEnabled = !isLoading
        binding.btToAuth.text = if (isLoading) "Вход..." else "Войти"
        binding.buttonToRegistration.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
        binding.editTextPassword.isEnabled = !isLoading
    }

    private fun showError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

            when {
                message.contains("401", ignoreCase = true) ||
                        message.contains("unauthorized", ignoreCase = true) ||
                        message.contains("invalid credentials", ignoreCase = true) -> {
                    binding.editTextPassword.error = "Неверный email или пароль"
                    binding.editTextPassword.requestFocus()
                    binding.editTextPassword.text?.clear()
                }

                message.contains("validation", ignoreCase = true) -> {
                    binding.editTextEmail.error = "Некорректный email"
                    binding.editTextEmail.requestFocus()
                }

                message.contains("network", ignoreCase = true) ||
                        message.contains("connection", ignoreCase = true) -> {
                    Toast.makeText(
                        requireContext(),
                        "Проверьте подключение к интернету",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}