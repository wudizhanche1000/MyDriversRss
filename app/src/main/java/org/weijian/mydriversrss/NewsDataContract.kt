package org.weijian.mydriversrss

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * Created by weijian on 16-4-8.
 */
class NewsDataContract {
    companion object {
        const val NEWS_DATABASE_NAME = "NewsData.db"
        const val NEWS_DATABASE_VERSION = 0

        const val NEWS_TABLE_NAME = "News"

        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TITLE = TITLE
        const val COLUMN_LINK = RssPullParser.LINK
        const val COLUMN_DESCRIPTION = RssPullParser.DESCRIPTION
        const val COLUMN_AUTHOR = RssPullParser.AUTHOR
        const val COLUMN_CATEGORY = RssPullParser.CATEGORY
        const val COLUMN_COMMENTS = RssPullParser.COMMENTS
        const val COLUMN_GUID = RssPullParser.GUID
        const val COLUMN_PUBDATE = RssPullParser.PUBDATE
        const val CREATE_NEWS_TABLE_SQL = """CREATE TABLE $NEWS_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_GUID TEXT UNIQUE,
                $COLUMN_TITLE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_COMMENTS TEXT,
                $COLUMN_PUBDATE DATE)"""
    }

    class NewsDatabaseHelper constructor(context: Context) : SQLiteOpenHelper(context, NEWS_DATABASE_NAME, null, 0) {
        override fun onCreate(db: SQLiteDatabase?) {
            throw UnsupportedOperationException()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            throw UnsupportedOperationException()
        }

    }
}