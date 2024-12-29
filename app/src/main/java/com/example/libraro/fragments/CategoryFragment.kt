package com.example.libraro.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraro.R
import com.example.libraro.adapter.BooksAdapter
import com.example.libraro.databinding.FragmentCategoryBinding
import com.example.libraro.model.Book
import com.example.libraro.model.Category
import com.google.firebase.firestore.FirebaseFirestore

class CategoryFragment : Fragment() {
    private lateinit var _binding: FragmentCategoryBinding
    private val binding get() = _binding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var bookAdapter: BooksAdapter
    private val bookList = mutableListOf<Book>()
    private lateinit var recyclerView: RecyclerView
    private var currentCategory: Category? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        @Suppress("DEPRECATION")
        currentCategory = arguments?.getParcelable("currentCategory")
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // managing the recycler view
        bookAdapter = BooksAdapter(bookList)
        recyclerView = binding.recyclerviewBooks
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
        }

        // managing action bar
        binding.customToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            title = "Genre: ${currentCategory?.name}"

            setNavigationOnClickListener {
                @Suppress("DEPRECATION")
                activity?.onBackPressed()
            }
        }


        bookAdapter = BooksAdapter(bookList)

        bookAdapter.onBookClick = {
            val intent = Intent(requireContext(), BookDetailsActivity::class.java)
            intent.putExtra("current_book", it)
            startActivity(intent)
        }

        recyclerView.adapter = bookAdapter
        binding.textView4.text = "Latest ${currentCategory?.name} Uploads"

        fetchBooksFromFirestore()
    }

    private fun fetchBooksFromFirestore() {
        val collectionRef = db.collection("books")
        collectionRef.get()
            .addOnSuccessListener { result ->
                bookList.clear()
                for (document in result) {
                    try {
                        val data = document.data
                        Log.d("HomeFragment", "Document data: $data")

                        val book = document.toObject(Book::class.java)
                        book.id = document.id

                        if(book.category == currentCategory?.name) {
                            bookList.add(book)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error adding book", Toast.LENGTH_SHORT).show()
                    }
                }
                Log.d("HomeFragment", "Fetched ${bookList.size} books")
                updateUI()
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error fetching documents", exception)
                Toast.makeText(context, "Failed to load books: ${exception.message}", Toast.LENGTH_LONG).show()
            }

    }
    
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun updateUI() {
        if (bookList.isEmpty()) {
            Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
            binding.textViewNoBooks.visibility = View.VISIBLE
            binding.imageViewErrorImage.visibility = View.VISIBLE
            binding.textView4.visibility = View.GONE
            binding.view.visibility = View.GONE
            binding.textViewNoBooks.text = "There are no books for the genre: ${currentCategory?.name}\n\n Yet"
        } else {
            bookAdapter.notifyDataSetChanged()
        }
    }
}