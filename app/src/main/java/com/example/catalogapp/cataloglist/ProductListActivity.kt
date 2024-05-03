package com.example.catalogapp.cataloglist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogapp.R
import com.example.catalogapp.TuneListener
import com.example.catalogapp.dataclasses.Tune
import com.example.catalogapp.network.ApiService
import com.example.catalogapp.network.ProductListViewModelFactory
import com.example.catalogapp.network.ProductRepository
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView


class ProductListActivity : AppCompatActivity(), TuneListener {

    private lateinit var productRecycler: RecyclerView
    private lateinit var searchBarInputText: SearchView
    private lateinit var productSearchList: RecyclerView
    private lateinit var searchBar: SearchBar
    private lateinit var viewModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        productRecycler = findViewById(R.id.productList)
        searchBarInputText = findViewById(R.id.searchBarInputText)
        productSearchList = findViewById(R.id.productSearchList)
        searchBar = findViewById(R.id.searchBar)

        viewModel =
            ViewModelProvider(
                this,
                ProductListViewModelFactory(ProductRepository(ApiService.create()))
            )[ProductListViewModel::class.java]

        val adapter = ProductAdapter(this, viewModel)
        val adapterSearch = ProductSearchAdapter(this, viewModel)

        searchBar.setOnMenuItemClickListener {
            val modalBottomSheet = TuneBottomSheet()
            modalBottomSheet.setTuneListener(this)
            modalBottomSheet.show(supportFragmentManager, TuneBottomSheet.TAG)
            return@setOnMenuItemClickListener true
        }


        productRecycler.adapter = adapter
        productRecycler.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        viewModel.products.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        val handler = Handler(Looper.getMainLooper())

        searchBarInputText.editText.addTextChangedListener { _ ->
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                productSearchList.adapter = adapterSearch
                productSearchList.layoutManager =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                if (searchBarInputText.editText.text.isNotBlank()) {
                    viewModel.fetchProducts(query = searchBarInputText.editText.text.toString())
                    viewModel.searchedProducts.observe(this) { pagingData ->
                        adapterSearch.submitData(lifecycle, pagingData)
                    }
                } else {
                    viewModel.searchedProducts.observe(this) { _ ->
                        adapterSearch.submitData(lifecycle, PagingData.empty())
                    }
                }
            }, 1000)
        }

    }

    override fun onTuneCreated(tune: Tune) {
        if (tune.category.isNotEmpty()) {
            viewModel.fetchProducts(tune = tune)
        } else {
            viewModel.fetchProducts()
        }
    }
}