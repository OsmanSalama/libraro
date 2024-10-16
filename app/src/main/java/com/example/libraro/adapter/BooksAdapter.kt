package com.example.libraro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraro.R
import com.example.libraro.model.Book

class BooksAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {
    var onBookClick: ((Book) -> Unit)? = null

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewBookTitle)
        val categoryTextView: TextView = itemView.findViewById(R.id.textViewBookCategory)
        val imageViewCover: ImageView = itemView.findViewById(R.id.imageViewCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.titleTextView.text = book.title
        holder.categoryTextView.text = book.author
        Glide.with(holder.itemView.context)
            .load(book.coverImageUrl)
            .placeholder(R.drawable.while_loading_cover)
            .error(R.drawable.book_item)
            .into(holder.imageViewCover)

        holder.itemView.setOnClickListener {
            onBookClick?.invoke(book)
        }
    }

    override fun getItemCount() = bookList.size
}
