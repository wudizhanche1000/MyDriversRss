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

import org.jetbrains.annotations.NotNull;
import org.weijian.mydriversrss.EndlessOnScrollListener;
import org.weijian.mydriversrss.HttpMethods;
import org.weijian.mydriversrss.ImageManager;
import org.weijian.mydriversrss.News;
import org.weijian.mydriversrss.NewsEntity;
import org.weijian.mydriversrss.R;
import org.weijian.mydriversrss.request.NewsRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
    private Map<ImageView, String> map = new HashMap<>();

    @Override
    public void onRefresh() {
        retrieveNews("0");
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
        mRecyclerView.addOnScrollListener(
                new EndlessOnScrollListener() {
                    @Override
                    public void onScrolledToEnd(@NotNull RecyclerView recyclerView) {
                        RecyclerView.Adapter adapter = recyclerView.getAdapter();
                        if (adapter instanceof NewsAdapter) {
                            if (((NewsAdapter) adapter).newsSet.size() != 0) {
                                int articleId = ((NewsAdapter) adapter).newsSet.last().getArticleId();
                                retrieveNews(String.valueOf(articleId));
                            }
                        }
                    }
                }
        );
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mHttpMethods = HttpMethods.getInstance(getApplicationContext());
        ImageManager.getInstance(getApplicationContext()).setOnImageLoadedListener(listener);
        retrieveNews("0");
    }

    Observer<NewsEntity> observer = new Observer<NewsEntity>() {
        @Override
        public void onCompleted() {
            if (mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
            }
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(NewsEntity newsEntity) {
            mAdapter.addNewsCollections(newsEntity.getData().getNews());
        }
    };

    private void retrieveNews(String minId) {
        mHttpMethods.getRetrofit("http://dt.kkeji.com", true, true)
                .create(NewsRequest.class)
                .getNews("411203410", "ebbfb000", "39223325", minId, "1", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private ImageManager.onImageLoaded listener = new ImageManager.onImageLoaded() {
        @Override
        public void onFinish(ImageView view, Bitmap bitmap, String imageUrl) {
            String url = map.get(view);
            if (url != null && url.equals(imageUrl)) {
                view.setImageBitmap(bitmap);
            }
        }
    };

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

        private TreeSet<News> newsSet = new TreeSet<>();
        private News[] newsArray;


        public void addNewsCollections(Collection<News> newsCollection) {
            int size = newsSet.size();
            newsSet.addAll(newsCollection);
            notifyArray(size);
        }

        private void notifyArray(int size) {
            if (size != newsSet.size()) {
                newsArray = newsSet.toArray(new News[newsSet.size()]);
                notifyDataSetChanged();
            }
        }

        public void addNews(News news) {
            int size = newsSet.size();
            newsSet.add(news);
            notifyArray(size);
        }


        @Override
        public int getItemViewType(int position) {
            return newsArray[position].getImgs().size();
        }

        @Override
        public long getItemId(int position) {
            return newsArray[position].getArticleId();
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


        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            News news = newsArray[position];
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).titleView.setText(news.getTitle());
                for (int i = 0; i < news.getImgs().size(); i++) {
                    ImageView imageView = ((ItemViewHolder) holder).imageViews[i];
                    String imageUrl = news.getImgs().get(i);
                    map.put(imageView, imageUrl);
                    Bitmap bitmap = ImageManager.getInstance(mContext).getBitmap(imageUrl, imageView, 0, 0);
                    imageView.setImageBitmap(bitmap);

                }
            }

        }

        @Override
        public int getItemCount() {
            return newsSet.size();
        }
    }

}
