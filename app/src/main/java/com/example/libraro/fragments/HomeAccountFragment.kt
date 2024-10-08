package com.example.libraro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.libraro.R
import com.google.firebase.auth.FirebaseAuth

class HomeAccountFragment : Fragment() {
    // declaring objects
    private lateinit var btnSignIn: Button
    private lateinit var textViewCreateAccount: TextView
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initializing objects
        btnSignIn = view.findViewById(R.id.btn_SignIn)
        textViewCreateAccount = view.findViewById(R.id.textViewCreateAccount)

        btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_homeAccountFragment_to_signInFragment)
        }

        textViewCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeAccountFragment_to_signUpFragment)
        }

    }

}