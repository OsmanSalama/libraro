package com.example.libraro.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.libraro.R
import com.example.libraro.model.Book

class BookDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_details)

        //val book = intent.getParcelableExtra<Book>("current_book")



    }
}