package com.example.ecomfire.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomfire.R
import com.example.ecomfire.activities.MainActivity
import com.example.ecomfire.databinding.FragmentIntroductionBinding
import com.example.ecomfire.viewmodels.IntroductionViewModel
import com.example.ecomfire.viewmodels.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.ecomfire.viewmodels.IntroductionViewModel.Companion.SHOPPING_ACTIVITY

class IntroductionFragment: Fragment(R.layout.fragment_introduction) {
    private lateinit var binding:FragmentIntroductionBinding
    private val introductionViewModel by viewModels<IntroductionViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }

        lifecycleScope.launchWhenStarted {
            introductionViewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(),MainActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    ACCOUNT_OPTIONS_FRAGMENT -> {
                        findNavController().navigate(it)
                    }


                }
            }
        }

        binding.buttonStart.setOnClickListener {
            introductionViewModel.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }
    }
}