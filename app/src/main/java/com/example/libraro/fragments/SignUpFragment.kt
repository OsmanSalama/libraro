package com.example.libraro.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libraro.R
import com.example.libraro.databinding.FragmentSignUpBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click listeners
        binding.imageView.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_homeAccountFragment)
        }
        binding.textViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        // Firebase Authentication
        binding.btnSignUp.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            fstore = FirebaseFirestore.getInstance()
            binding.progressBar2.visibility = View.VISIBLE
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && binding.editTextConfirmPassword.text.toString().isNotEmpty()){

                if(password == binding.editTextConfirmPassword.text.toString()){

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Handler(Looper.getMainLooper()).postDelayed({

                                // storing the user's data into the database
                                val userId = auth.currentUser?.uid.toString()
                                val currentTimestamp = System.currentTimeMillis()

                                val documentReference: DocumentReference = fstore.collection("users").document(userId)

                                val user: MutableMap<String, Any> = mutableMapOf(
                                    "firstName" to binding.editTextFirstName.text.toString(),
                                    "lastName" to binding.editTextLastName.text.toString(),
                                    "emailAddress" to binding.editTextEmail.text.toString(),
                                    "profilePicture" to "",
                                    "totalReadingTime" to 0L,
                                    "booksCompleted" to 0,
                                    "purchasedBooks" to listOf<String>(),
                                    "favoriteBooks" to listOf<String>(),
                                    "wishlistBooks" to listOf<String>(),
                                    "readingProgress" to mapOf<String, Any>(),
                                    "themePreference" to "light",
                                    "lastLoginDate" to currentTimestamp
                                )
                                documentReference.set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Success saving data into database", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                binding.progressBar2.visibility = View.GONE
                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                            }, 1000)

                        }else{
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.progressBar2.visibility = View.GONE
                                binding.textViewMessages.setText(R.string.msgLoginFailed)
                            }, 1000)

                        }
                    }

                }else{
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar2.visibility = View.GONE
                        binding.textViewMessages.setText(R.string.msgNotMatching)
                    }, 1000)
                }

            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBar2.visibility = View.GONE
                    binding.textViewMessages.setText(R.string.msgEmptyNotAccepted)
                }, 1000)

            }
        }
    }
}