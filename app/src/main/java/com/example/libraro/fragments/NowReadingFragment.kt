package com.example.libraro.fragments

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
import com.example.libraro.databinding.FragmentNowReadingBinding
import com.example.libraro.databinding.FragmentSignInBinding
import com.example.libraro.model.Book
import com.google.firebase.firestore.FirebaseFirestore

class NowReadingFragment : Fragment() {
    private lateinit var _binding: FragmentNowReadingBinding
    private val binding get() = _binding

    private lateinit var bookAdapter: BooksAdapter
    private val bookList = mutableListOf<Book>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNowReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "Hello there", Toast.LENGTH_SHORT).show()

        recyclerView = binding.recyclerviewBooks
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        bookAdapter = BooksAdapter(bookList)
        recyclerView.adapter = bookAdapter
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
                        bookList.add(book)
                        Log.d("HomeFragment", "Added book: ${book.title}")
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error processing document ${document.id}", e)
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

    private fun updateUI() {
        if (bookList.isEmpty()) {
            Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
        } else {
            bookAdapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Found ${bookList.size} books", Toast.LENGTH_SHORT).show()
        }
    }
}