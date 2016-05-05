package org.weijian.mydriversrss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by weijian on 16-4-1.
 */
public class ImageManager {
    private static final int MEMORY_CACHE_SIZE = 8 * 1024 * 1024;
    private static final int DISK_CACHE_SIZE = 64 * 1024 * 1024;
    private static volatile ImageManager instance;

    private Bitmap emptyPhoto;
    private Context mContext;
    private OkHttpClient mClient;
    private LruCache<String, byte[]> mLruCache;
    private DiskLruCache mDiskLruCache;

    private String getMD5String(String s) {
        final String algorithm = "MD5";
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digester != null;
        digester.update(s.getBytes());
        byte[] hash = digester.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1)
                stringBuilder.append('0');
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    private Observable.OnSubscribe<byte[]> getCache(final String imageUrl) {
        return new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                String hexString = getMD5String(imageUrl);
                byte[] data = null;
                try {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hexString);
                    if (snapshot != null) {
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        InputStream inputStream = snapshot.getInputStream(0);
                        int count = 0;
                        while ((count = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, count);
                        }
                        data = outputStream.toByteArray();
                        if (BuildConfig.DEBUG) {
                            System.out.println("Hit Disk Cache");
                        }
                        mLruCache.put(hexString, data);
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if (data == null) {
                    final Request request = new Request.Builder()
                            .url(imageUrl)
                            .build();
                    try {
                        Response response = mClient.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            subscriber.onError(new Exception(response.message()));
                        }
                        data = response.body().bytes();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }

                }
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        };
    }

    public interface onImageLoaded {
        void onFinish(ImageView view, Bitmap bitmap, String imageUrl);
    }

    private onImageLoaded onImageLoadedListener;

    public void setOnImageLoadedListener(onImageLoaded listener) {
        this.onImageLoadedListener = listener;
    }

    public Bitmap getBitmap(final String imageUrl, final ImageView view, int reqWidth, int reqHeight) {
        final String hexUrl = getMD5String(imageUrl);
        byte[] data = mLruCache.get(hexUrl);
        if (data != null) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        Observable.create(getCache(imageUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<byte[]>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(byte[] bytes) {
                                   mLruCache.put(hexUrl, bytes);
                                   Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                   onImageLoadedListener.onFinish(view, bitmap, imageUrl);

                               }
                           }
                );

        return emptyPhoto;
    }


    private ImageManager(Context context) {
        mContext = context.getApplicationContext();

        emptyPhoto = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);

        mClient = HttpMethods.getInstance(mContext).getHttpClient();
        try {
            mDiskLruCache = DiskLruCache.open(Utils.getDiskCacheDir(mContext)
                    , Utils.getAppVersion(mContext), 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLruCache = new LruCache<String, byte[]>(MEMORY_CACHE_SIZE) {
            @Override
            protected void entryRemoved(boolean evicted, String key, byte[] oldValue, byte[] newValue) {
                if (evicted && mDiskLruCache != null) {
                    try {
                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        OutputStream outputStream = editor.newOutputStream(0);
                        outputStream.write(oldValue);
                        outputStream.close();
                        editor.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected int sizeOf(String key, byte[] value) {
                return value.length;
            }
        };
    }


    public static ImageManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager(context);
                }
            }
        }
        return instance;
    }
}
