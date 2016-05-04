package org.weijian.mydriversrss.request;

import org.weijian.mydriversrss.News;
import org.weijian.mydriversrss.NewsEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by weijian on 5/3/16.
 */
public interface NewsRequest {
    @GET("api/2/contents")
    Observable<NewsEntity> getNews(@Query("sign") String sign,
                                   @Query("xaid") String xaId,
                                   @Query("udid") String udId,
                                   @Query("minid") String minId,
                                   @Query("tid") String tId,
                                   @Query("cid") String cId);
}
