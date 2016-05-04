package org.weijian.mydriversrss;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weijian on 5/4/16.
 */
public class HttpMethods {
    public static final String BASE_URL = "http://dt.kkeji.com";

    private OkHttpClient mClient;

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public void setRetrofit(Retrofit mRetrofit) {
        this.mRetrofit = mRetrofit;
    }

    private Retrofit mRetrofit;
    private volatile static HttpMethods instance;
    private Context mContext;
    static final int HTTP_CACHE_SIZE = 16 * 1024 * 1024;

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

    private HttpMethods(Context context) {
        mContext = context;
        Cache cache = new Cache(getDiskCacheDir(), HTTP_CACHE_SIZE);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        mClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();
    }

    public static HttpMethods getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

}
