package org.weijian.mydriversrss

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
            newsArray = Array(newsListObject.length(), { News() })
            for (i in 0..newsListObject.length()) {
                val newsObject = newsListObject.getJSONObject(i)
                val news = newsArray[i]
                news.title = newsObject.getString("title")
                news.articleId = newsObject.getInt("article_id")
                news.sourceUrl = newsObject.getString("source_url")
                news.picCount = newsObject.getInt("piccount")
                news.editor = newsObject.getString("editor")
                news.images = {
                    val imageObject = newsObject.getJSONArray("imgs")
                    Array<String>(news.picCount) {
                        imageObject.getString(it)
                    }
                }()
            }
        } catch(e: Exception) {
        }
        return newsArray
    }
}