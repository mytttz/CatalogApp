package com.example.catalogapp.network

import com.example.catalogapp.dataclasses.Product

data class ProductsResponse(
    val products: List<Product>
)