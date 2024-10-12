package com.example.libraro

import User
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navigationDrawerLayout: DrawerLayout
    private lateinit var emailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupWindowInsets()

        initializeViews()
        setupNavigation()
        setupFirebase()
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
        val db = FirebaseFirestore.getInstance()

        auth.currentUser?.let { user ->
            emailTextView.text = user.email
            navController.navigate(R.id.homeFragment)
        } ?: run {
            navController.navigate(R.id.homeAccountFragment)
        }
    }

    private fun setupNavigationItemListener(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> logoutUser()
                R.id.nav_settings -> Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            navigationDrawerLayout.closeDrawers()
            true
        }
    }

    private fun fetchUserDataAndUpdateUI(db: FirebaseFirestore, userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        val userFullName = "${it.firstName} ${it.lastName}"
                        nameTextView.text = userFullName
                    }
                } else {
                    nameTextView.text = "User Not Found"
                }
            }
            .addOnFailureListener { exception ->
                nameTextView.text = "Error Loading User"
                Log.e("MainActivity", "Error fetching user data", exception)
            }
    }

    private fun logoutUser() {
        AlertDialog.Builder(this).apply {
            setTitle("Confirm")
            setMessage("Are you sure you would like to logout?")
            setPositiveButton("YES") { _, _ ->
                try {
                    auth.signOut()
                    navController.navigate(R.id.action_homeFragment_to_homeAccountFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error logging out", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("NO") { dialog, _ -> dialog.dismiss() }
            create().show()
        }
    }
}