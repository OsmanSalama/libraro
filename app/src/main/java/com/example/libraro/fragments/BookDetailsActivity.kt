package com.example.libraro.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.libraro.R
import com.example.libraro.databinding.ActivityBookDetailsBinding
import com.example.libraro.model.Book

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // binding setup
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val book = intent.getParcelableExtra<Book>("current_book")

        val toolbar = findViewById<Toolbar>(R.id.custom_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = book?.title
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (book != null) {
            uiSetup(book)
        }
        setupHeaderColor()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRatingImage(rating: Double, imageView: ImageView) {
        val drawableId = when {
            rating <= 0.5 -> R.drawable.half_star
            rating <= 1.0 -> R.drawable.one_star
            rating <= 1.5 -> R.drawable.one_and_half_stars
            rating <= 2.0 -> R.drawable.two_stars
            rating <= 2.5 -> R.drawable.two_and_half_stars
            rating <= 3.0 -> R.drawable.three_stars
            rating <= 3.5 -> R.drawable.three_and_half_stars
            rating <= 4.0 -> R.drawable.four_stars
            rating <= 4.5 -> R.drawable.four_and_half_stars
            else -> R.drawable.five_stars
        }
        imageView.setImageResource(drawableId)
    }

    private fun uiSetup(book: Book){
        binding.textViewTitle.text = book.title
        binding.textViewAuthor.text = book.author
        binding.textViewDescription.text = book.description
        binding.textViewPrice.text = "Price: £${book.price.toString()}"
        binding.textViewRating.text = book.rating.toString()
        setRatingImage(book.rating, binding.imageViewRating)

        Glide.with(this)
            .load(book.coverImageUrl)
            .placeholder(R.drawable.while_loading_cover)
            .error(R.drawable.book_item)
            .into(binding.imageViewCover)
    }

    private fun setupHeaderColor(){
        // Get the window associated with the current activity
        val window = window
        // Allow the status bar to draw its own background
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the very top bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.gruvbox_dark_soft)
    }
}