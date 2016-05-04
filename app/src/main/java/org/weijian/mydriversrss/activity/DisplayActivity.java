package org.weijian.mydriversrss.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.weijian.mydriversrss.HttpMethods;
import org.weijian.mydriversrss.ImageManager;
import org.weijian.mydriversrss.News;
import org.weijian.mydriversrss.NewsEntity;
import org.weijian.mydriversrss.R;
import org.weijian.mydriversrss.request.NewsRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.weijian.mydriversrss.Constants.NEWS_ITEM_TYPE_IMAGE;
import static org.weijian.mydriversrss.Constants.NEWS_ITEM_TYPE_MULTI_IMAGES;
import static org.weijian.mydriversrss.Constants.NEWS_ITEM_TYPE_NO_IMAGE;

/**
 * Created by weijian on 5/3/16.
 */
public class DisplayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private HttpMethods mHttpMethods;

    private NewsAdapter mAdapter;

    @Override
    public void onRefresh() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mAdapter = new NewsAdapter(getBaseContext());
        mAdapter.setHasStableIds(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mHttpMethods = HttpMethods.getInstance(getApplicationContext());

        Subscriber<News> subscriber = new Subscriber<News>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(News news) {
                mAdapter.addToTail(news);

            }
        };

        mHttpMethods.getRetrofit().create(NewsRequest.class)
                .getNews("411203410", "ebbfb000", "3922335061311473025", "0", "1", "0")
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<NewsEntity, Observable<News>>() {
                    @Override
                    public Observable<News> call(NewsEntity newsEntity) {
                        return Observable.from(newsEntity.getData().getNews());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    class NewsAdapter extends RecyclerView.Adapter {
        private Context mContext;

        public NewsAdapter(Context context) {
            mContext = context;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            public @BindView(R.id.news_title) TextView titleView;
            public @BindView(R.id.news_pub_date) TextView dateView;
            public ImageView[] imageViews;

            public ItemViewHolder(View itemView, int viewType) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                if (viewType == NEWS_ITEM_TYPE_MULTI_IMAGES)
                    imageViews = new ImageView[]{
                            (ImageView) itemView.findViewById(R.id.news_image1),
                            (ImageView) itemView.findViewById(R.id.news_image2),
                            (ImageView) itemView.findViewById(R.id.news_image3)
                    };
                else {
                    imageViews = new ImageView[]{
                            (ImageView) itemView.findViewById(R.id.news_image1)
                    };
                }
            }
        }

        List<News> newsList = new ArrayList<>();

        public void addNews(int position, News news) {
            newsList.add(position, news);
            notifyItemInserted(position);

        }

        public void addToTail(News news) {
            addNews(newsList.size(), news);
        }

        @Override
        public int getItemViewType(int position) {
            return newsList.get(position).getImgs().size();
        }

        @Override
        public long getItemId(int position) {
            return newsList.get(position).getArticleId();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = 0;
            switch (viewType) {
                case NEWS_ITEM_TYPE_NO_IMAGE:
                    layoutId = R.layout.news_item;
                    break;
                case NEWS_ITEM_TYPE_IMAGE:
                    layoutId = R.layout.news_item_image;
                    break;
                case NEWS_ITEM_TYPE_MULTI_IMAGES:
                    layoutId = R.layout.news_item_multi_images;
                    break;
            }
            View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            return new ItemViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            News news = newsList.get(position);
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).titleView.setText(news.getTitle());
                for (int i = 0; i < news.getImgs().size(); i++) {
                    ImageView imageView = ((ItemViewHolder) holder).imageViews[i];
                    Bitmap bitmap = ImageManager.INSTANCE.getBitmap(news.getImgs().get(i));
                    imageView.setImageBitmap(bitmap);
                }
            }

        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }

}
