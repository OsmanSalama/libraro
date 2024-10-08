package com.example.libraro.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.libraro.R
import com.example.libraro.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
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
        binding.textViewSignUp.setOnClickListener {  // Add this block
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        // Firebase Authentication
        binding.btnSignUp.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            binding.progressBar2.visibility = View.VISIBLE
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && binding.editTextConfirmPassword.text.toString().isNotEmpty()){

                if(password == binding.editTextConfirmPassword.text.toString()){

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Handler(Looper.getMainLooper()).postDelayed({
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