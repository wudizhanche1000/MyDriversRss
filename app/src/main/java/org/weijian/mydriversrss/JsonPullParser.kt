package org.weijian.mydriversrss

import android.util.Log
import org.json.JSONObject

/**
 * Created by weijian on 16-4-12.
 */

class JsonPullParser {
    fun parse(json: String): Array<News>? {
        var newsArray: Array<News>? = null
        try {
            var jsonObject = JSONObject(json)
            var newsListObject = jsonObject.getJSONObject("data").getJSONArray("news")
            var size = newsListObject.length()
            newsArray = Array(size, { News() })
            for (i in 0..size - 1) {
                val newsObject = newsListObject.getJSONObject(i)
                val news = newsArray[i]
                news.title = newsObject.getString("title")
                news.articleId = newsObject.getInt("article_id")
                news.sourceUrl = newsObject.getString("source_url")
                news.picCount = newsObject.getInt("piccount")
                news.editor = newsObject.getString("editor")
                news.images = {
                    val imageObject = newsObject.getJSONArray("imgs")
                    Array<String>(imageObject.length()) { index ->
                        imageObject.getString(index)
                    }
                }()
            }
        } catch(e: Exception) {
            Log.e("JsonParser", e.message)
        }
        return newsArray
    }
}