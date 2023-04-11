package com.example.ecomfire.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomfire.R
import com.example.ecomfire.data.User
import com.example.ecomfire.databinding.FragmentSignupBinding
import com.example.ecomfire.utils.AppState
import com.example.ecomfire.utils.SignUpFieldState
import com.example.ecomfire.utils.SignUpValidation
import com.example.ecomfire.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "SignUpFragment"

@AndroidEntryPoint
class SignUpFragment:Fragment(R.layout.fragment_signup) {
private lateinit var binding:FragmentSignupBinding
private val signUpViewModel by viewModels<SignUpViewModel> ()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                signUpViewModel.signUpUserOnFirebase(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            signUpViewModel.signUpUser.collect{
                when(it){
                    is AppState.Loading->{
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is AppState.Success ->{
                       // Log.d("test",it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is AppState.Error ->{
                        Log.e(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
           signUpViewModel.validation.collect{ validation->
               if (validation.email is SignUpValidation.Failed){
                   withContext(Dispatchers.Main){
                       binding.edEmailRegister.apply {
                           requestFocus()
                           error = validation.email.message
                       }
                   }
               }

           }
        }

    }
}