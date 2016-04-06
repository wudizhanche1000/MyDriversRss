package org.weijian.mydriversrss

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import org.weijian.mydriversrss.NewsProviderContract.*;

private const val DATABASE_NAME = "NewsData"
private const val DATABASE_VERSION = 1
private const val NEWS_TABLE_NAME = "News"
private const val COLUMN_ID = BaseColumns._ID
private const val COLUMN_TITLE = "TITLE"
private const val COLUMN_LINK = "LINK"
private const val COLUMN_DESCRIPTION = "DESCRIPTION"
private const val COLUMN_AUTHOR = "AUTHOR"
private const val COLUMN_CATEGORY = "CATEGORY"
private const val COLUMN_COMMENT = "COMMENT"
private const val COLUMN_GUID = "GUID"
private const val COLUMN_PUBDATE = "PUB_DATE"

private const val CREATE_NEWS_DB_SQL = """CREATE TABLE $NEWS_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_GUID TEXT,
                $COLUMN_TITLE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_COMMENT TEXT,
                $COLUMN_PUBDATE DATE)"""


class NewsProvider : ContentProvider() {

    private var mProviderHelper: SQLiteOpenHelper

    init {
        mProviderHelper = NewsProviderHelper(context)
    }

    companion object {
        const val AUTHORITY = ""
        val mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            mUriMatcher.addURI(AUTHORITY, NEWS_TABLE_NAME, NEWS_URI_CODE)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.

        when(mUriMatcher.match(uri)){
            NewsProviderContract.NEWS_URI_CODE->{

            }
        }
        return -1
    }

    override fun getType(uri: Uri): String? {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO: Implement this to handle requests to insert a new row.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate(): Boolean {
        // TODO: Implement this to initialize your content provider on startup.
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // TODO: Implement this to handle query requests from clients.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        // TODO: Implement this to handle requests to update one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    inner class NewsProviderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
            null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_NEWS_DB_SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            throw UnsupportedOperationException()
        }
    }
}
