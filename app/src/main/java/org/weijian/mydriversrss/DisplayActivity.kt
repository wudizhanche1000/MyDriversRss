package org.weijian.mydriversrss

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.net.URLDecoder

class DisplayActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mServiceIntent: Intent? = null
    private var mStatusReciver: StatusReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mServiceIntent = Intent(this, RssPullService::class.java)
        startService(mServiceIntent)
        val statusIntentFilter = IntentFilter(Constants.BROADCAST_ACTION)
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        mStatusReciver = StatusReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(mStatusReciver, statusIntentFilter)
    }

    inner class RetrieveDatabaseTask : AsyncTask<Unit, Unit, List<News>>() {
        override fun doInBackground(vararg params: Unit?): List<News> {
            var dbHelper = NewsDbHelper(this@DisplayActivity)
            var db = dbHelper.readableDatabase
            var cursor = db.rawQuery("SELECT * FROM ${NewsDbHelper.TABLE_NAME}", null)
            var newsList = mutableListOf<News>()
            cursor.moveToFirst()
            do {
                val title = cursor.getString(cursor.getColumnIndex(NewsDbHelper.COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndex(NewsDbHelper.COLUMN_DESCRIPTION))
                newsList.add(News(title = title, description = description))
            } while (cursor.moveToNext())
            return newsList
        }

        override fun onPostExecute(result: List<News>) {
            var linearLayoutManager = LinearLayoutManager(this@DisplayActivity.baseContext)
            mRecyclerView?.layoutManager = linearLayoutManager
            mRecyclerView?.adapter = NewsAdapter(result)


        }
    }

    inner class StatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.extras.get(Constants.EXTENDED_DATA_STATUS)
            when (status) {
                Constants.STATE_ACTION_COMPLETE -> {
                    Log.d("BROADCAST_RECEIVER", "STATE_ACTION_COMPLETE")
                    RetrieveDatabaseTask().execute()
                }
            }
        }

    }

    inner class NewsAdapter constructor(var newsList: List<News>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        inner class NewsHolder constructor(var cardView: CardView) : RecyclerView.ViewHolder(cardView) {
            var titleView: TextView? = null
            var descriptionView: TextView? = null
            var newsImageView: ImageView? = null

            init {
                titleView = cardView.findViewById(R.id.news_title) as TextView
                descriptionView = cardView.findViewById(R.id.news_description) as TextView
                newsImageView = cardView.findViewById(R.id.news_image) as ImageView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            var newsCard = LayoutInflater.from(this@DisplayActivity).inflate(R.layout.newscard_view, parent, false) as CardView
            var newsHolder = NewsHolder(newsCard)
            return newsHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            var newsHolder = holder as NewsHolder
            val item = newsList[position]
            newsHolder.titleView?.text = item.title
            newsHolder.titleView?.paint?.isFakeBoldText = true
            newsHolder.titleView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            newsHolder.descriptionView?.text = Html.fromHtml(item.description).toString().substring(0..25) + "..."
            newsHolder.descriptionView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        override fun getItemCount(): Int {
            return newsList.size
        }

    }
}
