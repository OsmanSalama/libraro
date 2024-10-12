package com.example.libraro.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraro.R
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import com.bumptech.glide.Glide
import com.example.libraro.adapter.BooksAdapter
import com.example.libraro.model.Book
import com.example.libraro.model.User
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var bookAdapter: BooksAdapter
    private val bookList = mutableListOf<Book>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.custom_toolbar)

        // code to the set the toolbar as the actionbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Get the DrawerLayout from MainActivity
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)

        // Set up ActionBarDrawerToggle
        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        // setting the menu icon on the left of the bar
        //(activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //toolbar.setNavigationIcon(R.drawable.ic_menu)

        // Sync the state of ActionBarDrawerToggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Enable home button (drawer toggle button)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        // click listener when menu icon is clicked
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Pressed", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        // Setup RecyclerView
        val bookRecyclerView = view.findViewById<RecyclerView>(R.id.bookRecyclerView)
        val layoutManager = GridLayoutManager(requireContext(), 5) // Change here to use GridLayoutManager
        bookRecyclerView.layoutManager = layoutManager
        bookAdapter = BooksAdapter(bookList)
        bookRecyclerView.adapter = bookAdapter

        // Fetch data from Firestore
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
        db.collection("books").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val book = document.toObject(Book::class.java)
                    bookList.add(book)
                }
                bookAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e(tag,"Error loading data: $e")
            }
    }


}