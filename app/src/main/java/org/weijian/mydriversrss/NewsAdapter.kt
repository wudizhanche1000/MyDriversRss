package org.weijian.mydriversrss

import android.view.ViewGroup

/**
 * Created by weijian on 16-4-1.
 */
class NewsAdapter constructor(var newsList: List<News>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    public class NewsHolder constructor(var cardView: CardView) : RecyclerView.ViewHolder(cardView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun getItemCount(): Int {
        throw UnsupportedOperationException()
    }

}
