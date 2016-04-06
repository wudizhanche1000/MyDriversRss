package org.weijian.mydriversrss

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.DateFormat

class NewsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_NEWS_DB)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        throw UnsupportedOperationException()
    }

    companion object {
        const val DATABASE_NAME = "NewsData.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "News"

        const val COLUMN_TITLE = "TITLE"
        const val COLUMN_LINK = "LINK"
        const val COLUMN_DESCRIPTION = "DESCRIPTION"
        const val COLUMN_AUTHOR = "AUTHOR"
        const val COLUMN_CATEGORY = "CATEGORY"
        const val COLUMN_COMMENT = "COMMENT"
        const val COLUMN_GUID = "GUID"
        const val COLUMN_PUBDATE = "PUB_DATE"

        const val CREATE_NEWS_DB = """CREATE TABLE $TABLE_NAME (
                $COLUMN_GUID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_COMMENT TEXT,
                $COLUMN_PUBDATE DATE)"""
        const val INSERT_NEWS = """INSERT INTO NEWS ($COLUMN_GUID,$COLUMN_TITLE,$COLUMN_LINK,
        $COLUMN_DESCRIPTION,$COLUMN_AUTHOR,$COLUMN_CATEGORY,$COLUMN_COMMENT,
        $COLUMN_PUBDATE) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"""
    }


    fun exists(news: News): Boolean {
        val db = this.readableDatabase
        val sql = "SELECT $COLUMN_GUID FROM $TABLE_NAME WHERE $COLUMN_GUID=?"
        val cursor = db.rawQuery(sql, arrayOf(news.guid))
        val exists = cursor.count >= 1
        cursor.close()
        return exists
    }

    fun insertNews(newsList: Collection<News>) {
        val db = this.writableDatabase
        try {
            db.beginTransaction()
            for (news in newsList) {
                if (!exists(news)) {
                    val dateString = DateFormat.getDateInstance().format(news.pubDate)
                    db.execSQL(INSERT_NEWS, arrayOf(news.guid, news.title, news.link, news.description, news.author, news.category, news.comment, dateString))
                }
            }
        } finally {
            if(db.inTransaction()){
                db.endTransaction()
            }
            db.close()
        }
    }
}
