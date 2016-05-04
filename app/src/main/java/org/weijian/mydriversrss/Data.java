package org.weijian.mydriversrss;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("news")
    @Expose
    private List<News> news = new ArrayList<News>();

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    /**
     * @param news
     */
    public Data(List<News> news) {
        this.news = news;
    }

    /**
     * @return The news
     */
    public List<News> getNews() {
        return news;
    }

    /**
     * @param news The news
     */
    public void setNews(List<News> news) {
        this.news = news;
    }


}
