package org.weijian.mydriversrss

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar

class DisplayActivity : AppCompatActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mNewsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mServiceIntent: Intent?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        var recyclerView=findViewById(R.id.recycler_view) as RecyclerView
        var linearLayoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=linearLayoutManager

        val rssServiceIntent=Intent(this,RssPullService::class.java)
        startService(rssServiceIntent)


    }

}
