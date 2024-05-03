package com.example.catalogapp.network

import androidx.paging.PagingSource
import com.example.catalogapp.dataclasses.Product
import com.example.catalogapp.dataclasses.Tune
import retrofit2.Response

class ProductRepository(private val apiService: ApiService) {


    suspend fun getProduct(id: Int): Response<Product> {
        return apiService.getProduct(id)
    }

    fun getProductsPagingSource(
        query: String? = null,
        tune: Tune? = null
    ): PagingSource<Int, Product> {
        return ProductPagingSource(apiService, query, tune)
    }
}