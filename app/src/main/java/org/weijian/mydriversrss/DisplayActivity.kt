package org.weijian.mydriversrss

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.design.widget.AppBarLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class NewsResultReceiver constructor(handler: Handler, receiver: NewsReceiver) : ResultReceiver(handler) {
    companion object {
        const val NEWS_RESULT_RECEIVER = "NEWS_RESULT_RECEIVER"
    }

    interface NewsReceiver {
        fun onReceiveResult(resultCode: Int, resultData: Bundle?)
    }

    var receiver: NewsReceiver

    init {
        this.receiver = receiver
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        super.onReceiveResult(resultCode, resultData)
        receiver.onReceiveResult(resultCode, resultData)
    }
}

class DisplayActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var mToolbar: Toolbar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mStatusReciver: StatusReceiver
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mAppbarLayout: AppBarLayout
    private lateinit var mStatusIntentFilter: IntentFilter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        // Init variables
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mProgressBar = findViewById(R.id.progress_bar) as ProgressBar
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh) as SwipeRefreshLayout
        mAppbarLayout = findViewById(R.id.appbarLayout) as AppBarLayout


        // get or generate rss parameters
        val signId = savedInstanceState?.getString(Constants.RSS_SIGN_ID) ?: { Random().nextInt().toString() }()
        val xaId = savedInstanceState?.getString(Constants.RSS_XAID) ?: {
            // Generate random XAID
            var stringBuilder = StringBuilder()
            val random = Random()
            kotlin.repeat(8) {
                stringBuilder.append(Integer.toHexString(random.nextInt(16)))
            }
            stringBuilder.toString()
        }()
        val udId = savedInstanceState?.getString(Constants.RSS_UDID) ?: { Random().nextLong().toString() }()
        val minId = savedInstanceState?.getString(Constants.RSS_MINID) ?: "0"


        // Set toolbar
        setSupportActionBar(mToolbar)
        // Set RecyclerView layoutManager
        var linearLayoutManager = LinearLayoutManager(this.baseContext)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val adapter = NewsAdapter(this.baseContext, signId = signId, xaId = xaId, minId = minId, udId = udId)
        adapter.setHasStableIds(true)
        mRecyclerView.adapter = adapter

        // Set RefreshLayout Listener
        mSwipeRefreshLayout.setOnRefreshListener(this)





        // register broadcast receiver
        mStatusIntentFilter = IntentFilter(Constants.BROADCAST_ACTION)
        mStatusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        mStatusReciver = StatusReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(mStatusReciver, mStatusIntentFilter)

        // TODO delete this after test
        mProgressBar.visibility = View.GONE

    }


    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStatusReciver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_display, menu)
        return true
    }

    override fun onRefresh() {
        val adapter = mRecyclerView.adapter as NewsAdapter
        adapter.refresh()
    }


    inner class StatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.extras.get(Constants.EXTENDED_DATA_STATUS)
            when (status) {
                Constants.STATE_ACTION_COMPLETE -> {
                    Log.d("BROADCAST_RECEIVER", "STATE_ACTION_COMPLETE")
                    if (mSwipeRefreshLayout.isRefreshing) {
                        mSwipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

    }

    inner class NewsAdapter constructor(val context: Context, var signId: String, var xaId: String
                                        , var minId: String, var udId: String) :
            RecyclerView.Adapter<RecyclerView.ViewHolder>(), NewsResultReceiver.NewsReceiver {
        private lateinit var mServiceIntent: Intent

        var newsList: MutableList<News> = mutableListOf()

        private var mResultReceiver: NewsResultReceiver


        init {
            mResultReceiver = NewsResultReceiver(Handler(), receiver = this)
            mServiceIntent = Intent(context, RssPullService::class.java)
            mServiceIntent.putExtra(Constants.RSS_MINID, minId)
            mServiceIntent.putExtra(Constants.RSS_SIGN_ID, signId)
            mServiceIntent.putExtra(Constants.RSS_XAID, xaId)
            mServiceIntent.putExtra(Constants.RSS_UDID, udId)
            mServiceIntent.putExtra(NewsResultReceiver.NEWS_RESULT_RECEIVER, mResultReceiver)
            refresh()
        }

        fun refresh() {
            // start RssPullService at startup
            startService(mServiceIntent)
        }

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            when (resultCode) {
                Constants.STATE_FETCH_COMPLETE -> {
                    if (resultData != null) {
                        val newsArray = resultData.getSerializable(Constants.NEWS_ITEM_RESULT) as Array<News>
                        val new = mutableSetOf(*newsArray)
                        new.removeAll(newsList)
                        val changeList = new.toMutableList()
                        Collections.sort(changeList) { lhs, rhs ->
                            when {
                                lhs.pubTime > rhs.pubTime -> 1
                                lhs.pubTime < rhs.pubTime -> -1
                                else -> 0
                            }
                        }
                        Collections.reverse(changeList)

                        for (item in changeList) {
                            newsList.add(0, item)
                        }

                        if (changeList.size > 0)
                            notifyItemRangeInserted(0, changeList.size)
                        //                        notifyDataSetChanged()
                    }
                }
            }
            Log.d("RECEIVER", resultData.toString())
        }

        override fun getItemCount(): Int {
            return newsList.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (newsList[position].images.size) {
                0 -> 0
                1 -> 1
                else -> 2
            }
        }

        override fun getItemId(position: Int): Long {
            return newsList[position].articleId.toLong()
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            var newsHolder = viewHolder as NewsHolder
            val newsItem = newsList[position]
            val itemType = newsItem.images.size
            //TODO Add handler when itemType is not 0
            when (itemType) {
                1 -> {
                }
                2 -> {

                }
            }
            newsHolder.titleView.text = newsItem.title
            newsHolder.titleView.paint?.isFakeBoldText = true
            newsHolder.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            val dateFormat = SimpleDateFormat("MM-dd HH:mm")
            newsHolder.pubDateView.text = dateFormat.format(Date(newsItem.pubTime * 1000))
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            val layoutId = when (viewType) {
                0 -> R.layout.news_item
                1 -> R.layout.news_item_image
                else -> R.layout.news_item_multi_images
            }
            var newsCard = LayoutInflater.from(context).inflate(layoutId, parent, false) as CardView
            var newsHolder = NewsHolder(newsCard, viewType)
            return newsHolder
        }

        inner class NewsHolder constructor(var cardView: CardView, viewType: Int) : RecyclerView.ViewHolder(cardView) {
            lateinit var titleView: TextView
            lateinit var descriptionView: TextView
            lateinit var newsImageView: ImageView
            lateinit var commentCountView: TextView
            lateinit var pubDateView: TextView
            lateinit var artileId: String

            init {
                titleView = cardView.findViewById(R.id.news_title) as TextView
                pubDateView = cardView.findViewById(R.id.news_pub_date) as TextView
                commentCountView = cardView.findViewById(R.id.news_comments_count) as TextView
                //TODO Add handler when itemType is not 0
                when (viewType) {
                    1 -> {

                    }
                    2 -> {
                    }
                }
            }
        }
    }
}
