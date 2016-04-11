package org.weijian.mydriversrss

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import java.net.URL

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS_URL = "NEWS_URL"
    }

    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        webView = findViewById(R.id.webview) as WebView
        val url = URL(intent.getStringExtra(NEWS_URL))
        webView.loadUrl(url.toString())

    }

    inner class RetrieveNews : AsyncTask<URL, Void, String>() {
        override fun doInBackground(vararg params: URL?): String? {
            throw UnsupportedOperationException()
        }

    }
}
