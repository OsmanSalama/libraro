package com.example.libraro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraro.R
import com.example.libraro.adapter.CategoryAdapter
import com.example.libraro.databinding.FragmentCategoriesBinding
import com.example.libraro.model.Category
import androidx.navigation.fragment.findNavController


class CategoriesFragment : Fragment() {

    private lateinit var _binding: FragmentCategoriesBinding
    private val binding get() = _binding
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerViewCategories
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        categoryAdapter = CategoryAdapter(categoryList)

        categoryAdapter.onCategoryClick = {
            val bundle = Bundle()
            bundle.putParcelable("currentCategory", it)
            findNavController().navigate(R.id.action_categoriesFragment_to_categoryFragment, bundle)
        }

        recyclerView.adapter = categoryAdapter


        fetchCategories()
    }

    private fun fetchCategories() {
        val literature = Category("Literature", imageDescription = R.drawable.crime_book)
        val science = Category("Science", imageDescription = R.drawable.crime_book)
        val history = Category("History", imageDescription = R.drawable.crime_book)
        val geography = Category("Geography", imageDescription = R.drawable.crime_book)
        val selfDevelopment = Category("Self Development", imageDescription = R.drawable.crime_book)
        val religion = Category("Religion", imageDescription = R.drawable.crime_book)
        val romance = Category("Romance", imageDescription = R.drawable.crime_book)
        val politics = Category("Politics", imageDescription = R.drawable.crime_book)
        val health = Category("Health", imageDescription = R.drawable.crime_book)
        val fiction = Category("Fiction", imageDescription = R.drawable.crime_book)
        val scienceFiction = Category("Science Fiction", imageDescription = R.drawable.crime_book)
        val crimeFiction = Category("Crime", imageDescription = R.drawable.crime_book)
        val psychology = Category("Psychology", imageDescription = R.drawable.crime_book)
        val language = Category("Language", imageDescription = R.drawable.crime_book)
        categoryList.clear()
        categoryList.add(literature)
        categoryList.add(science)
        categoryList.add(history)
        categoryList.add(geography)
        categoryList.add(selfDevelopment)
        categoryList.add(religion)
        categoryList.add(romance)
        categoryList.add(politics)
        categoryList.add(health)
        categoryList.add(fiction)
        categoryList.add(scienceFiction)
        categoryList.add(crimeFiction)
        categoryList.add(psychology)
        categoryList.add(language)
    }

}