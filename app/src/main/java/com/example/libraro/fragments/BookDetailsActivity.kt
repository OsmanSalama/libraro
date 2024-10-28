package com.example.libraro.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import kotlin.math.round

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
        
        binding.imageViewAddToWishList.setOnClickListener {
            Toast.makeText(this, "nice", Toast.LENGTH_SHORT).show()
        }

        binding.btnReadNow.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("currentBook", book)
                val fragment = BookFragment()
                fragment.arguments = bundle

                binding.appBarLayout.layoutParams.height = 0
                binding.appBarLayout.requestLayout()
                (binding.appBarLayout.parent as? ViewGroup)?.removeView(binding.appBarLayout)
                supportActionBar?.hide()
                setTheme(R.style.Theme_Libraro_NoActionBar)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

        }

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

    @SuppressLint("SetTextI18n")
    private fun uiSetup(book: Book){
        binding.textViewTitle.text = book.title
        binding.textViewAuthor.text = book.author

        if(book.description.length<= 260){
            binding.textViewDescription.text = book.description
            binding.textViewReadMore.visibility = View.GONE
        }else{
            binding.textViewDescription.text = cutDescription(book)
        }


        binding.textViewPrice.text = "Price: £${book.price}"
        binding.textViewRating.text = book.rating.toString()
        setRatingImage(book.rating, binding.imageViewRating)

        Glide.with(this)
            .load(book.coverImageUrl)
            .placeholder(R.drawable.while_loading_cover)
            .error(R.drawable.book_item)
            .into(binding.imageViewCover)

        binding.textViewReadMore.setOnClickListener {
            if(binding.textViewDescription.text.length <= 260){
                binding.textViewDescription.text = book.description
                binding.textViewReadMore.text = "Read Less"
            }else{
                binding.textViewDescription.text = cutDescription(book)
                binding.textViewReadMore.text = "Read More"
            }
        }

        val totalWordsInThousands: Double = round(book.totalWords.toDouble()/1000)
        binding.textViewWordsValue.text = "${totalWordsInThousands}K"
        binding.textViewPagesValue.text = book.numberOfPages.toString()
        binding.textViewReadTimeValue.text = book.hoursToRead.toString()
    }

    private fun cutDescription(book: Book) ="${book.description.substring(0, 250)}..."

    private fun setupHeaderColor(){
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gruvbox_dark_soft)
    }
}