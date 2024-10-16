package com.example.libraro.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraro.R
import com.google.firebase.auth.FirebaseAuth
import com.example.libraro.adapter.BooksAdapter
import com.example.libraro.model.Book
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var bookAdapter: BooksAdapter
    private val bookList = mutableListOf<Book>()
    private val crimeBooksList = mutableListOf<Book>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.custom_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Pressed", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setupMenu()

        recyclerView = view.findViewById(R.id.recyclerviewBooks)

        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
        }


        bookAdapter = BooksAdapter(bookList)

        bookAdapter.onBookClick = {
            val intent = Intent(requireContext(), BookDetailsActivity::class.java)
            intent.putExtra("current_book", it)
            startActivity(intent)
        }

        recyclerView.adapter = bookAdapter


        fetchBooksFromFirestore()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

                        if(book.category == "Crime") {
                            crimeBooksList.add(book)
                        }else if(book.category == "Fiction"){
                            bookList.add(book)
                        }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        if (bookList.isEmpty()) {
            Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
        } else {
            bookAdapter.notifyDataSetChanged()
        }
    }
}