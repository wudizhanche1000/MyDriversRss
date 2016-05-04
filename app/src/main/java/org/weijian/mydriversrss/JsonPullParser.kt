package org.weijian.mydriversrss

import android.util.Log
import org.json.JSONObject

/**
 * Created by weijian on 16-4-12.
 */

class JsonPullParser {
    private companion object {
        const val TITLE = "title"
        const val ARTICLE_ID = "article_id"
        const val SOURCE_URL = "source_url"
        const val PICTURE_COUNT = "piccount"
        const val PUBLISH_TIME = "pub_time"
        const val EDITOR = "editor"
        const val IMAGES = "imgs"
    }

    fun parse(json: String): Array<News>? {
        var newsArray: Array<News>? = null
//        try {
//            var jsonObject = JSONObject(json)
//            var newsListObject = jsonObject.getJSONObject("data").getJSONArray("news")
//            var size = newsListObject.length()
//            newsArray = Array(size, { News() })
//            for (i in 0..size - 1) {
//                val newsObject = newsListObject.getJSONObject(i)
//                val news = newsArray[i]
//                news.title = newsObject.getString(TITLE)
//                news.articleId = newsObject.getInt(ARTICLE_ID)
//                news.sourceUrl = newsObject.getString(SOURCE_URL)
//                news.picCount = newsObject.getInt(PICTURE_COUNT)
//                news.pubTime = newsObject.getLong(PUBLISH_TIME)
//                news.editor = newsObject.getString(EDITOR)
//                news.images = {
//                    val imageObject = newsObject.getJSONArray(IMAGES)
//                    Array<String>(imageObject.length()) { index ->
//                        imageObject.getString(index)
//                    }
//                }()
//            }
//        } catch(e: Exception) {
//            Log.e("JsonParser", e.message)
//        }
        return newsArray
    }
}