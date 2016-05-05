package org.weijian.mydriversrss;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
    private OkHttpClient mClient;
    private volatile Map<String, Retrofit> retrofitMap = new HashMap<>();
    private volatile static HttpMethods instance;
    private Context mContext;
    static final int HTTP_CACHE_SIZE = 16 * 1024 * 1024;

    public Retrofit getRetrofit(String baseUrl, boolean rxjava, boolean convertGson) {
        if (retrofitMap.containsKey(baseUrl))
            return retrofitMap.get(baseUrl);
        else {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(mClient);
            if (rxjava) {
                builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            }
            if (convertGson) {
                builder.addConverterFactory(GsonConverterFactory.create());
            }
            Retrofit retrofit = builder.build();
            retrofitMap.put(baseUrl, retrofit);
            return retrofit;
        }
    }


    private HttpMethods(Context context) {
        mContext = context;
        Cache cache = new Cache(Utils.getDiskCacheDir(mContext), HTTP_CACHE_SIZE);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        mClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    public OkHttpClient getHttpClient() {
        return mClient;
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
