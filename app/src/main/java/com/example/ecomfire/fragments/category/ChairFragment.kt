package com.example.ecomfire.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecomfire.data.Category
import com.example.ecomfire.utils.AppState
import com.example.ecomfire.viewmodels.CategoryViewModel
import com.example.ecomfire.viewmodels.factory.BaseCategoryViewModelFactoryFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class ChairFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactoryFactory(firestore, Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is AppState.Loading -> {
                        showOfferLoading()
                    }
                    is AppState.Success -> {
                        offerAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    }
                    is AppState.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hideOfferLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is AppState.Loading -> {
                        showBestProductsLoading()
                    }
                    is AppState.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()
                    }
                    is AppState.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hideBestProductsLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onBestProductsPagingRequest() {

    }

    override fun onOfferPagingRequest() {

    }
}