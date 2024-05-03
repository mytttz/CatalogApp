package com.example.catalogapp.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catalogapp.cataloglist.ProductListViewModel
import com.example.catalogapp.productscreen.ProductScreenViewModel


class ProductListViewModelFactory(
    private val repository: ProductRepository,
    private val additionalIntValue: Int? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ProductScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductScreenViewModel(repository, additionalIntValue ?: 0) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

