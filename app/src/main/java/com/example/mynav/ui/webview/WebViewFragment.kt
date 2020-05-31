package com.example.mynav.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.mynav.R

class WebViewFragment : Fragment() {

    private lateinit var webViewViewModel: WebViewViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        webViewViewModel =
                ViewModelProviders.of(this).get(WebViewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val web: WebView = root.findViewById(R.id.wb)
        web.settings.allowFileAccess = true;
        web.settings.allowFileAccessFromFileURLs = true;
        web.settings.allowContentAccess = true;
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        web.settings.domStorageEnabled = true
        web.settings.javaScriptEnabled = true
        web.loadUrl("javascript:callJS()")
        web.loadUrl("https://i.youbike.com.tw/home")
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        return root
    }
}
