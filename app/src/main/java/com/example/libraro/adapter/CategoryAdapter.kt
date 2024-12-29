package com.example.libraro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.libraro.R
import com.example.libraro.model.Category

class CategoryAdapter(private val categoryList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var onCategoryClick: ((Category) -> Unit)? = null

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewCategoryTitle)
        var imageViewCategoryImage: ImageView = itemView.findViewById(R.id.imageViewCategoryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.titleTextView.text = category.name
        holder.imageViewCategoryImage.setImageResource(category.imageDescription)

        holder.itemView.setOnClickListener {
            onCategoryClick?.invoke(category)
        }
    }

    override fun getItemCount() = categoryList.size
}