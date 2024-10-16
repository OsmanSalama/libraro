package com.example.libraro

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.libraro.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navigationDrawerLayout: DrawerLayout
    private lateinit var emailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var navController: NavController
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()
        initializeViews()
        setupNavigation()
        setupFirebase()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        // hiding bottom navigation bar if the current page is the AccountHomeFragment,
        // SignInFragment, or the SignUpFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.signUpFragment, R.id.homeAccountFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when(item.itemId){


                R.id.home_nav -> {
                    Toast.makeText(this, "home has been clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.categories_nav -> {
                    Toast.makeText(this, "categories has been clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.favorite_nav -> {
                    Toast.makeText(this, "favorites has been clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViews() {
        navigationDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        emailTextView = headerView.findViewById(R.id.nav_header_email)
        nameTextView = headerView.findViewById(R.id.nav_header_user_name)

        setupNavigationItemListener(navigationView)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupFirebase() {
        auth = FirebaseAuth.getInstance()

        auth.currentUser?.let { user ->
            emailTextView.text = user.email
            nameTextView.text = fetchUserData().firstName
            navController.navigate(R.id.homeFragment)
        } ?: run {
            navController.navigate(R.id.homeAccountFragment)
        }
    }

    private fun setupNavigationItemListener(navigationView: NavigationView) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> logoutUser()
                R.id.nav_settings -> Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_nowReading -> navController.navigate(R.id.nowReadingFragment)
                R.id.nav_home -> navController.navigate(R.id.homeFragment)
            }
            navigationDrawerLayout.closeDrawers()
            true
        }
    }

    private fun fetchUserData(): User {
        val collectionRef = db.collection("users")
        var currentUser = User()
        collectionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    try {
                        if(document.id == auth.currentUser?.uid){
                            currentUser = document.toObject(User::class.java)
                        }

                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error loading user's data", e)
                    }
                }
                Log.d("HomeFragment", "Fetched current user")
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error fetching documents", exception)
                Toast.makeText(this, "Failed to load books: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        return currentUser
    }

    private fun logoutUser() {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle("Confirm")
            setMessage("Are you sure you would like to logout?")
            setPositiveButton("YES") { dialog, _ ->
                try {
                    auth.signOut()
                    navController.navigate(R.id.action_homeFragment_to_homeAccountFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error logging out", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            setNegativeButton("NO") { dialog, _ -> dialog.dismiss() }
        }.create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white))
        }

        alertDialog.show()
    }
}