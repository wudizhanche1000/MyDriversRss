package org.weijian.mydriversrss

import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar

class DisplayActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    override fun onLoaderReset(loader: Loader<Cursor>?) {
        throw UnsupportedOperationException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        throw UnsupportedOperationException()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        throw UnsupportedOperationException()
    }

    companion object {
        const val URL_LOADER = 1
    }

    private var mRecyclerView: RecyclerView? = null
    private var mNewsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mServiceIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        var recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val rssServiceIntent = Intent(this, RssPullService::class.java)
        startService(rssServiceIntent)

        loaderManager.initLoader(URL_LOADER,null,this)


    }

}
