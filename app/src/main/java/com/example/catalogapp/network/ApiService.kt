package com.example.catalogapp.network

import com.example.catalogapp.dataclasses.Product
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") page: Int,
        @Query("limit") limit: Int = 20,
    ): Response<ProductsResponse>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("skip") page: Int,
        @Query("limit") limit: Int = 20,
        @Query("q") query: String,
    ): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int,
    ): Response<Product>

    @GET("products/category/{category}")
    suspend fun getCategory(
        @Path("category") category: String,
        @Query("skip") page: Int,
        @Query("limit") limit: Int = 20,
    ): Response<ProductsResponse>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}