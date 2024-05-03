package com.example.catalogapp.productscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogapp.R
import com.example.catalogapp.RoundedCornerTransformation
import com.example.catalogapp.network.ApiService
import com.example.catalogapp.network.ProductListViewModelFactory
import com.example.catalogapp.network.ProductRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.squareup.picasso.Picasso


class ProductScreenActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ProductListViewModelFactory(
                ProductRepository(ApiService.create()),
                intent.getIntExtra("EXTRA_ID", -1)
            )
        )[ProductScreenViewModel::class.java]
    }
    private lateinit var productName: TextView
    private lateinit var productdescription: TextView
    private lateinit var productPrice: TextView
    private lateinit var productStock: TextView
    private lateinit var productBrand: TextView
    private lateinit var productCategory: TextView
    private lateinit var productRating: TextView
    private lateinit var productImages: TextView
    private lateinit var toolBar: MaterialToolbar
    private lateinit var productImage: ImageView
    private lateinit var carouselImages: RecyclerView
    private lateinit var progressIndicator: LinearProgressIndicator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_screen)
        productName = findViewById(R.id.productName)
        productdescription = findViewById(R.id.description)
        productPrice = findViewById(R.id.price)
        productStock = findViewById(R.id.stock)
        productBrand = findViewById(R.id.brand)
        productCategory = findViewById(R.id.category)
        productRating = findViewById(R.id.rating)
        productImages = findViewById(R.id.images)
        toolBar = findViewById(R.id.topAppBar)
        productImage = findViewById(R.id.productImage)
        carouselImages = findViewById(R.id.carouselImages)
        progressIndicator = findViewById(R.id.progressIndicator)


        carouselImages.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        CarouselSnapHelper().attachToRecyclerView(carouselImages)

        val adapterImage = ImageAdapter()
        carouselImages.adapter = adapterImage

        viewModel.product.observe(this) { product ->
            with(product ?: return@observe) {
                progressIndicator.visibility = View.GONE
                productName.text = title
                productdescription.text = description

                productPrice.text = getString(R.string.product_price, price)
                productStock.text = getString(R.string.product_stock, stock)
                productBrand.text = getString(R.string.product_brand, brand)
                productCategory.text = getString(
                    R.string.product_category,
                    category.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                productRating.text = getString(R.string.product_rating, rating.toString())


                toolBar.title = title

                Picasso.get()
                    .load(thumbnail)
                    .transform(RoundedCornerTransformation())
                    .into(productImage)


                if (product.images.isNotEmpty()) {
                    carouselImages.visibility = View.VISIBLE
                    productImages.text = getString(R.string.images)
                    adapterImage.submitList(product.images)
                } else {
                    carouselImages.visibility = View.GONE
                    productImages.text = getString(R.string.no_image_information)
                }
            }
        }

        toolBar.setNavigationOnClickListener {
            finish()
        }
    }
}