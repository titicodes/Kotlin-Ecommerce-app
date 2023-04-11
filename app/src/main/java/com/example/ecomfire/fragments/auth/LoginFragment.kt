package com.example.ecomfire.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomfire.R
import com.example.ecomfire.activities.MainActivity
import com.example.ecomfire.databinding.FragmentLoginBinding
import com.example.ecomfire.utils.AppState
import com.example.ecomfire.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding:FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonLoginLogin.setOnClickListener {
              val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                loginViewModel.loginWithEmailAndPassword(email,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            loginViewModel.login.collect{
                when(it){
                    is AppState.Loading ->{
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is AppState.Success -> {
                        binding.buttonLoginLogin.revertAnimation()
                       Intent(requireActivity(), MainActivity::class.java).also { intent ->
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                           startActivity(intent)
                       }
                    }
                    is AppState.Error -> {
                        Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_LONG).show()
                        binding.buttonLoginLogin.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.resetPassword.collect{
                when(it){
                    is AppState.Loading ->{

                    }
                    is AppState.Success -> {
                        Snackbar.make(requireView(),"Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is AppState.Error -> {
                        Snackbar.make(requireView(),"Error: ${it.message}",Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvForgotPasswordLogin.setOnClickListener {

        }
    }
}