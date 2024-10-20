package com.example.libraro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.libraro.databinding.FragmentBookBinding
import com.example.libraro.model.Book

class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        val book = arguments?.getParcelable<Book>("currentBook")

        if (book != null) {
            Log.d("BookFragment", "Book received: ${book.title}")
        } else {
            Log.e("BookFragment", "Book is null")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
