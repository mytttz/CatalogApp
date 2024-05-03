package com.example.catalogapp.cataloglist

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.catalogapp.dataclasses.Product
import com.example.catalogapp.dataclasses.Tune
import com.example.catalogapp.network.ProductRepository
import com.example.catalogapp.productscreen.ProductScreenActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductListViewModel(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _products = MutableLiveData<PagingData<Product>>()
    val products: LiveData<PagingData<Product>> get() = _products

    private val _searchedProducts = MutableLiveData<PagingData<Product>>()
    val searchedProducts: LiveData<PagingData<Product>> get() = _searchedProducts


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchProducts()
    }

    fun fetchProducts(query: String? = null, tune: Tune? = null) {
        val pagingSource = repository.getProductsPagingSource(query, tune)
        val pager = Pager(PagingConfig(pageSize = 20)) {
            pagingSource
        }
        viewModelScope.launch {
            if (query != null) {
                pager.flow.cachedIn(viewModelScope).collectLatest {
                    _searchedProducts.postValue(it)
                }
            } else {
                pager.flow.cachedIn(viewModelScope).collectLatest {
                    _products.postValue(it)
                }
            }
        }
    }

    fun selectedProduct(context: Context, id: Int?) {
        val intentProductScreen = Intent(context, ProductScreenActivity::class.java)
        intentProductScreen.putExtra("EXTRA_ID", id)
        context.startActivity(intentProductScreen)
    }
}

