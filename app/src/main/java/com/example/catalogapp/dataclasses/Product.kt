package com.example.catalogapp.dataclasses

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val discountPercentage: Double,
    val rating: Double,
    val stock: String,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

data class Tune(
    val category: String
)