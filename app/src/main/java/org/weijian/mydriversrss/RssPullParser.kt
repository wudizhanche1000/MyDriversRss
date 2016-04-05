package org.weijian.mydriversrss

/**
 * Created by weijian on 16-3-30.
 */
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

data class News(var title: String? = null, var link: String? = null, var description: String? = null,
                var author: String? = null, var category: String? = null, var comment: String? = null,
                var guid: String? = null, var pubDate: Date? = null) {
}


class RssPullParser {
    companion object {
        const val ITEM = "ITEM"
        const val TITLE = "TITLE"
        const val LINK = "LINK"
        const val DESCRIPTION = "DESCRIPTION"
        const val AUTHOR = "AUTHOR"
        const val CATEGORY = "CATEGORY"
        const val COMMENTS = "COMMENTS"
        const val GUID = "GUID"
        const val PUBDATE = "PUBDATE"
    }

    fun parse(input: InputStream, encoding: String = "UTF-8"): List<News> {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        var parser = factory.newPullParser()
        parser.setInput(input, encoding)
        var eventType = parser.eventType
        var newsList = mutableListOf<News>()
        var news: News? = null
        loop@while (true) {
            var eventName = parser.name
            when (eventType) {
                XmlPullParser.START_DOCUMENT ->{}
                XmlPullParser.START_TAG -> {
                    if (eventName.equals(ITEM, true)) {
                        news = News()
                    } else if (eventName.equals(TITLE, true) && news != null) {
                        news.title = parser.nextText()
                    } else if (eventName.equals(LINK, true) && news != null) {
                        news.link = parser.nextText()
                    } else if (eventName.equals(DESCRIPTION, true) && news != null) {
                        news.description = parser.nextText()
                    } else if (eventName.equals(AUTHOR, true) && news != null) {
                        news.author = parser.nextText()
                    } else if (eventName.equals(CATEGORY, true) && news != null) {
                        news.category = parser.nextText()
                    } else if (eventName.equals(COMMENTS, true) && news != null) {
                        news.comment = parser.nextText()
                    } else if (eventName.equals(GUID, true) && news != null) {
                        news.guid = parser.nextText()
                    } else if (eventName.equals(PUBDATE, true) && news != null) {
                        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = dateFormat.parse(parser.nextText())
                        news.pubDate = date
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (eventName.equals(ITEM, true)) {
                        if (news != null) {
                            newsList.add(news)
                            news = null
                        }
                    }
                }
                XmlPullParser.END_DOCUMENT -> break@loop
            }
            eventType = parser.next()
        }
        return newsList
    }
}
