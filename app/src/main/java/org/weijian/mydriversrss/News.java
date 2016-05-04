package org.weijian.mydriversrss;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class News {

    @SerializedName("article_id")
    @Expose
    private int articleId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_long")
    @Expose
    private String titleLong;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("article_type")
    @Expose
    private int articleType;
    @SerializedName("ban_review")
    @Expose
    private int banReview;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("flag")
    @Expose
    private int flag;
    @SerializedName("tag_id")
    @Expose
    private int tagId;
    @SerializedName("display_title")
    @Expose
    private String displayTitle;
    @SerializedName("article_url")
    @Expose
    private String articleUrl;
    @SerializedName("source_url")
    @Expose
    private String sourceUrl;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    @SerializedName("review_count")
    @Expose
    private int reviewCount;
    @SerializedName("digg_count")
    @Expose
    private int diggCount;
    @SerializedName("bury_count")
    @Expose
    private int buryCount;
    @SerializedName("is_digg")
    @Expose
    private int isDigg;
    @SerializedName("is_bury")
    @Expose
    private int isBury;
    @SerializedName("is_repin")
    @Expose
    private int isRepin;
    @SerializedName("editor")
    @Expose
    private String editor;
    @SerializedName("piccount")
    @Expose
    private int piccount;
    @SerializedName("ispass")
    @Expose
    private int ispass;
    @SerializedName("isdel")
    @Expose
    private int isdel;
    @SerializedName("islive")
    @Expose
    private int islive;
    @SerializedName("imgs")
    @Expose
    private List<String> imgs = new ArrayList<String>();
    @SerializedName("pub_time")
    @Expose
    private int pubTime;
    @SerializedName("showtype")
    @Expose
    private int showtype;

    /**
     * No args constructor for use in serialization
     */
    public News() {
    }

    /**
     * @param pubTime
     * @param summary
     * @param buryCount
     * @param islive
     * @param isDigg
     * @param diggCount
     * @param ispass
     * @param piccount
     * @param sourceUrl
     * @param tagId
     * @param titleLong
     * @param articleId
     * @param articleUrl
     * @param displayTitle
     * @param title
     * @param isdel
     * @param imgs
     * @param isBury
     * @param showtype
     * @param isRepin
     * @param editor
     * @param flag
     * @param source
     * @param reviewCount
     * @param shareUrl
     * @param banReview
     * @param articleType
     */
    public News(int articleId, String title, String titleLong, String summary, int articleType, int banReview, String source, int flag, int tagId, String displayTitle, String articleUrl, String sourceUrl, String shareUrl, int reviewCount, int diggCount, int buryCount, int isDigg, int isBury, int isRepin, String editor, int piccount, int ispass, int isdel, int islive, List<String> imgs, int pubTime, int showtype) {
        this.articleId = articleId;
        this.title = title;
        this.titleLong = titleLong;
        this.summary = summary;
        this.articleType = articleType;
        this.banReview = banReview;
        this.source = source;
        this.flag = flag;
        this.tagId = tagId;
        this.displayTitle = displayTitle;
        this.articleUrl = articleUrl;
        this.sourceUrl = sourceUrl;
        this.shareUrl = shareUrl;
        this.reviewCount = reviewCount;
        this.diggCount = diggCount;
        this.buryCount = buryCount;
        this.isDigg = isDigg;
        this.isBury = isBury;
        this.isRepin = isRepin;
        this.editor = editor;
        this.piccount = piccount;
        this.ispass = ispass;
        this.isdel = isdel;
        this.islive = islive;
        this.imgs = imgs;
        this.pubTime = pubTime;
        this.showtype = showtype;
    }

    /**
     * @return The articleId
     */
    public int getArticleId() {
        return articleId;
    }

    /**
     * @param articleId The article_id
     */
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The titleLong
     */
    public String getTitleLong() {
        return titleLong;
    }

    /**
     * @param titleLong The title_long
     */
    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The articleType
     */
    public int getArticleType() {
        return articleType;
    }

    /**
     * @param articleType The article_type
     */
    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }

    /**
     * @return The banReview
     */
    public int getBanReview() {
        return banReview;
    }

    /**
     * @param banReview The ban_review
     */
    public void setBanReview(int banReview) {
        this.banReview = banReview;
    }

    /**
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return The flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * @param flag The flag
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * @return The tagId
     */
    public int getTagId() {
        return tagId;
    }

    /**
     * @param tagId The tag_id
     */
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    /**
     * @return The displayTitle
     */
    public String getDisplayTitle() {
        return displayTitle;
    }

    /**
     * @param displayTitle The display_title
     */
    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    /**
     * @return The articleUrl
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * @param articleUrl The article_url
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * @return The sourceUrl
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * @param sourceUrl The source_url
     */
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * @return The shareUrl
     */
    public String getShareUrl() {
        return shareUrl;
    }

    /**
     * @param shareUrl The share_url
     */
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    /**
     * @return The reviewCount
     */
    public int getReviewCount() {
        return reviewCount;
    }

    /**
     * @param reviewCount The review_count
     */
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     * @return The diggCount
     */
    public int getDiggCount() {
        return diggCount;
    }

    /**
     * @param diggCount The digg_count
     */
    public void setDiggCount(int diggCount) {
        this.diggCount = diggCount;
    }

    /**
     * @return The buryCount
     */
    public int getBuryCount() {
        return buryCount;
    }

    /**
     * @param buryCount The bury_count
     */
    public void setBuryCount(int buryCount) {
        this.buryCount = buryCount;
    }

    /**
     * @return The isDigg
     */
    public int getIsDigg() {
        return isDigg;
    }

    /**
     * @param isDigg The is_digg
     */
    public void setIsDigg(int isDigg) {
        this.isDigg = isDigg;
    }

    /**
     * @return The isBury
     */
    public int getIsBury() {
        return isBury;
    }

    /**
     * @param isBury The is_bury
     */
    public void setIsBury(int isBury) {
        this.isBury = isBury;
    }

    /**
     * @return The isRepin
     */
    public int getIsRepin() {
        return isRepin;
    }

    /**
     * @param isRepin The is_repin
     */
    public void setIsRepin(int isRepin) {
        this.isRepin = isRepin;
    }

    /**
     * @return The editor
     */
    public String getEditor() {
        return editor;
    }

    /**
     * @param editor The editor
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * @return The piccount
     */
    public int getPiccount() {
        return piccount;
    }

    /**
     * @param piccount The piccount
     */
    public void setPiccount(int piccount) {
        this.piccount = piccount;
    }

    /**
     * @return The ispass
     */
    public int getIspass() {
        return ispass;
    }

    /**
     * @param ispass The ispass
     */
    public void setIspass(int ispass) {
        this.ispass = ispass;
    }

    /**
     * @return The isdel
     */
    public int getIsdel() {
        return isdel;
    }

    /**
     * @param isdel The isdel
     */
    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    /**
     * @return The islive
     */
    public int getIslive() {
        return islive;
    }

    /**
     * @param islive The islive
     */
    public void setIslive(int islive) {
        this.islive = islive;
    }

    /**
     * @return The imgs
     */
    public List<String> getImgs() {
        return imgs;
    }

    /**
     * @param imgs The imgs
     */
    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    /**
     * @return The pubTime
     */
    public int getPubTime() {
        return pubTime;
    }

    /**
     * @param pubTime The pub_time
     */
    public void setPubTime(int pubTime) {
        this.pubTime = pubTime;
    }

    /**
     * @return The showtype
     */
    public int getShowtype() {
        return showtype;
    }

    /**
     * @param showtype The showtype
     */
    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }


}
