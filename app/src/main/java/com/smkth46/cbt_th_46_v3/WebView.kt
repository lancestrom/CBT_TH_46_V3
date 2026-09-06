package com.smkth46.cbt_th_46_v3

import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val swipeRefresh: SwipeRefreshLayout = findViewById(R.id.swipeRefresh)
        val webView: WebView = findViewById(R.id.webView)
        
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                swipeRefresh.isRefreshing = false
                super.onPageFinished(view, url)
            }
        }
        
        val encryptedUrl = BuildConfig.ENCRYPTED_URL
        val baseUrl = decryptUrl(encryptedUrl)
        webView.loadUrl("http://$baseUrl/cbt2.6client")

        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun decryptUrl(encrypted: String): String {
        val key = "CBT_SECRET_KEY"
        val decoded = String(Base64.decode(encrypted, Base64.DEFAULT))
        return decoded.mapIndexed { index, char ->
            (char.code xor key[index % key.length].code).toChar()
        }.joinToString("")
    }
}