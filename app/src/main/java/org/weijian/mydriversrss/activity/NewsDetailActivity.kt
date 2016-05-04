package org.weijian.mydriversrss.activity

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import org.jsoup.Jsoup
import org.weijian.mydriversrss.R
import java.net.URL

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS_URL = "NEWS_URL"
    }

    lateinit var mNewsInfo: TextView
    lateinit var retrieveTask: RetrieveNewsTask
    lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        mNewsInfo = findViewById(R.id.news_info) as TextView
        retrieveTask = RetrieveNewsTask()

        val url = intent.getStringExtra(NEWS_URL)
        retrieveTask.execute(url)

    }

    class ImageGetter : Html.ImageGetter {
        override fun getDrawable(source: String?): Drawable? {
            var drawable: Drawable? = null
            if (source != null) {
                val url = URL(source)
                drawable = Drawable.createFromStream(url.openStream(), "")
            }
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
    }

    inner class RetrieveNewsTask : AsyncTask<String, Void, Spanned>() {
        override fun doInBackground(vararg params: String?): Spanned {
            var doc = Jsoup.connect(params[0]).get()
            val newsInfo = doc.getElementsByClass("news_info").select("p")
            val html = Html.fromHtml(newsInfo.html(), ImageGetter(), Html.TagHandler { opening, tag, output, xmlReader -> })
            return html
        }

        override fun onPostExecute(result: Spanned) {
            mNewsInfo.text = result
        }
    }
}
