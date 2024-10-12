package com.example.libraro.data

import com.example.libraro.model.User
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    private fun fetchUserData(db: FirebaseFirestore, userId: String): User {
        var userToReturn: User = User()
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        val userFullName = "${it.firstName} ${it.lastName}"
                        userToReturn = user

                    }
                } else {
                    //nameTextView.text = "com.example.libraro.model.User Not Found"
                }
            }
            .addOnFailureListener { exception ->
                //nameTextView.text = "Error Loading com.example.libraro.model.User"
                Log.e("MainActivity", "Error fetching user data", exception)
            }
        return userToReturn
    }
}