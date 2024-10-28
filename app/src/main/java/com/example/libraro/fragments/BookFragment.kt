package com.example.libraro.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libraro.R
import com.example.libraro.databinding.FragmentBookBinding
import com.example.libraro.model.Book

class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!
    private var book: Book? = null
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        book = arguments?.getParcelable("currentBook")

        if (book != null) {
            Log.d("BookFragment", "Book received: ${book?.title}")
            Toast.makeText(requireContext(), "The book you are reading is ${book?.title}", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("BookFragment", "Book is null")
        }

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = binding.webView
        val webSettings = webView.settings

        webSettings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            defaultFontSize = 18 // Adjust as needed
            defaultTextEncodingName = "UTF-8"
        }

        // Custom CSS to style the Gutenberg HTML content
        val customCSS = """
            javascript:(function() {
                var style = document.createElement('style');
                style.innerHTML = `
                    body {
                        font-family: 'Georgia', serif;
                        line-height: 1.6;
                       
                        padding: 20px;
                        max-width: 800px;
                        margin: 0 auto;
                        background-color: #F0DEC6;
                    }
                    p {
                        margin-bottom: 1.2em;
                        text-align: justify;
                        font-size: 35px;
                    }
                    h1, h2, h3 {
                        color: #2C3E50;
                        margin-top: 1.5em;
                        margin-bottom: 0.8em;
                    }
                    img {
                        max-width: 100%;
                        height: auto;
                        display: block;
                        margin: 1em auto;
                    }
                    /* Hide Gutenberg header and footer */
                    .pgheader, .pgfooter, .pgnav {
                        display: none !important;
                    }
                    /* Improve readability */
                    @media (max-width: 768px) {
                        body {
                            padding: 15px;
                            font-size: 16px;
                        }
                    }
                `;
                document.head.appendChild(style);
            })()
        """

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Inject custom CSS after page loads
                webView.loadUrl(customCSS)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error loading book. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up toolbar
        binding.customToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            title = book?.title
            subtitle = book?.author

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            inflateMenu(R.menu.book_reading_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_bookmark -> {
                        Toast.makeText(requireContext(), "Working!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }

        // Load the book
        loadBook()
    }

    private fun loadBook() {
        // Use book's URL if available, otherwise use default
        val bookUrl = book?.pdfUrl ?: "https://www.gutenberg.org/files/1260/1260-h/1260-h.htm"
        webView.loadUrl(bookUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}