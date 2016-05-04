package org.weijian.mydriversrss;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.weijian.mydriversrss.request.NewsRequest;

import java.io.File;
import java.net.URL;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weijian on 5/3/16.
 */
class DownloadTask {
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    private URL url;
}

class DownloadManager {
    private static final long DISK_CACHE_SIZE = 10 * 1024 * 1024;

    private volatile static DownloadManager instance;

    private Context mContext;

    public OkHttpClient getClient() {
        return mClient;
    }

    public void setClient(OkHttpClient mClient) {
        this.mClient = mClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    private Retrofit mRetrofit;
    private OkHttpClient mClient;


    private File getDiskCacheDir() {
        File cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir();
        } else {
            cachePath = mContext.getCacheDir();
        }
        return cachePath;

    }

    private DownloadManager(Context context) {
        // get its Application mContext or it may cause memory leak;
        this.mContext = context.getApplicationContext();

        Cache cache = new Cache(getDiskCacheDir(), DISK_CACHE_SIZE);

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        mClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();


        mRetrofit = new Retrofit.Builder()
                .baseUrl(DownloadService.BASE_URL)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    /**
     * get the instance of DownloadManager
     *
     * @param context mContext
     * @return DownloadManager instance
     */
    public static DownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


}

public class DownloadService extends Service {

    public static final String RSS_ADDRESS =
            "http://dt.kkeji.com/api/2/contents?istop=0&sign=%s&xaid=%s&udid=%s&minid=%s&tid=1&cid=0";
    public static final String BASE_URL = "http://dt.kkeji.com";
    private IBinder binder = new DownloadBinder();

    DownloadManager manager;

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager = DownloadManager.getInstance(getApplicationContext());
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager = null;
    }

    public NewsRequest getNews(String signId, String xaId, String udId, String minId) {
        NewsRequest request = manager.getRetrofit().create(NewsRequest.class);
        return request;
    }
}
