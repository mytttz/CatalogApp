package com.example.catalogapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.catalogapp.dataclasses.Tune
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductPagingSource<T : Any>(
    private val apiService: ApiService,
    private val query: String? = null,
    private val tune: Tune? = null,
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return withContext(Dispatchers.IO) {
            try {
                val nextPageNumber = params.key ?: 0
                val response = when {
                    query != null -> {
                        apiService.searchProducts(nextPageNumber, query = query)
                    }

                    tune != null -> {
                        apiService.getCategory(tune.category, nextPageNumber)
                    }

                    else -> apiService.getProducts(nextPageNumber)

                }
                return@withContext if (response.isSuccessful) {
                    val items = response.body()?.let { body ->
                        when (body) {
                            is ProductsResponse -> body.products
                            else -> emptyList()
                        }
                    } ?: emptyList()

                    LoadResult.Page(
                        data = items as List<T>,
                        prevKey = if (nextPageNumber == 0) null else nextPageNumber - 20,
                        nextKey = if (items.isEmpty()) null else nextPageNumber + 20
                    )
                } else {
                    LoadResult.Error(Exception("Error fetching data"))
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition
    }
}
