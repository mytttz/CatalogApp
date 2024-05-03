package com.example.catalogapp.productscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogapp.dataclasses.Product
import com.example.catalogapp.network.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductScreenViewModel(
    private val repository: ProductRepository,
    request: Int?
) : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        request?.let {
            fetchProduct(it)
        }
    }

    private fun fetchProduct(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = repository.getProduct(id)
                    if (response.isSuccessful) {
                        _product.postValue(response.body())
                    } else {
                        _error.postValue("Failed to fetch movie: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred: ${e.message}")
            }
        }
    }
}