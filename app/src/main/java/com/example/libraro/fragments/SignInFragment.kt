package com.example.libraro.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.libraro.R
import com.example.libraro.databinding.FragmentSignInBinding
import com.example.libraro.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var _binding: FragmentSignInBinding
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewBack.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_homeAccountFragment)
        }

        binding.textViewSignUp.setOnClickListener {  // Add this block
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        // Authenticating details to sign in
        binding.btnSignIn.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                        }, 1000)

                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.progressBar.visibility = View.GONE
                            binding.textViewMessages.setText(R.string.msgLoginFailed)
                        }, 1000)
                    }
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBar.visibility = View.GONE
                    binding.textViewMessages.setText(R.string.msgEmptyNotAccepted)
                }, 1000)

            }
        }

        binding.textViewForgetPassword.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_passwordResetFragment)
        }
    }
}