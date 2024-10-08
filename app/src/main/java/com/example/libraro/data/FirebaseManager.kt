package com.example.libraro.data

import com.example.libraro.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class FirebaseManager {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    fun signUpUser(email: String, password: String, username: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    firebaseUser?.let {
                        val user = User(
                            uid = it.uid,
                            username = username,
                            email = email
                        )
                        saveUserToDatabase(user) { success ->
                            onComplete(success, if (success) null else "Failed to save user details")
                        }
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    private fun saveUserToDatabase(user: User, onComplete: (Boolean) -> Unit) {
        database.child("users").child(user.uid).setValue(user)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getUserDetails(uid: String, onComplete: (User?) -> Unit) {
        database.child("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue<User?>()
                onComplete(user)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }
}