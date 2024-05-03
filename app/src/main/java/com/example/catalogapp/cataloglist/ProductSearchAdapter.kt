package com.example.catalogapp.cataloglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogapp.R
import com.example.catalogapp.RoundedCornerTransformation
import com.example.catalogapp.dataclasses.Product
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class ProductSearchAdapter(
    private val context: Context,
    private val viewModel: ProductListViewModel
) :
    PagingDataAdapter<Product, ProductSearchAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productPoster: ImageView = itemView.findViewById(R.id.productPoster)
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val productProgress: CircularProgressIndicator = itemView.findViewById(R.id.productProgress)


        init {
            itemView.setOnClickListener {
                viewModel.selectedProduct(context, getItem(absoluteAdapterPosition)?.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = getItem(position)?.title
        holder.price.text = getItem(position)?.price
        holder.rating.text = buildString {
            append("Rating: ")
            append(getItem(position)?.rating?.toString())
        }
        Picasso.get()
            .load(getItem(position)?.thumbnail)
            .resize(400, 400)
            .centerCrop()
            .transform(RoundedCornerTransformation())
            .into(holder.productPoster,
                object : Callback {
                    override fun onSuccess() {
                        holder.productProgress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        holder.productProgress.visibility = View.VISIBLE
                    }
                }
            )
    }


    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}